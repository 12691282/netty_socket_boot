package io.renren.modules.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;
import io.renren.modules.netty.decoder.ByteToModelDecoder;
import io.renren.modules.netty.handler.ShortLinksMessageDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShortLinksChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Value("${netty.read-timeout}")
    private int readTimeOut;

    @Autowired
    @Qualifier("businessGroup")//开了50个线程来确保并发性
    private EventExecutorGroup businessGroup;

    //单例注入
    @Autowired
    private ShortLinksMessageDataHandler shortLinksMessageDataHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 这里将FixedLengthFrameDecoder添加到pipeline中，指定长度为1024*10
        /** 解析报文 */
        socketChannel.pipeline().addLast(new ByteToModelDecoder());
        pipeline.addLast(businessGroup, shortLinksMessageDataHandler);//涉及到数据库操作，所以放入businessGroup

    }
}
