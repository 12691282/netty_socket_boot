package io.renren.config;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 声明为spring容器的配置，注入到spring中
 */
@Configuration
public class NettyLoopGroupConfig {

    @Value("${netty.threads.boss}")
    private int bossThreadsNum;

    @Value("${netty.threads.worker}")
    private int workerThreadsNum;

    @Value("${netty.threads.business}")
    private int businessThreadsNum;

    /**
     * 负责TCP连接建立操作 绝对不能阻塞
     * 1).理论上tcp长连接一个端口可以链接数无上限，但受到内存影响
     * 2).netty权威指南上说一个监听端口tcp连接的NioEventLoopGroup就可以处理成千上万的链接
     * @return
     */
    @Bean(name = "bossGroup")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossThreadsNum);
    }

    /**
     * 负责Socket读写操作 绝对不能阻塞
     * @return
     */
    @Bean(name = "workerGroup")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(workerThreadsNum);
    }

    /**
     * Handler中出现IO操作(如数据库操作，网络操作)使用这个
     * @return
     */
    @Bean(name = "businessGroup")
    public EventExecutorGroup businessGroup(){
        return new DefaultEventExecutorGroup(businessThreadsNum);
    }
}
