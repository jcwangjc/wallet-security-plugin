package com.plugin.wallet.security.ca;

import com.plugin.wallet.security.ca.rsa.RSAUtils;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;

@Slf4j
public class LockExample {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        log.info(RSAUtils.getPublicKey());
        log.info(RSAUtils.getPrivateKey());

        String message="hello!my name is wang";

        String encrypt = RSAUtils.encrypt(message);
        log.info(encrypt);

        String decrypt = RSAUtils.decrypt(encrypt);
        System.out.println("decrypt = " + decrypt);
    }
}
