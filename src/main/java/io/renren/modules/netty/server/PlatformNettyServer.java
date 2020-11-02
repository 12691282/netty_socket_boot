package io.renren.modules.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.EventExecutorGroup;
import io.renren.modules.netty.decoder.ByteToModelDecoder;
import io.renren.modules.netty.handler.PlatformMessageDataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * 平台接收数据服务
 *
 * @author 李强
 * @date 2020-09-11
 */


@Component
public class PlatformNettyServer {

    private Logger logger = LoggerFactory.getLogger(PlatformNettyServer.class);

    @Value("${netty.platform-server-port}")
    private int port;

    @Autowired
    @Qualifier("bossGroup")
    private NioEventLoopGroup bossGroup;

    @Autowired
    @Qualifier("workerGroup")
    private NioEventLoopGroup workerGroup;

    @Autowired
    @Qualifier("businessGroup")
    private EventExecutorGroup businessGroup;

    @Autowired
    private PlatformChannelInitializer channelInitializer;


    /**
     * @PostConstruct
     * 该注解被用来修饰一个非静态的void（）方法。 被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，
     * 并且只会被服务器执行一次。PostConstruct在构造函数之后执行，init（）方法之前执行
     */

    /**
     * 启动server
     */
    @PostConstruct
    public void start() throws InterruptedException {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelInitializer)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 1024)//服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                    .childOption(ChannelOption.TCP_NODELAY, true)//立即写出
                    .childOption(ChannelOption.SO_KEEPALIVE, true);//内存泄漏检测 开发推荐PARANOID 线上SIMPLE
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.SIMPLE);//内存泄漏检测 开发推荐PARANOID 线上SIMPLE
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            if (channelFuture.isSuccess()) {
                logger.info("平台socket服务启动完毕,port={}", this.port);
            }
            // 等到服务端监听端口关闭
//            channelFuture.channel().closeFuture().sync();
        } finally {
            // 优雅释放线程资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }


    }



    /**
     * 网络事件处理器
     */
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Autowired
        @Qualifier("businessGroup")//开了50个线程来确保并发性
        private EventExecutorGroup businessGroup;

        //单例注入
        @Autowired
        private PlatformMessageDataHandler messageDataHandler;

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            // 添加自定义协议的编解码工具
            /** 解析报文 */
            ch.pipeline().addLast(new ByteToModelDecoder());
            //涉及到数据库操作，所以放入businessGroup
            ch.pipeline().addLast(businessGroup, messageDataHandler);
        }
    }



    /**
     * 被@PreDestroy修饰的方法会在服务器卸载Servlet的时候运行，
     * 并且只会被服务器调用一次，类似于Servlet的destroy()方法。
     */

    /**
     * 销毁资源
     */
    @PreDestroy
    public void destroy() {
        bossGroup.shutdownGracefully().syncUninterruptibly();
        workerGroup.shutdownGracefully().syncUninterruptibly();
        businessGroup.shutdownGracefully().syncUninterruptibly();
        logger.info("关闭成功");
    }
}
