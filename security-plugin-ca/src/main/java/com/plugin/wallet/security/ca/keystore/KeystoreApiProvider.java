package com.plugin.wallet.security.ca.keystore;

import com.plugin.wallet.security.ca.KeystoreApi;
import com.plugin.wallet.security.ca.content.SecurityContent;
import com.plugin.wallet.security.ca.content.SecuritySecureManagement;
import com.plugin.wallet.security.ca.hex.HexUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;

/**
 * @author : laoa
 * @describe : 签名
 * @email : laoa@markcoin.net
 */
public final class KeystoreApiProvider implements KeystoreApi {

    private KeyStore keyStore;

    public KeystoreApiProvider(String fileLocation, String password, String alias) {
        SecuritySecureManagement.setCertificatePassword(SecurityContent.KEYSTORE_FILE_LOCATION,fileLocation);
        SecuritySecureManagement.setCertificatePassword(SecurityContent.KEYSTORE_PASSWORD,password);
        SecuritySecureManagement.setCertificatePassword(SecurityContent.KEYSTORE_ALIAS,alias);
    }

    /**
     * 获取keystore
     * @return keystore
     */
    private KeyStore getKeyStore() throws IOException {
        if(keyStore!=null){
            return keyStore;
        }
        String fileLocation=SecuritySecureManagement.getCertificatePassword(SecurityContent.KEYSTORE_FILE_LOCATION);
        String password=SecuritySecureManagement.getCertificatePassword(SecurityContent.KEYSTORE_PASSWORD);
        if (!StringUtils.isEmpty(fileLocation) && !StringUtils.isEmpty(password)) {
            // 使用 ClassLoader 获取资源文件的输入流
            InputStream inputStream=null;
            try (InputStream is = KeystoreApiProvider.class.getClassLoader().getResourceAsStream(fileLocation)) {
                inputStream=is;
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(is, password.toCharArray());
                this.keyStore=keyStore;
                return keyStore;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                inputStream.close();
            }
        }
        throw new RuntimeException("fileLocation or password is null");
    }

    /**
     * 签名
     * @param message     消息
     * @return 签名
     */
    @Override
    public String sign(String message) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String password=SecuritySecureManagement.getCertificatePassword(SecurityContent.KEYSTORE_PASSWORD);
        String alias=SecuritySecureManagement.getCertificatePassword(SecurityContent.KEYSTORE_ALIAS);
        //获取私钥
        PrivateKey privateKey = (PrivateKey) getKeyStore().getKey(alias, password.toCharArray());
        //获取证书
        X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
        if (!StringUtils.isEmpty(message) && null != privateKey && null != certificate) {
            try {
                //根据算法签名对象
                java.security.Signature signature = java.security.Signature.getInstance(certificate.getSigAlgName());
                //初始化密钥
                signature.initSign(privateKey);
                //注入待签名的消息
                signature.update(message.getBytes(StandardCharsets.UTF_8));
                //获取签名
                byte[] signBytes = signature.sign();
                //转十六进制字符串
                return HexUtils.toHexString(signBytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 验签
     *
     * @param message     消息
     * @param sign        签名
     * @return 验签结果
     */
    @Override
    public boolean signed(String message, String sign) throws KeyStoreException, IOException {
        String alias=SecuritySecureManagement.getCertificatePassword(SecurityContent.KEYSTORE_ALIAS);
        //获取证书
        X509Certificate certificate = (X509Certificate) getKeyStore().getCertificate(alias);
        if (!StringUtils.isEmpty(message) && !StringUtils.isEmpty(sign) && null != certificate) {
            try {
                //根据算法获取签名对象
                java.security.Signature signature = java.security.Signature.getInstance(certificate.getSigAlgName());
                //初始化密钥
                signature.initVerify(certificate.getPublicKey());
                //注入待验证的消息
                signature.update(message.getBytes(StandardCharsets.UTF_8));
                //验证签名
                return signature.verify(HexUtils.fromHexString(sign));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
