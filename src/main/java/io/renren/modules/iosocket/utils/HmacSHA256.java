package io.renren.modules.iosocket.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @project 上海监控项目
 * @package io.renren.modules.websocket.utils
 * @file HmacSHA256.java 创建时间:2020/9/17 16:01
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
public class HmacSHA256 {

    /**
     * 加密
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String message, String key) throws Exception{
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        hmac.init(secret_key);
        byte[] array = hmac.doFinal(message.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toLowerCase();
    }
}