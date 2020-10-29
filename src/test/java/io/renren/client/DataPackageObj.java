package io.renren.client;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DataPackageObj {

    private String headMessage;
    private String bodyData;
    private byte[] lengthByte;

}
