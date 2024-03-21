package com.plugin.wallet.security.ca;

import com.plugin.wallet.security.ca.content.SecurityContent;
import com.plugin.wallet.security.ca.content.SecuritySecureManagement;
import com.plugin.wallet.security.ca.enums.SecurityLevel;
import com.plugin.wallet.security.ca.rsa.RSAUtils;
import com.plugin.wallet.security.ca.utils.TxtReadUtil;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.UUID;

/**
 * @author : laoa
 * @describe : ca认证接口实现
 * @email : laoa@markcoin.net
 */
final class SecurityApiBuilder implements SecurityApi.Builder{
    private String ca_path;
    private String ca_password;
    private String ca_alias;

    @Override
    public SecurityApi.@NotNull Builder withKeyStoreLocation(@NotNull String path) {
        this.ca_path=path;
        return this;
    }

    @Override
    public SecurityApi.@NotNull Builder withKeyStorePassword(@NotNull String password,@NotNull SecurityLevel securityLevel) throws IOException {
        if(securityLevel.equals(SecurityLevel.A)){
            this.ca_password=password;
        }else if(securityLevel.equals(SecurityLevel.B)){
            String privateKey = TxtReadUtil.read(SecurityContent.PASSWORD_FILE_LOCATION);
            String decrypt = RSAUtils.decrypt(password, privateKey);
            this.ca_password=decrypt;
        }else if(securityLevel.equals(SecurityLevel.C)){
            this.ca_password = RSAUtils.decrypt(password);
            UUID uuid = UUID.randomUUID();
            SecurityContent.KEYSTORE_PASSWORD=uuid.toString();
        }
        return this;
    }

    @Override
    public SecurityApi.@NotNull Builder withKeyStoreAlias(@NotNull String alias) {
        this.ca_alias=alias;
        return this;
    }

    @Override
    public @NotNull SecurityApi build() {
        return new SecurityApiProvider(ca_path,ca_password,ca_alias);
    }
}
