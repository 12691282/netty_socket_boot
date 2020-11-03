package io.renren.modules.netty.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("accept_data_model")
public class AcceptDataEntity {

    @TableId(value = "id")
    private int id;

    @TableField("business_code")
    private String businessCode;

    @TableField("business_content")
    private String businessContent;

}
