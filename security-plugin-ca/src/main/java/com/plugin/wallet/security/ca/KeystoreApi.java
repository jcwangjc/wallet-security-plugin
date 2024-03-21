package com.plugin.wallet.security.ca;


import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/**
 * @author : laoa
 * @describe : ca认证接口
 * @email : laoa@markcoin.net
 */
public interface KeystoreApi {
    public String sign(String message) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, IOException;
    public boolean signed(String message, String sign) throws KeyStoreException, IOException;
}
