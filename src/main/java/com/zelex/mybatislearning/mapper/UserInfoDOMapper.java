package com.zelex.mybatislearning.mapper;

import com.zelex.mybatislearning.interceptor.anno.SensitiveField;
import com.zelex.mybatislearning.po.UserInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserInfoDOMapper {
    void insertWithTypeHandler(@Param("phone")String phone, @Param("name") String name);


    UserInfoDO selectByIdWithDec(Long id);





    void insert(UserInfoDO userInfoDO);

    UserInfoDO selectById(Long id);

    void updateById(Long id, String name, @Param("phone") @SensitiveField String phone);

}
