package com.scnu.zhou.signer.elss.util.encrypt;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Created by zhou on 16/9/5.
 *
 *  RSA 加密
 */
public class RSAEncryptUtil {

    /**
     * 用公钥publicKey对data数据进行加密
     * @param data
     * @param publicKey
     * @return
     */
    public static String encryptData(String data, String publicKey){

        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            PublicKey key = loadPublicKey(publicKey);
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 传入编码数据并返回编码结果
            return Base64Util.encode((cipher.doFinal(data.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {

        String[] result = publicKeyStr.split("-----");

        try {
            byte[] buffer = Base64Util.decode(result[2]);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }
}
