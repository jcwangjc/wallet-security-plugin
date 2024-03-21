package com.plugin.wallet.security.ca.rsa;

import javax.crypto.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author : laoa
 * @describe : 加密解密
 * @email : laoa@markcoin.net
 */
public class RSAUtils {

    private static KeyPair keyPair = null;

    /**
     * 生成随机的公钥和私钥
     * @return
     */
    static {
        try{
            //根据算法获取密钥对构造器
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            //获取密钥对对象
            keyPair =  keyPairGenerator.generateKeyPair();
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    /**
     *获取私钥
     */
    public static String getPrivateKey(){
        //获取私钥
        PrivateKey privateKey = RSAUtils.keyPair.getPrivate();
        //字节数组转十六进制字符串
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    /**
     *获取公钥
     */
    public static String getPublicKey(){
        //获取公钥
        PublicKey publicKey = RSAUtils.keyPair.getPublic();
        //字节数组转十六进制字符串
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * 私钥反序列化
     * @param privateKeyHex
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey deserializePrivateKey(String privateKeyHex) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyHex);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
    }

    /**
     * 公钥反序列化
     * @param publicKeyHex
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PublicKey deserializePublicKey(String publicKeyHex) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyHex);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    }

    /**
     * 加密
     * @param message 明文
     * @return 密文（十六进制字符串）
     */
    public static String encrypt(String message) throws NoSuchAlgorithmException {
        String publicKeyHex = RSAUtils.getPublicKey();
        try{
            PublicKey publicKey = deserializePublicKey(publicKeyHex);
            //根据算法获取密码对象
            Cipher cipher = Cipher.getInstance("RSA");
            //初始化密钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            //加密
            byte[] encryptBytes = cipher.doFinal(message.getBytes());
            //转为十六进制字符串
            return Base64.getEncoder().encodeToString(encryptBytes);
        }catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException  | InvalidKeySpecException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     * @param message 明文
     * @return 密文（十六进制字符串）
     */
    public static String encrypt(String message,String publicKeyHex) throws NoSuchAlgorithmException {
        try{
            PublicKey publicKey = deserializePublicKey(publicKeyHex);
            //根据算法获取密码对象
            Cipher cipher = Cipher.getInstance("RSA");
            //初始化密钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            //加密
            byte[] encryptBytes = cipher.doFinal(message.getBytes());
            //转为十六进制字符串
            return Base64.getEncoder().encodeToString(encryptBytes);
        }catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException  | InvalidKeySpecException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param message 密文
     * @return 明文
     */
    public static String decrypt(String message){
        String privateKeyHex = RSAUtils.getPrivateKey();
        try{
            //获取私钥对象
            PrivateKey privateKey = deserializePrivateKey(privateKeyHex);
            //根据算法获取密码对象
            Cipher cipher = Cipher.getInstance("RSA");
            //初始化密钥
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            //解密
            byte[] encryptBytes = cipher.doFinal(Base64.getDecoder().decode(message));
            //转为字符串
            return new String(encryptBytes);
        }catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param message 密文
     * @return 明文
     */
    public static String decrypt(String message,String privateKeyHex){
        try{
            //获取私钥对象
            PrivateKey privateKey = deserializePrivateKey(privateKeyHex);
            //根据算法获取密码对象
            Cipher cipher = Cipher.getInstance("RSA");
            //初始化密钥
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            //解密
            byte[] encryptBytes = cipher.doFinal(Base64.getDecoder().decode(message));
            //转为字符串
            return new String(encryptBytes);
        }catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException e){
            e.printStackTrace();
        }
        return null;
    }
}
