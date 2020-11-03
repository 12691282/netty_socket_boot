package io.renren.modules.netty.handler;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.renren.common.utils.BeanUtils;
import io.renren.config.Commont;
import io.renren.modules.netty.dao.AcceptDataDao;
import io.renren.modules.netty.entity.AcceptDataEntity;
import io.renren.modules.netty.model.RequestModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
@ChannelHandler.Sharable
public class LongLinksMessageDataHandler extends ChannelInboundHandlerAdapter {


    /**
     * 获取事务控制管理器
     */
    private DataSourceTransactionManager transactionManager;


    /**
     * 管理一个全局map，保存连接进服务端的通道数量
     */
    private static final ConcurrentHashMap<ChannelId, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();


    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();

        String clientIp = insocket.getAddress().getHostAddress();
        int clientPort = insocket.getPort();

        //获取连接通道唯一标识
        ChannelId channelId = ctx.channel().id();

        //如果map中不包含此连接，就保存连接
        if (CHANNEL_MAP.containsKey(channelId)) {
            log.info("客户端【" + channelId + "】是连接状态，连接通道数量: " + CHANNEL_MAP.size());
        } else {
            //保存连接
            CHANNEL_MAP.put(channelId, ctx);
            log.info("客户端【" + channelId + "】连接netty服务器[IP:" + clientIp + "--->PORT:" + clientPort + "]");
            log.info("连接通道数量: " + CHANNEL_MAP.size());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();

        String clientIp = insocket.getAddress().getHostAddress();

        ChannelId channelId = ctx.channel().id();

        //包含此客户端才去删除
        if (CHANNEL_MAP.containsKey(channelId)) {
            //删除连接
            CHANNEL_MAP.remove(channelId);

            System.out.println();
            log.info("客户端【" + channelId + "】退出netty服务器[IP:" + clientIp + "--->PORT:" + insocket.getPort() + "]");
            log.info("连接通道数量: " + CHANNEL_MAP.size());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        log.info("加载客户端报文......");
        log.info("【" + ctx.channel().id() + "】" + " :" + msg);
        if (msg == null) {
            log.info("服务端响应空的消息");
            return;
        }

        ChannelHandlerContext channelHandlerContext = CHANNEL_MAP.get(ctx.channel().id());

        if (channelHandlerContext == null) {
            log.info("通道【" + ctx.channel().id() + "】不存在");
            return;
        }

        RequestModel requestModel = (RequestModel)msg;
        AcceptDataEntity entity = new AcceptDataEntity();

        entity.setBusinessCode(requestModel.getHeadData());
        entity.setBusinessContent(requestModel.getContentData());
        log.info("原始请求数据 headMsg {} contentMsg {} " , entity.getBusinessCode(), entity.getBusinessContent());

        transactionManager = BeanUtils.getBean(DataSourceTransactionManager.class);
        TransactionDefinition transactionDefinition =  BeanUtils.getBean(TransactionDefinition.class);
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);

        try {

            AcceptDataDao acceptDataDao =  BeanUtils.getBean(AcceptDataDao.class);
            acceptDataDao.insert(entity);
            //手动提交事务
            transactionManager.commit(transactionStatus);

        }catch (Exception e) {
            //手动回滚事物
            transactionManager.rollback(transactionStatus);
            e.printStackTrace();
        }
        //响应客户端
        this.sendCodeToClient(ctx, requestModel);

    }

    private void sendCodeToClient(ChannelHandlerContext ctx, RequestModel msgCode) {
        /**
         * 需要响应就可以回复消息    跟读取数据时候一样  根据数据类型对应字节回复消息即可
         */
        ctx.writeAndFlush(msgCode);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("AuthServerInitHandler channelReadComplete");
        ctx.flush();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("level-server  channelRead Exception ....");
        cause.printStackTrace();
        ctx.close();
    }



}
