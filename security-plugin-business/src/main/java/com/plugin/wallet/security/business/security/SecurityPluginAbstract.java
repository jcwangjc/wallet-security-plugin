package com.plugin.wallet.security.business.security;

import com.plugin.wallet.security.business.SecurityPlugin;
import com.plugin.wallet.security.business.annotation.SecurityMapping;
import com.plugin.wallet.security.business.enums.SecurityElement;
import com.plugin.wallet.security.business.reflectasm.ReflectRecord;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : laoA
 * @describe : 安全管理接口抽象逻辑
 * @email : laoa@markcoin.net
 */

public abstract class SecurityPluginAbstract<T> implements SecurityPlugin<T> {
    protected Map<String,Object> carrier(T last) throws IllegalAccessException {
        List<Field> fields = ReflectRecord.getFields(last.getClass());
        SecurityMapping securityMapping;
        Map<String,Object> result=new HashMap<>();
        for(Field privateField:fields){
            securityMapping = privateField.getAnnotation(SecurityMapping.class);
            if (securityMapping!=null) {
                privateField.setAccessible(true);
                Object o = privateField.get(last);
                String name="";
                if(securityMapping!=null&&!securityMapping.type().equals(SecurityElement.NULL)){
                    name=securityMapping.type().name();
                }else {
                    continue;
                }
                if(o!=null){
                    result.put(name,o);
                }
            }
        }
        return result;
    }
}
