package io.renren.modules.netty.model;

import lombok.Data;

/**
 * 接收的请求对象模型
 */

@Data
public class RequestModel {

    private String headData;

    private String contentData;

}
