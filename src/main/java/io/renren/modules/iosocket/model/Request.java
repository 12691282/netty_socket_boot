package io.renren.modules.iosocket.model;

import com.alibaba.fastjson.JSON;
import io.renren.modules.iosocket.utils.HmacSHA256;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @project 上海监控项目
 * @package io.renren.modules.websocket.model
 * @file Request.java 创建时间:2020/9/17 15:13
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
@Slf4j
@Builder
@Accessors(chain = true)
public class Request {

    /**
     * 请求消息
     */
    private Message message;

    /**
     * 动态令牌（根据message对象的base64编码）
     */
    private String hmac;

    /**
     * 系统当前时间
     */
    private String time;

    /**
     * 通信协议版本（注：1为当前版本）
     */
    private String version;

    /**
     * 加油站编码
     */
    private String gasStationNo;










}