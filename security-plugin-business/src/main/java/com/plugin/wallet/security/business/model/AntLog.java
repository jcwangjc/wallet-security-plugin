package com.plugin.wallet.security.business.model;

import com.plugin.wallet.security.business.annotation.SecurityMapping;
import com.plugin.wallet.security.business.enums.SecurityElement;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : laoA
 * @describe : 校验对象
 * @email : laoa@markcoin.net
 */
@Data
public class AntLog {

    @SecurityMapping(type = SecurityElement.HASH_MESSAGE)
    private String hashMessage;
    @SecurityMapping(type = SecurityElement.SIGN_MESSAGE)
    private String signMessage;

    @SecurityMapping(type = SecurityElement.HASH)
    private String hash;
    @SecurityMapping(type = SecurityElement.PRE_HASH)
    private String preHash;
    @SecurityMapping(type = SecurityElement.INDEX)
    private Long index;
    @SecurityMapping(type = SecurityElement.SIGN)
    private String sign;

    @SecurityMapping(type = SecurityElement.BALANCE)
    private BigDecimal balance;
    @SecurityMapping(type = SecurityElement.LOCKED)
    private BigDecimal locked;
    @SecurityMapping(type = SecurityElement.TOTAL_CHANGE_VALUE)
    private BigDecimal totalChangeValue;
}
