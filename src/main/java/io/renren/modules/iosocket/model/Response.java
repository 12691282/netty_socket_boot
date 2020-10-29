package io.renren.modules.iosocket.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


/**
 *  @Author 李强
 *  *@reviewer 审核人
 */

@Data
public class Response {

  /**
   * 返回消息对象
   */
  private JSONObject message;
  /**
   * 请求是否成功
   */
  private Boolean success;
  /**
   * 动态令牌
   */
  private String hmac;
  /**
   * 错误代码
   */
  private String errorCode;
  /**
   * 错误说明
   */
  private String errorMessage;
  /**
   * 处理时间
   */
  private String time;

}
