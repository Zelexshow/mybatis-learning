package com.zelex.mybatislearning.util;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

public class EnDecrUtil {

    // 注意:保持16位
    private static final String DEFAULT_KEYS = "123456789asdfghj";

    /**
     * 加密
     * @param sourceStr 明文
     * @param key 密钥
     * @return 密文
     */
    public static String encrypt(String sourceStr, String key) {
        if (sourceStr == null) {
            return null;
        }
        AES aes;
        if (key == null || key.length() == 0) {
            aes = SecureUtil.aes(DEFAULT_KEYS.getBytes(StandardCharsets.UTF_8));
        } else {
            aes = SecureUtil.aes(key.getBytes(StandardCharsets.UTF_8));
        }

        return aes.encryptHex(sourceStr);
    }

    /**
     *
     * @param encryptStr 密文
     * @param key 密钥
     * @return 明文
     */
    public static String decrypt(String encryptStr, String key) {
        if (null == encryptStr) {
            return null;
        }
        if (key == null || key.length() == 0) {
            return SecureUtil.aes(DEFAULT_KEYS.getBytes(StandardCharsets.UTF_8)).decryptStr(encryptStr);
        }
        return SecureUtil.aes(key.getBytes(StandardCharsets.UTF_8)).decryptStr(encryptStr);
    }

    public static void main(String[] args) {
        String content = "121212";
        String encrypt = encrypt(content, null);
        System.out.println(decrypt(encrypt, null));
    }
}
