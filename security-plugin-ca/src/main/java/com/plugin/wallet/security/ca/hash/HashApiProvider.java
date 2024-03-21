package com.plugin.wallet.security.ca.hash;

import com.plugin.wallet.security.ca.HashApi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author : laoa
 * @describe : hash接口实现
 * @email : laoa@markcoin.net
 */
public class HashApiProvider implements HashApi {
    @Override
    public String digest(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes());

            // Convert byte array to hexadecimal string
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte hashByte : hashBytes) {
                hexStringBuilder.append(String.format("%02x", hashByte));
            }

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
