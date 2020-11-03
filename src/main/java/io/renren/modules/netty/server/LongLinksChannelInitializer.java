package io.renren.modules.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;
import io.renren.modules.netty.decoder.ByteToModelDecoder;
import io.renren.modules.netty.encoder.ModelMessageToByteEncoder;
import io.renren.modules.netty.handler.LongLinksMessageDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class LongLinksChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    @Qualifier("businessGroup")//开了50个线程来确保并发性
    private EventExecutorGroup businessGroup;

    //单例注入
    @Autowired
    private LongLinksMessageDataHandler messageDataHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        /** 编码工具 */
        socketChannel.pipeline().addLast(new ModelMessageToByteEncoder());
        /** 解析报文 */
        socketChannel.pipeline().addLast(new ByteToModelDecoder());
        //涉及到数据库操作，所以放入businessGroup
        socketChannel.pipeline().addLast(businessGroup, messageDataHandler);

    }
}
