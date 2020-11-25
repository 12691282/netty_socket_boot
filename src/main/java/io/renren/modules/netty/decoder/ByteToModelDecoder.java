package io.renren.modules.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import io.renren.common.utils.NumberUtil;
import io.renren.modules.netty.model.RequestModel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ByteToModelDecoder extends ByteToMessageDecoder {
/**
 * <pre>
 * 协议开始
 * 起始符，占据5个字节.
 * 业务内容长度，占据4个字节.
 * </pre>
 */

    /**
     * 占据7个字节
     */
    public final int START_LENGTH = 7;

    /**
     *
     *  起始符占据4个字节
     */
    public final int BUSINESS_CONTENT_LENGTH = 4;
    /**
     * 数据起始 大小
     */
    public final int DATA_MIN_LENGTH = 1024;



    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer,
                          List<Object> out) {

        // 可读长度必须大于基本长度
        if (buffer.readableBytes() >= START_LENGTH) {

            // 数据包的长度，又变得不满足
            // 此时，应该结束。等待后面的数据到达
//            if (buffer.readableBytes() < START_LENGTH) {
//                log.info("package data size {}", buffer.readableBytes());
//                return;
//            }

            //读取协议长度的字段大小
            //开始头标示 字节
            byte[] headByte = new byte[START_LENGTH];
            buffer.readBytes(headByte);
            String headData = new String(headByte, CharsetUtil.UTF_8);
            log.info("head code {}", headData);
            log.info("data package  size {}", buffer.readableBytes());

            //读取业务长度字节数组
            byte[] businessContentLengthByte = new byte[BUSINESS_CONTENT_LENGTH];
            buffer.readBytes(businessContentLengthByte);
            Integer contentLength = NumberUtil.byteArrayToInt(businessContentLengthByte);

            /**
             * 数据 大于 默认容量时 重新对取
             */
            if (contentLength > DATA_MIN_LENGTH) {
                buffer.resetReaderIndex();
                return;
            }

            log.info("content length {}", contentLength);
            log.info("data package  size {}", buffer.readableBytes());

            byte[] contentByte = new byte[contentLength];
            buffer.readBytes(contentByte);

            RequestModel requestModel = new RequestModel();
            requestModel.setHeadData(headData);
            requestModel.setContentData(new String(contentByte, CharsetUtil.UTF_8));

            log.info(" message code {}", requestModel);
            out.add(requestModel);

        }
    }
}
