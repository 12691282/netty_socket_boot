package io.renren.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * 虫洞栈：https://bugstack.cn
 * 公众号：bugstack虫洞栈  ｛关注获取学习源码｝
 * Create by fuzhengwei on 2019
 */
@Slf4j
public class NettyClient implements Runnable {

    private static String host = "127.0.0.1";

    private static Integer post = 11001;

    private String message;


    public NettyClient(String msg){
        this.message = msg;
    }


    public void sendData(){

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        boolean boo = true;
        int num = 0;
        try {


            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.AUTO_READ, true);
            b.handler(new NettyClientChannelInitializer());
            ChannelFuture f = b.connect(host, post).sync();
//            f.channel().writeAndFlush(this.message);

            while (boo) {
                int numb = message.indexOf("{");

                String headMessage  = message.substring(0, numb);
                String bodyData  = message.substring(numb);
                int dataSize = bodyData.getBytes().length;
                byte[] lengthByte = intToByteArray(dataSize);
                num++;
                log.info("headMessage {} length {} bodyData {}", headMessage, bodyData.length(), bodyData);
                DataPackageObj obj = new DataPackageObj();
                obj.setHeadMessage(headMessage);
                obj.setBodyData(bodyData);
                obj.setLengthByte(lengthByte);
                f.channel().writeAndFlush(obj);
                try { //休眠一段时间
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //每一条线程向服务端发送的次数
                if (num >= 20) {
                    boo = false;
                    f.channel().closeFuture().sync();
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }

    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }


    @Override
    public void run() {
        this.sendData();
    }


}
