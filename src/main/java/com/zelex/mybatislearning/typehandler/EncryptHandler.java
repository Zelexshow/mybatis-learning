//package com.zelex.mybatislearning.typehandler;
//
//import com.zelex.mybatislearning.typehandler.model.EncryptItem;
//import com.zelex.mybatislearning.util.EnDecrUtil;
//import org.apache.ibatis.type.BaseTypeHandler;
//import org.apache.ibatis.type.JdbcType;
//import org.apache.ibatis.type.MappedJdbcTypes;
//import org.apache.ibatis.type.MappedTypes;
//
//import java.nio.charset.StandardCharsets;
//import java.sql.CallableStatement;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//@MappedJdbcTypes(JdbcType.VARCHAR)
//public class EncryptHandler extends BaseTypeHandler<EncryptItem> {
//
//    // encrypt key
//    private static final byte[] KEYS = "12345678abcdefgh".getBytes(StandardCharsets.UTF_8);
//    // 入库前进行操作
//    @Override
//    public void setNonNullParameter(PreparedStatement ps, int i, EncryptItem encryptItem, JdbcType jdbcType) throws SQLException {
//
//        // 使用默认工具中默认key进行加密
//        String encrypt = EnDecrUtil.encrypt(encryptItem.getValue(), null);
//        ps.setString(i, encrypt);
//    }
//
//    // 在返回时获取结果,直接通过str的值
//    @Override
//    public EncryptItem getNullableResult(ResultSet resultSet, String s) throws SQLException {
//        return new EncryptItem(EnDecrUtil.decrypt(s, null));
//    }
//
//    // 在返回时获取结果,直接通过字段列索引
//    @Override
//    public EncryptItem getNullableResult(ResultSet resultSet, int i) throws SQLException {
//        return new EncryptItem(EnDecrUtil.decrypt(resultSet.getString(i), null));
//    }
//
//    @Override
//    public EncryptItem getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
//        return new EncryptItem(EnDecrUtil.decrypt(callableStatement.getString(i), null));
//
//    }
//}
