package com.plugin.wallet.security.test;

import com.plugin.wallet.security.business.annotation.SecurityMapping;
import com.plugin.wallet.security.business.enums.SecurityElement;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LogInfo {

    private Long id;
    private Long businessId;
    private Long uid;

    @SecurityMapping(type = SecurityElement.NULL)
    private String a;
    @SecurityMapping(type = SecurityElement.NULL)
    private String b;
    @SecurityMapping(type = SecurityElement.NULL)
    private String c;
    private String d;

    //追加的注解
    @SecurityMapping(type = SecurityElement.BALANCE)
    private BigDecimal balance;
    @SecurityMapping(type = SecurityElement.LOCKED)
    private BigDecimal locked;
    @SecurityMapping(type = SecurityElement.TOTAL_CHANGE_VALUE)
    private BigDecimal totalChangeValue;

    //新增的字段
    @SecurityMapping(type = SecurityElement.HASH)
    private String hash;
    @SecurityMapping(type = SecurityElement.PRE_HASH)
    private String preHash;
    @SecurityMapping(type = SecurityElement.INDEX)
    private Long index;
    @SecurityMapping(type = SecurityElement.SIGN)
    private String sign;
}
