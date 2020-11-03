package io.renren.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.renren.common.utils.NumberUtil;
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
            out.writeBytes(in.getHeadMessage().getBytes("UTF-8"));
            byte[] bodyBytes = in.getBodyData().getBytes("UTF-8");
            //内容长度字节
            int lengthByte = bodyBytes.length;
            out.writeBytes(NumberUtil.intToByte(lengthByte));
            out.writeBytes(bodyBytes);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }



}
