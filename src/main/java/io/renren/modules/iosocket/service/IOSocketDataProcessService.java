package io.renren.modules.iosocket.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 李强
 */
public interface IOSocketDataProcessService {

    /**
     * 处理数据
     * @param json
     */
    void doDataProcess(JSONObject json);


}
