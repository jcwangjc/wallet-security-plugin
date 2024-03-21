package com.plugin.wallet.security.business.annotation;

import com.plugin.wallet.security.business.enums.SecurityElement;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author : laoA
 * @describe : 安全管理字段标识注解
 * @email : laoa@markcoin.net
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface SecurityMapping {
    SecurityElement type() default SecurityElement.NULL;
}
