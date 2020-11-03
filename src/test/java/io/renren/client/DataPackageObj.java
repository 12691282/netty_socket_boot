package io.renren.client;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DataPackageObj {

    //文件头信息
    private String headMessage;
    //内容字符串
    private String bodyData;

}
