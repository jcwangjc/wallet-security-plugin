package com.plugin.wallet.security.ca;

import java.security.NoSuchAlgorithmException;

/**
 * @author : laoa
 * @describe : hash接口
 * @email : laoa@markcoin.net
 */
public interface HashApi {
    public String digest(String data) throws NoSuchAlgorithmException;
}
