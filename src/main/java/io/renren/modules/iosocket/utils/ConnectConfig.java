package io.renren.modules.iosocket.utils;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


import java.net.URISyntaxException;

/**
 * 链接配置
 * @author 李强
 */

@Configuration
public class ConnectConfig {

    /**
     * iosocket 地址
     */
    @Value("${iot-socket.server-url}")
    private String serverUrl;

    /**
     * 失败重试次数
     */
    @Value("${iot-socket.reconnectionAttempts}")
    private Integer reconnectionAttempts;

    /**
     * 失败重连的时间间隔
     */
    @Value("${iot-socket.reconnectionDelay}")
    private Integer reconnectionDelay;

    /**
     * 连接超时时间
     */
    @Value("${iot-socket.timeout}")
    private Integer timeout;

    @Bean
    public RestTemplate restTemplateBean(){
        return new RestTemplate();
    }


    @Bean
    public IotSocket SocketBean(){
        IotSocket iotsocket = new IotSocket();
        IO.Options options = new IO.Options();
        //失败重试次数
        options.reconnectionAttempts = reconnectionAttempts;
        //失败重连的时间间隔
        options.reconnectionDelay = reconnectionDelay;
        //连接超时时间(ms)
        options.timeout = timeout;
        try {
            Socket socket =  IO.socket(serverUrl, options);
            iotsocket.setSocket(socket);
            iotsocket.setReconnectionAttempts(reconnectionAttempts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return iotsocket;
    }

}
