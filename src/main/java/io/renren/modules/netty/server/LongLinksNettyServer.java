package io.renren.modules.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.EventExecutorGroup;
import io.renren.modules.netty.decoder.ByteToModelDecoder;
import io.renren.modules.netty.encoder.ModelMessageToByteEncoder;
import io.renren.modules.netty.handler.LongLinksMessageDataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 平台接收数据服务
 *
 * @author 李强
 * @date 2020-09-11
 */


public class LongLinksNettyServer implements Runnable{

    private Logger logger = LoggerFactory.getLogger(LongLinksNettyServer.class);

    private int port;

    private NioEventLoopGroup bossGroup;

    private NioEventLoopGroup workerGroup;

    private EventExecutorGroup businessGroup;

    private LongLinksMessageDataHandler messageDataHandler;

    public LongLinksNettyServer(int port, NioEventLoopGroup bossGroup, NioEventLoopGroup workerGroup,
                                EventExecutorGroup businessGroup, LongLinksMessageDataHandler messageDataHandler) {
        this.port = port;
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
        this.businessGroup = businessGroup;
        this.messageDataHandler = messageDataHandler;
    }

    /**
     * @PostConstruct
     * 该注解被用来修饰一个非静态的void（）方法。 被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，
     * 并且只会被服务器执行一次。PostConstruct在构造函数之后执行，init（）方法之前执行
     */

    @Override
    public void run() {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChildChannelHandler(businessGroup, messageDataHandler))
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 1024)//服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                    .childOption(ChannelOption.TCP_NODELAY, true)//立即写出
                    .childOption(ChannelOption.SO_KEEPALIVE, true);//内存泄漏检测 开发推荐PARANOID 线上SIMPLE
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.SIMPLE);//内存泄漏检测 开发推荐PARANOID 线上SIMPLE
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            if (channelFuture.isSuccess()) {
                logger.info("Netty 长连接 服务启动完毕,port={}", this.port);
            }
            // 等到服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅释放线程资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }


    /**
     * 网络事件处理器
     */
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        private EventExecutorGroup businessGroup;

        private LongLinksMessageDataHandler messageDataHandler;

        public ChildChannelHandler(EventExecutorGroup businessGroup, LongLinksMessageDataHandler messageDataHandler) {
            this.businessGroup = businessGroup;
            this.messageDataHandler = messageDataHandler;
        }

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            // 添加自定义协议的编解码工具
            /** 编码工具 */
            ch.pipeline().addLast(new ModelMessageToByteEncoder());
            /** 解析报文 */
            ch.pipeline().addLast(new ByteToModelDecoder());
            //涉及到数据库操作，所以放入businessGroup
            ch.pipeline().addLast(businessGroup, messageDataHandler);
        }
    }

}
