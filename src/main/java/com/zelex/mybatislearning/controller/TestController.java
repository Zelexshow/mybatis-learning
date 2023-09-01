package com.zelex.mybatislearning.controller;

import com.zelex.mybatislearning.mapper.UserInfoDOMapper;
import com.zelex.mybatislearning.po.UserInfoDO;
import com.zelex.mybatislearning.typehandler.model.EncryptItem;
import com.zelex.mybatislearning.typehandler.model.EncryptItems;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController("/testMybatis")
public class TestController {

    @Resource
    private UserInfoDOMapper userInfoDOMapper;

    @GetMapping("/insert")
    public String insertUserDO(@RequestParam("name") String name) {
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setName(name);
        userInfoDO.setPhone("123333333");
        userInfoDO.setIdNumber("22223333333");
        userInfoDO.setIsDelete(false);
        Date now = new Date();
        userInfoDO.setGmtCreateTime(now);
        userInfoDO.setGmtModTime(now);
        userInfoDOMapper.insert(userInfoDO);
        return "成功";
    }

    @GetMapping("/insertWithTypeHandler")
    public String insertWithTypeHandler(@RequestParam("name") String name, @RequestParam("phone") String phone) {
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setName(name);
        userInfoDO.setPhone(phone);
        userInfoDO.setIdNumber("22223333333");
        userInfoDO.setIsDelete(false);
        Date now = new Date();
        userInfoDO.setGmtCreateTime(now);
        userInfoDO.setGmtModTime(now);
        userInfoDOMapper.insertWithTypeHandler(phone, name);
        return "成功";
    }

    @GetMapping("/getWithDec")
    public UserInfoDO getWithDec(@RequestParam("id") Long id) {
        UserInfoDO userInfoDO = userInfoDOMapper.selectByIdWithDec(id);
        return userInfoDO;
    }


    @GetMapping("/insertWithTypeHandler2")
    public String insertWithTypeHandler2(@RequestParam("name") String name, @RequestParam("phone") String phone) {
        EncryptItems encryptItems = new EncryptItems(phone, name);
        userInfoDOMapper.insertWithTypeHandler2(encryptItems);
        return "成功";
    }

    @GetMapping("/get")
    public UserInfoDO getById(@RequestParam("id") Long id) {
        UserInfoDO userInfoDO = userInfoDOMapper.selectById(id);
        return userInfoDO;
    }




}
