package com.plugin.wallet.security.ca;

import com.plugin.wallet.security.ca.enums.SecurityLevel;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author : laoa
 * @describe : 安全管理API接口
 * @email : laoa@markcoin.net
 */
public interface SecurityApi {
    @NotNull
    KeystoreApi keystore();

    @NotNull
    HashApi hash();

    interface Builder{
        /**
         * 设置密码
         * @param password 秘文，通过系统随机生成的公私钥加密的结果
         * @param securityLevel 安全级别
         * @return
         */
        Builder withKeyStorePassword(@NotNull String password,@NotNull SecurityLevel securityLevel) throws IOException;

        /**
         * keystore地址
         * @param path
         * @return
         */
        @NotNull
        Builder withKeyStoreLocation(@NotNull String path);

        /**
         * 秘钥的alias
         * @param alias
         * @return
         */
        @NotNull
        Builder withKeyStoreAlias(@NotNull String alias);

        /**
         * 编译
         * @return
         */
        @NotNull
        SecurityApi build();
    }

    @NotNull
    static Builder builder(){
        SecurityApiBuilder securityApiBuilder=new SecurityApiBuilder();
        return securityApiBuilder;
    }
}
