package io.renren.modules.iosocket.utils;

import io.renren.modules.iosocket.model.Response;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *  @author 李强
 */

@Data
@Slf4j
public class IotSocket {

    private Socket socket;

    /**
     *  失败重试次数
     */
    private  Integer reconnectionAttempts;




    public void onEvent(String event, Emitter.Listener fn){
        socket.on(event, fn);
    }

    public void connect() {
        socket.connect();
    }

    public boolean connected() {
      return socket.connected();
    }

    public void emit(String gsConfigurationDataEvent, String... generate) {
        socket.emit(gsConfigurationDataEvent, generate);
    }

    public Socket disconnect() {
        socket.disconnect();
        return socket;
    }


}
