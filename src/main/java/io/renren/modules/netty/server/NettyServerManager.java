package io.renren.modules.netty.server;


import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.renren.common.utils.BeanUtils;
import io.renren.modules.netty.handler.LongLinksMessageDataHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * netty 服务器管理
 *
 * @author 李强
 * @date 2020-09-11
 */
@Component
@Slf4j
public class NettyServerManager {

    @Value("${netty.longLinks-server-port}")
    private int longLinksPort;

    @Autowired
    @Qualifier("bossGroup")
    private NioEventLoopGroup bossGroup;

    @Autowired
    @Qualifier("workerGroup")
    private NioEventLoopGroup workerGroup;

    @Autowired
    @Qualifier("businessGroup")//开了50个线程来确保并发性
    private EventExecutorGroup businessGroup;

    //单例注入
    @Autowired
    private LongLinksMessageDataHandler messageDataHandler;

    /**
     * 线程池
     */
    @Autowired
    private ThreadPoolExecutor executor;

    /**
     * 启动server
     */
    @PostConstruct
    public void startServer() {
        log.info("启动 netty 线程池");
        executor.execute(
                new LongLinksNettyServer(longLinksPort, bossGroup, workerGroup, businessGroup, messageDataHandler)
        );
    }

}
