package io.renren.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;

/**
 * 编码器
 * Create by 2020-09-11
 */
@Slf4j
public class ObjEncoder extends MessageToByteEncoder<DataPackageObj> {

    @Override
    protected void encode(ChannelHandlerContext ctx, DataPackageObj in, ByteBuf out) {
        try {
            log.info(" send data {}", in);
            out.writeBytes("201019".getBytes("UTF-8"));
            out.writeBytes(in.getHeadMessage().getBytes("UTF-8"));
            out.writeBytes(in.getLengthByte());
            out.writeBytes(in.getBodyData().getBytes("UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }



}
