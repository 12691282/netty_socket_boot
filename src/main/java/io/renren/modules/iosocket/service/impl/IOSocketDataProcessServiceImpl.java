package io.renren.modules.iosocket.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.renren.modules.iosocket.model.Response;
import io.renren.modules.iosocket.service.IOSocketDataProcessService;
import io.renren.modules.iosocket.utils.Constants;
import io.renren.modules.iosocket.utils.IotSocket;
import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * @author 李强
 */

@Service
@Slf4j
public class IOSocketDataProcessServiceImpl implements IOSocketDataProcessService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${iot-socket.token-url}")
    private String tokenUrl;

    @Autowired
    private IotSocket socket;


    @PostConstruct
    private void initIOSocketEvent(){

        log.info("开始注册 io socket 事件。。。。。");
        //监听 加油站在线监测系统配置数据上传  事件
        socket.onEvent(Constants.GS_CONFIGURATION_DATA_EVENT, objects -> {

            String msg = objects[objects.length - 1].toString();
            log.info("事件 :{}",Constants.GS_CONFIGURATION_DATA_EVENT);
            log.info("收到数据:" + msg);
            Response response =  JSONObject.parseObject(msg, Response.class);

        });

        //监听 加油站环境数据上传  事件
        socket.onEvent(Constants.GS_ENVIRONMENT_DATA_EVENT, objects -> {
            String msg = objects[objects.length - 1].toString();
            log.info("事件 :{}",Constants.GS_ENVIRONMENT_DATA_EVENT);
            log.info("收到数据:{}", msg);

        });

        //监听 加油站油气回收数据上传 事件
        socket.onEvent(Constants.GS_RECYCLE_DATA_EVENT, objects ->{
            String msg = objects[objects.length - 1].toString();
            log.info("事件 :{}",Constants.GS_RECYCLE_DATA_EVENT);
            log.info("收到数据:{}", msg);
            Response response = JSONObject.parseObject(msg, Response.class);
            if(response.getSuccess()){
                String messageId = response.getMessage().getString("messageId");
                log.info("数据上传成功 messageId : {}", messageId);
            }else {
                log.info("数据上传失败 response : {}", response);
            }
        });

        //监听 加油站报警数据上传  事件
        socket.onEvent(Constants.GS_ALARM_DATA_EVENT, objects -> {
            String msg = objects[objects.length - 1].toString();
            log.info("事件 :{}",Constants.GS_ALARM_DATA_EVENT);
            log.info("收到数据:{}", msg);
            Response response = JSONObject.parseObject(msg, Response.class);
            if(response.getSuccess()){
                String messageId = response.getMessage().getString("messageId");
                log.info("数据上传成功 messageId : {}", messageId);
            }else {
                log.info("数据上传失败 response : {}", response);
            }

        });

        socket.onEvent(Socket.EVENT_CONNECT, objects -> log.info("socket服务器 连接成功") );
        socket.onEvent(Socket.EVENT_CONNECTING, objects -> log.info("socket服务器 连接中"));
        socket.onEvent(Socket.EVENT_CONNECT_TIMEOUT, objects -> log.info("socket服务器 连接超时"));
        socket.onEvent(Socket.EVENT_CONNECT_ERROR, objects -> log.info("socket服务器 失败"));
        socket.onEvent(Socket.EVENT_DISCONNECT, objects -> log.info("socket服务器 断开链接"));
        //todo 暂时连接
        log.info("开始连接socket服务器。。。。。");
//        socket.connect();
    }

    @Override
    public void doDataProcess(JSONObject originalDataJson) {
        log.info("开始处理数据。。。{}",originalDataJson);
        String token = this.getTokenAndRequestServer();
        if(token == null){
            return;
        }
        JSONObject busDataJson = this.getBusJsonObj(originalDataJson);
        if(busDataJson == null){
            return;
        }
        String jsonData = "";
        //除非时间上传数据
        socket.emit(Constants.GS_CONFIGURATION_DATA_EVENT, jsonData);
        log.info("触发上传事件:{}, 上传数据:{}",Constants.GS_CONFIGURATION_DATA_EVENT, jsonData);

    }



    private JSONArray getBusJsonArr(JSONObject json) {
        JSONObject messageJson = json.getJSONObject("message");
        if(messageJson == null){
            log.info("消息体为空");
            return null;
        }
        JSONArray busJsonArr = messageJson.getJSONArray("busObject");
        if(busJsonArr == null){
            log.info("业务对象为空");
            return null;
        }
        return busJsonArr;
    }


    private JSONObject getBusJsonObj(JSONObject json) {
        JSONObject messageJson = json.getJSONObject("message");
        if(messageJson == null){
            log.info("消息体为空");
            return null;
        }
        JSONObject busJson = messageJson.getJSONObject("busObject");
        if(busJson == null){
            log.info("业务对象为空");
            return null;
        }
        return busJson;
    }


    private String getTokenAndRequestServer() {
        log.info("请授权服务器请求token。。。。");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        JSONObject obj = new JSONObject();
        obj.put("companyCode", "844072586@qq.com");
        obj.put("username", "154512121");
        obj.put("password", "liqiang");
        HttpEntity<String> entity = new HttpEntity<>(obj.toJSONString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, entity , String.class );
        JSONObject resultJson  = JSONObject.parseObject(response.getBody());
        boolean isSuccess = resultJson.getBooleanValue("success");
        if(!isSuccess){
            log.info("token 请求失败  返回编码为 ： {}", resultJson.getString("errorCode"));
            return null;
        }
        return resultJson.getString("accessToken");
    }


}
