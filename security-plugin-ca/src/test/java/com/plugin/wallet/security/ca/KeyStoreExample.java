package com.plugin.wallet.security.ca;

import com.plugin.wallet.security.ca.enums.SecurityLevel;
import com.plugin.wallet.security.ca.rsa.RSAUtils;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Scanner;

public class KeyStoreExample {

    public static void main(String[] args) throws Exception {
        test2();
    }

    //简单版本--明文
    public static void test() {
        try {
            SecurityApi securityApi = SecurityApi.builder()
                    .withKeyStoreLocation("wallet-ca.keystore")
                    .withKeyStorePassword("qwe@1234", SecurityLevel.A)
                    .withKeyStoreAlias("wallet-key")
                    .build();
            //内容
            String message="测试数字证书";
            //签名
            String sign= securityApi.keystore().sign(message);
            //验签
            boolean signed = securityApi.keystore().signed(message, sign);
            //结果
            System.out.println("signed = " + signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //普通版本--秘文
    public static void test2(){
        try {
            //加密公钥 MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiUx6GLqDoj2C0b6iRTRRFrl
            String password="ewhPZvn0cPkiVQbwaOH1ZamTOBQTye2ngiebI22Yx7lRjHULOSxEneQoflVoEBxdh4oBoltIoaunRF+ysFHDRddj6IRglHE+K6kTl5GinPzBbBd4rzdPD2XEqKOIqrCCozIKbunMejXboa+rxKJRzPelFzRcFgtZq3PdF8IzbcQTyHpm43O1/UkJMFzL+NLscdiTm1mPT1T5qq8Ee3g/bUPD5IVvvB4vj4r4/W1gKZ3d1t+4rHI3/ryAGUe5tSjYlBRj+cg8zhkhS0UlohfI/EkcA9pOZayvUMnqiw9nfm8OKUyLkw95JD0fZ1BsZCoIPq4u/KTuWNZSOxXBZS0lRg==";
            SecurityApi securityApi = SecurityApi.builder()
                    .withKeyStoreLocation("wallet-ca.keystore")
                    .withKeyStorePassword(password, SecurityLevel.B)
                    .withKeyStoreAlias("wallet-key")
                    .build();
            //内容
            String message="测试数字证书";
            //签名
            String sign= securityApi.keystore().sign(message);
            //验签
            boolean signed = securityApi.keystore().signed(message, sign);
            //结果
            System.out.println("signed = " + signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //安全版--授权
    //1.程序启动后，本地生成公钥和私钥
    //2.通过mq发送授权申请，参数包括IP地址、服务名称以及公钥
    //3.console收到mq的消息后，手动签发授权——即确认服务和IP名称以后，手动输入密码，密码通过公钥加密
    //4.最后将加密后的密码返回给申请的节点
    //5.各个程序节点收到console的ack以后，根据自动生成的私钥进行解密
    //总结，在这个过程中，密码的管理相对来说是非常安全，除非是内部人员通过写代码的方式来破解，不然很难获取到password
    public static void test3() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException {
        //先把password加密，公私钥都是系统启动的时候随机生成的。
        System.out.println("请输入keystore密码:");
        Scanner scan = new Scanner(System.in);
        String password = scan.nextLine();
        //用动态ras加密
        password=RSAUtils.encrypt(password);
        //构建API
        SecurityApi securityApi = SecurityApi.builder()
                .withKeyStoreLocation("wallet-ca.keystore")
                .withKeyStorePassword(password,SecurityLevel.C)
                .withKeyStoreAlias("wallet-key")
                .build();
        //内容
        String message="测试数字证书";
        //签名
        String sign= securityApi.keystore().sign(message);
        //验签
        boolean signed = securityApi.keystore().signed(message, sign);
        //结果
        System.out.println("signed = " + signed);
    }
}


