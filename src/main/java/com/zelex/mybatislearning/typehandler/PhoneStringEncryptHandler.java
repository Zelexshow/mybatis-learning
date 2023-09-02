package com.zelex.mybatislearning.typehandler;


import com.zelex.mybatislearning.util.EnDecrUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhoneStringEncryptHandler extends BaseTypeHandler<String> {

    // 写操作时进行加密
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        // 使用默认工具中默认key进行加密
        String encrypt = EnDecrUtil.encrypt(parameter, null);
        ps.setString(i, encrypt);
    }

    //读操作时进行解密
    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return EnDecrUtil.decrypt(rs.getString(columnName), null);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return EnDecrUtil.decrypt(rs.getString(columnIndex), null);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return EnDecrUtil.decrypt(cs.getString(columnIndex), null);
    }
}
