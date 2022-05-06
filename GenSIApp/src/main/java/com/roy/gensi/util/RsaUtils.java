package com.roy.gensi.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/5
 * @description:
 **/

public class RsaUtils {
    public static final Logger LOGGER = Logger.getLogger(RsaUtils.class);
    public static final String KEY_ALGORITHM = "RSA";
    /** 貌似默认是RSA/NONE/PKCS1Padding，未验证 */
    public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
    /** RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024 */
    public static final int KEY_SIZE = 2048;

    /**
     * 生成一对Rsa密钥对
     *
     * @return json对象字符串，publicKey对应的值为公钥，privateKey对应的值为私钥
     */
    public static JSONObject generateKeyPairForJava() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            JSONObject keyPairJson = new JSONObject();
            keyPairJson.put("publicKey", Base64.encodeBase64String(publicKey.getEncoded()));
            keyPairJson.put("privateKey", Base64.encodeBase64String(privateKey.getEncoded()));
            return keyPairJson;
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("生成rsa密钥对异常:" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 还原公钥，X509EncodedKeySpec 用于构建公钥的规范，转换后使java jdk能够正常加解密
     *
     * @param keyBytes
     * @return
     */
    private static PublicKey restorePublicKeyForJava(byte[] keyBytes) {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.error("还原公钥异常:" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 还原私钥，PKCS8EncodedKeySpec 用于构建私钥的规范，转换后使java jdk能够正常加解密
     *
     * @param keyBytes
     * @return
     */
    private static PrivateKey restorePrivateKeyForJava(byte[] keyBytes) {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.error("还原私钥异常:" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 通用加密
     *
     * @param key
     * @param plainText
     * @return
     */
    private static byte[] RSAEncode(Key key, byte[] plainText) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(plainText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            LOGGER.error("加密异常:" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 通用解密
     *
     * @param key
     * @param encodedText
     * @return
     */
    private static String RSADecode(Key key, byte[] encodedText) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(encodedText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            LOGGER.error("解密异常:" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 利用公钥进行加密
     *
     * @param publicKey
     * @param plainText
     *            要进行加密的原始字符串
     * @return
     */
    public static String RSAEncodeByPublicKey(String publicKey, String plainText) {
        try {
            PublicKey publicKeyObj = restorePublicKeyForJava(Base64.decodeBase64(publicKey));
            return Base64.encodeBase64String(RSAEncode(publicKeyObj, plainText.getBytes("utf-8")));
        } catch (Exception e) {
            LOGGER.error("加密异常:" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 利用私钥进行加密
     *
     * @param privateKey
     * @param plainText
     *            要进行加密的原始字符串
     * @return
     */
    public static String RSAEncodeByPrivateKey(String privateKey, String plainText) {
        try {
            PrivateKey privateKeyObj = restorePrivateKeyForJava(Base64.decodeBase64(privateKey));
            return Base64.encodeBase64String(RSAEncode(privateKeyObj, plainText.getBytes("utf-8")));
        } catch (Exception e) {
            LOGGER.error("加密异常:" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 利用私钥进行解密
     *
     * @param privateKey
     * @param encodedText
     *            RSAEncode加密后所得到的字符串
     * @return
     */
    public static String RSADecodeByPrivateKey(String privateKey, String encodedText) {
        try {
            PrivateKey privateKeyObj = restorePrivateKeyForJava(Base64.decodeBase64(privateKey));
            return RSADecode(privateKeyObj, Base64.decodeBase64(encodedText));
        } catch (Exception e) {
            LOGGER.error("解密异常:" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 利用公钥进行解密
     *
     * @param publicKey
     * @param encodedText
     *            RSAEncode加密后所得到的字符串
     * @return
     */
    public static String RSADecodeByPublicKey(String publicKey, String encodedText) {
        try {
            PublicKey publicKeyObj = restorePublicKeyForJava(Base64.decodeBase64(publicKey));
            return RSADecode(publicKeyObj, Base64.decodeBase64(encodedText));
        } catch (Exception e) {
            LOGGER.error("解密异常:" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 测试, 使用Demo
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            // 生成密钥对
            // String keyPairStr = generateKeyPairForJava();
            // 转换为json对象
            JSONObject keyPairJson = generateKeyPairForJava();
            // 获取公钥
            String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjQTWfGXgF4sxr0PAXa7j8GlgzoReplMTBIcFQYyFubCFDJFQMAMnCuk0Y5DOVcabSdqcO6UkjLROWv+F5xnAy5PReQ19a0ZM+z/j8qen4zuwvDDg0XuLONy7nKnV7O6QQ+EeiZX0IvmHJDnPQd89HKi5qn8rrHT25WKPuWaoXLI1gWiGx3MJxSMyXvqm9ToNoWVYGYgkDxtiX80yWx6+hGOa+d6hfq6oj6JtUIT9tTgitcKNBB36ut0KgSJraySPXhLzbdc0H4b3bwaOZEHERPUntsOTIQKqD4K7Y409LwSAkZL7OA02tWFV2dwTqzgN1Q3IT83qS9lJREQJHaaLmwIDAQAB";
            // String publicKey = keyPairJson.getString("publicKey");
            // System.out.println("publicKey为：" + publicKey);
            // 获取私钥
            String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCNBNZ8ZeAXizGvQ8BdruPwaWDOhF6mUxMEhwVBjIW5sIUMkVAwAycK6TRjkM5VxptJ2pw7pSSMtE5a/4XnGcDLk9F5DX1rRkz7P+Pyp6fjO7C8MODRe4s43LucqdXs7pBD4R6JlfQi+YckOc9B3z0cqLmqfyusdPblYo+5ZqhcsjWBaIbHcwnFIzJe+qb1Og2hZVgZiCQPG2JfzTJbHr6EY5r53qF+rqiPom1QhP21OCK1wo0EHfq63QqBImtrJI9eEvNt1zQfhvdvBo5kQcRE9Se2w5MhAqoPgrtjjT0vBICRkvs4DTa1YVXZ3BOrOA3VDchPzepL2UlERAkdpoubAgMBAAECggEBAIHAms25OxjOpXebQkuw2OBjRYSfvSsSsyPuTELE1mJF7hXjOsIClJDwhJi8K243ILkyoaAr0lZ4rMQD6qP6SS/HoezdIbJr6/ceEVI7OznUuUhfPOzi3PYD+8gpaMHpu+oJH/z+cbUgnV0SqAX3LxbIlJUxD/FNacMkDhRMRcuLfA5SH4QkG8rKj8VcKRBbxWwJckXB0pGT7ORtw+uwZh9oWWTwGFNsa9FYjF6kjv+IzEcXJvUl2oJcDnEnewy0ZCG1togoG3Iuk2vBbMaNsedATJ24SFQkMGoFk9uCnmS/xC9DNEt3Udl+lWYKGqOhwojGtfMzvDlxF4/4FxYyRXkCgYEAwAuwWuuF3F2XHQGoduAA1CoYuPZygmPhzsgOR5f9LE3wpxsdufcS5+hjKbKc09XMYa+XOhW4YYoCIcS1Jt6HViVH6Up28yOqkWiNOLDVMyV+MUJ65SyeMeUTsEkweaLyL7TtzjAB8ynnihNEmJSYauBEdbPRas+hAPDxsT3sAc8CgYEAu/sBmqqVil2h/LdcgsFI6GWy1PI8pA2UW4XNUwfjRWovcqX7fUhs97IMzyLFL/+X2w6jDTiTtorb2SM90y0INAmu0/5JJeJo7AQnA8py7+9Z377kd6VmouyZmXZqu0V4atJ8AZXxXrhgj93gHKjlZU+oUgLXGC+oU4dbzlKgyHUCgYAJ009Fh5FEsgEeOcx9sH46XKEBLZK0Oxn9c1zPquYIE0DBu/BsaEiuVsRkCMBfUivvv4UUMrJJQz9SrPa7s1znOmcQd3/YdrWuVNQM13dA86UeLhGNkhfR4697MHNrdbvAVxhnmSswXq967/ttdwTJSwoVn4qSVWceXjVvZQSjHwKBgQCpikV0HY4p8TD8INRLCdCb6b1U2fVfcO/xa8g3rtGH9bE9kf40jHHbJLbwysdV9wwbAiSE9BYleS+3s1U7dU4EwQbxJFMM3BsNYuFdReZMCG/ioOSmFINxRjtycZ7psEpe7d/x/i5n45sTpmVO8/WRXjrNXMpqgfErBNYES/m/UQKBgCYgbglZwGHQfRriTXKqdTnih9OH5t/jpCEII7bTmQ26C9D8Hmm1/slI4feKHxcQVPovmPrXOSo+uW45oGLDF3VgLGF4TCDejgokiQqyuiqGNe2IoNRrB3yijn8XMEV9NdBnxplrqNWRajJ/+ATTIP6fShxDJ0aUpmluU3wdXfNw";
            // String privateKey = keyPairJson.getString("privateKey");
            // System.out.println("privateKey为：" + privateKey);
            // 要加密的字符串
            String plainText = "62579031024A4218C7C32EF958E50689";
            System.out.println("plainText加密前为：" + plainText);

            // // 使用公钥进行加密
            // String encodedText = RSAEncodeByPublicKey(publicKey, plainText);
            // System.out.println("plainText加密后为：" + encodedText);
            // // 使用私钥进行解密
            // String decodeText = RSADecodeByPrivateKey(privateKey,
            // encodedText);

            // 使用私钥进行加密
            String encodedText = RSAEncodeByPublicKey(publicKey,plainText);
//			String encodedText = RSAUtil.encryptByPublicKey(plainText, publicKey);

            System.out.println("plainText加密后为：" + encodedText);
            // 使用公钥进行解密
            String decodeText = RSADecodeByPrivateKey(privateKey, encodedText);
//			String decodeText = RSAUtil.decryptByPrivateKey(encodedText, privateKey);

            System.out.println("plainText解密后为：" + decodeText);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
