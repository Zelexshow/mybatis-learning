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

    /**
     * 更新电话名
     * @param id
     * @param name
     * @param phone
     */
    void updateById(Long id,
                    String name,
                    @Param("phoneParam")
                    @SensitiveField(paramName = "phoneParam")
                    String phone);

}
