package com.zelex.mybatislearning.po;

import com.zelex.mybatislearning.interceptor.anno.SensitiveEntity;
import com.zelex.mybatislearning.interceptor.anno.SensitiveField;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@SensitiveEntity
@Data
@ToString
public class UserInfoDO {
    private Long id;
    private String name;
    // 加密字段
    @SensitiveField
    private String phone;
    private String idNumber;

    private Boolean isDelete;
    private Date gmtCreateTime;
    private Date gmtModTime;
}


