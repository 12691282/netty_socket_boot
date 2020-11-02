package io.renren.modules.netty.model;

import lombok.Data;

/**
 * 相应的对象模型
 */

@Data
public class ResponseModel {

    private String headData;

    private String contentData;

    public ResponseModel(RequestModel request) {
        this.headData = request.getHeadData();
        this.contentData = request.getContentData();
    }
}
