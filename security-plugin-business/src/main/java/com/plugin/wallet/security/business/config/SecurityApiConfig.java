package com.plugin.wallet.security.business.config;

import com.plugin.wallet.security.ca.SecurityApi;
import com.plugin.wallet.security.ca.enums.SecurityLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author : laoA
 * @describe : ca key的API配置
 * @email : laoa@markcoin.net
 */
@Component
public class SecurityApiConfig {
    @Value("${wallet.security.password}")
    private String password;
    @Value("${wallet.security.path}")
    private String path;
    @Value("${wallet.security.alias}")
    private String alias;

    @Bean
    public SecurityApi securityApi() throws IOException {
        SecurityApi securityApi = SecurityApi.builder()
                .withKeyStoreLocation(path)
                .withKeyStorePassword(password, SecurityLevel.B)
                .withKeyStoreAlias(alias)
                .build();
        return securityApi;
    }
}
