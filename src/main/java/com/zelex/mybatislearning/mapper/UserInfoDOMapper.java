package com.zelex.mybatislearning.mapper;

import com.zelex.mybatislearning.po.UserInfoDO;
import com.zelex.mybatislearning.typehandler.model.EncryptItems;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserInfoDOMapper {
    void insertWithTypeHandler(@Param("phone")String phone, @Param("name") String name);


    UserInfoDO selectByIdWithDec(Long id);


    void insertWithTypeHandler2(@Param("encs")EncryptItems encs);



    void insert(UserInfoDO userInfoDO);

    UserInfoDO selectById(Long id);

}
