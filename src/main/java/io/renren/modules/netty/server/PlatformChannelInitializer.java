package io.renren.modules.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;
import io.renren.modules.netty.handler.PlatformMessageDataHandler;
import io.renren.modules.netty.server.decoder.ByteToStringDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PlatformChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    @Qualifier("businessGroup")//开了50个线程来确保并发性
    private EventExecutorGroup businessGroup;

    //单例注入
    @Autowired
    private PlatformMessageDataHandler messageDataHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 这里将FixedLengthFrameDecoder添加到pipeline中，指定长度为1024*10
//        pipeline.addLast(new LineBasedFrameDecoder(1024*10));//字节解码器 ,其中2048是规定一行数据最大的字节数。  用于解决拆包问题
        /** 解析报文 */
        socketChannel.pipeline().addLast(new ByteToStringDecoder());
        //涉及到数据库操作，所以放入businessGroup
        socketChannel.pipeline().addLast(businessGroup, messageDataHandler);

    }
}
