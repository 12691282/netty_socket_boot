package io.renren.modules.iosocket.model;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @project 上海监控项目
 * @package io.renren.modules.websocket.model
 * @file Message.java 创建时间:2020/9/17 15:16
 * @title TODO
 * @description TODO
 * @copyright Copyright (c) 2018 中石油天然气股份有限公司规划总院
 * @company 中石油天然气股份有限公司规划总院
 * @module 模块: TODO
 * @Author 李熠
 * @reviewer 审核人
 * @Version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 */
@Data
@Builder
@Accessors(chain = true)
@JSONType(orders={"messageId","busObject"})
public class Message {

    /**
     * 消息ID,32位UUID+年月日时分秒毫秒
     * 不能为空，采用base64编码
     */
    private String messageId;

    /**
     * 业务报文（数据需转化为base64编码）
     */
    private String busObject;
}