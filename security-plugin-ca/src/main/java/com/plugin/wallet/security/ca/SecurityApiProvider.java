package com.plugin.wallet.security.ca;

import com.plugin.wallet.security.ca.hash.HashApiProvider;
import com.plugin.wallet.security.ca.keystore.KeystoreApiProvider;
import org.jetbrains.annotations.NotNull;

/**
 * @author : laoa
 * @describe : 安全管理接口实现
 * @email : laoa@markcoin.net
 */
final class SecurityApiProvider implements SecurityApi{

    private KeystoreApi keystoreApi;
    private HashApi hashApi;

    SecurityApiProvider(String ca_path,String ca_password,String ca_alias){
        this.keystoreApi= new KeystoreApiProvider(ca_path,ca_password,ca_alias);
        this.hashApi=new HashApiProvider();
    }

    @Override
    public @NotNull KeystoreApi keystore() {
        return keystoreApi;
    }

    @Override
    public @NotNull HashApi hash() {
        return hashApi;
    }
}
