
package com.elphin.framework.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5处理类
 */
public class Md5 {
    public String str;

    /**
     * 对字符串进行md5加密
     * @param plainText
     *      要加密的字符串
     * @return
     *      加密后的密文
     */
    public static String md5s(String plainText) {

        String str = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            str = buf.toString();
            // ==================("result: " + buf.toString());// 32位加密
            // ==================("result: " + buf.toString().substring(8,
            // 24));// 16位加密

            str = buf.toString();
        } catch (NoSuchAlgorithmException e) {
        }

        return str;
    }

}
