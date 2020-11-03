package io.renren.modules.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.renren.modules.netty.model.ResponseModel;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author 李强
 */

@Slf4j
public class ModelMessageToByteEncoder extends MessageToByteEncoder<ResponseModel> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ResponseModel response,
                          ByteBuf byteBuf) {
        log.info(" 发送数据 : {} ", response);
        byteBuf.writeBytes(response.getHeadData().getBytes());
        byteBuf.writeBytes(response.getContentData().getBytes());

    }
}
