package io.renren.modules.netty.server.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import io.renren.config.Commont;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ByteToStringDecoder extends ByteToMessageDecoder {

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

    public final int BUSINESS_CODE_LENGTH = 1;


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

            //开始头标示 字节
            byte[] startByte = new byte[START_LENGTH];
            buffer.readBytes(startByte);
            log.info("head code {}", new String(startByte,CharsetUtil.UTF_8));
            //业务编码长度 字节
            byte[] businessCodeLengthByte = new byte[BUSINESS_CODE_LENGTH];
            buffer.readBytes(businessCodeLengthByte);
            if(businessCodeLengthByte.length == 0){
                ByteBuf headerBuf = Unpooled.buffer();
                headerBuf.writeBytes(Commont.RETURN_DEFEATED_CODE.getBytes(CharsetUtil.UTF_8));
                ctx.writeAndFlush(headerBuf);
                return;
            }

            String businessCodeStr = new String(businessCodeLengthByte,CharsetUtil.UTF_8);
            log.info("business length code {}", businessCodeStr);
            //业务编码转成整数
            Integer businessContentLength =  Integer.valueOf(businessCodeStr);

            byte[] businessContentByte = new byte[businessContentLength];
            buffer.readBytes(businessContentByte);



            byte[] dataByteLength = new byte[BUSINESS_BYTE_LENGTH];
            buffer.readBytes(dataByteLength);

            if(dataByteLength.length == 0){
                ByteBuf headerBuf = Unpooled.buffer();
                headerBuf.writeBytes(Commont.RETURN_DEFEATED_CODE.getBytes(CharsetUtil.UTF_8));
                ctx.writeAndFlush(headerBuf);
                return;
            }
            String businessContent = new String(businessContentByte,CharsetUtil.UTF_8);

            log.info("businessContent code {}", businessContent);

            Integer dataByteInt = byteArrayToInt(dataByteLength);

            log.info("dataByteInt size code {}", dataByteInt);

            byte[] dataByte = new byte[dataByteInt];
            buffer.readBytes(dataByte);
            String dataContent = new String(dataByte,CharsetUtil.UTF_8);
            String message = businessContent+dataContent;
            log.info(" message code {}", message);
            out.add(message);
        }


    }

    private int byteArrayToInt(byte[] bytes) {
        int value=0;
        for(int i = 0; i < BUSINESS_BYTE_LENGTH; i++) {
            int shift= (3-i) * 8;
            value +=(bytes[i] & 0xFF) << shift;
        }
        return value;
    }

}
