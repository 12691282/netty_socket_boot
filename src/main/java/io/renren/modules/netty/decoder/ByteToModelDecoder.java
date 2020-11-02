package io.renren.modules.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;
import io.renren.modules.netty.model.RequestModel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ByteToModelDecoder extends ByteToMessageDecoder {
/**
 * <pre>
 * 协议开始
 * 起始符，占据5个字节.
 * 业务编码长度，占据1个字节.
 * 业务报文字节长度 占据4个字节.
 * </pre>
 */

    /**
     *  起始符，占据5个字节
     */
    public final int START_LENGTH = 5;


    public final int BUSINESS_BYTE_LENGTH = 4;

    /**
     * 起始符 + 业务编码长度 + 业务编码 + 业务报文字节长度值的总字节长度为19。
     */
    public final int BASE_LENGTH = 19;



    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
                          List<Object> out) {

        // 可读长度必须大于基本长度
        if (buffer.readableBytes() >= BASE_LENGTH) {
            // 防止socket字节流攻击
            // 防止，客户端传来的数据过大
            // 因为，太大的数据，是不合理的
            if (buffer.readableBytes() > 2048) {
                buffer.skipBytes(buffer.readableBytes());
            }

            // 数据包的长度，又变得不满足
            // 此时，应该结束。等待后面的数据到达
            if (buffer.readableBytes() < BASE_LENGTH) {
                log.info("package data size {}", buffer.readableBytes());
                return;
            }

            //读取协议长度的字段大小
            //开始头标示 字节
            byte[] headByte = new byte[START_LENGTH];
            buffer.readBytes(headByte);
            String headData = new String(headByte, CharsetUtil.UTF_8);
            log.info("head code {}", headData);


            byte[] dataByte = new byte[BUSINESS_BYTE_LENGTH];
            buffer.readBytes(dataByte);
            String contentData = new String(dataByte, CharsetUtil.UTF_8);

            RequestModel requestModel = new RequestModel();
            requestModel.setHeadData(headData);
            requestModel.setContentData(contentData);

            log.info(" message code {}", requestModel);
            out.add(requestModel);

        }
    }
}
