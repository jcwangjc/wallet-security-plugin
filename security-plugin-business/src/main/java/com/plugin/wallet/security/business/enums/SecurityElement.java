package com.plugin.wallet.security.business.enums;

/**
 * @author : laoA
 * @describe : 字段含义
 * @email : laoa@markcoin.net
 */
public enum SecurityElement implements Comparable<SecurityElement>{
    HASH,
    PRE_HASH,
    INDEX,
    SIGN,
    BALANCE,
    LOCKED,
    TOTAL_CHANGE_VALUE,
    HASH_MESSAGE,
    SIGN_MESSAGE,
    NULL
}
