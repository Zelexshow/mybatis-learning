package com.zelex.mybatislearning.po;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class UserInfoDO {
    private Long id;
    private String name;
    private String phone;
    private String idNumber;

    private Boolean isDelete;
    private Date gmtCreateTime;
    private Date gmtModTime;
}


