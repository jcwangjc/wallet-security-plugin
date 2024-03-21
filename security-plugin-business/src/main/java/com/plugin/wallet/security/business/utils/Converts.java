package com.plugin.wallet.security.business.utils;

import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.plugin.wallet.security.business.annotation.SecurityMapping;
import com.plugin.wallet.security.business.enums.SecurityElement;
import com.plugin.wallet.security.business.model.AntLog;
import com.plugin.wallet.security.business.reflectasm.ReflectRecord;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : laoA
 * @describe : 对象私有属性操作
 * @email : laoa@markcoin.net
 */
public class Converts {
    public static final String SET_METHOD="set";

    public static String bestow(Object t, Object value, SecurityElement securityElement){
        Class<?> clazz = t.getClass();
        MethodAccess access = ReflectRecord.getMethodAccess(clazz);
        List<Field> fields = ReflectRecord.getFields(clazz);
        SecurityMapping securityMapping;
        for(Field privateField:fields){
            if ((securityMapping = privateField.getAnnotation(SecurityMapping.class)) != null && (securityMapping.type().equals(securityElement))) {
                String fieldName = privateField.getName();
                String setKey = clazz.getName() + SET_METHOD + fieldName;
                Integer setIndex = ReflectRecord.getSetIndex(setKey);
                if(setIndex==null){
                    setIndex = access.getIndex(SET_METHOD + StringUtils.capitalize(fieldName));
                    ReflectRecord.putSetIndex(setKey,setIndex);
                }
                access.invoke(t, setIndex, value);
                return fieldName;
            }
        }
        return null;
    }

    public static <T> Map<String,Object> convert(T log) throws IllegalAccessException {
        Class<?> clazz = log.getClass();
        List<Field> fields = ReflectRecord.getFields(clazz);
        Map<String,Object> result=new HashMap<>();
        SecurityMapping securityMapping;
        for(Field privateField:fields){
            if ((securityMapping = privateField.getAnnotation(SecurityMapping.class)) != null) {
                String fieldName = privateField.getName();
                privateField.setAccessible(true);
                Object o = privateField.get(log);
                if(o!=null){
                    result.put(fieldName,o);
                }
            }
        }
        return result;
    }

    public static <T> Map<String,Object> convert(T log,SecurityElement... renounce) throws IllegalAccessException {
        Class<?> clazz = log.getClass();
        List<Field> fields = ReflectRecord.getFields(clazz);
        Map<String,Object> result=new HashMap<>();
        SecurityMapping securityMapping;
        for(Field privateField:fields){
            if ((securityMapping = privateField.getAnnotation(SecurityMapping.class)) != null) {
                if(collision(securityMapping.type(),renounce)){
                    continue;
                }
                String fieldName = privateField.getName();
                privateField.setAccessible(true);
                Object o = privateField.get(log);
                if(o!=null){
                    result.put(fieldName,o);
                }
            }
        }
        return result;
    }

    private static Boolean collision(SecurityElement type, SecurityElement... renounce){
        for(SecurityElement r:renounce){
            if(type.equals(r)){
                return true;
            }
        }
        return false;
    }

    public static <T> AntLog convert(T log,Map<String, Object> carrier) throws IllegalAccessException {
        AntLog antLog=new AntLog();
        List<Field> fields = ReflectRecord.getFields(antLog.getClass());
        SecurityMapping securityMapping;
        for(Field privateField:fields){
            if((securityMapping = privateField.getAnnotation(SecurityMapping.class)) != null){
                SecurityElement type = securityMapping.type();
                Object value = carrier.get(type.name());
                Converts.bestow(antLog,value,type);
            }
        }

        //计算sign message
        Map<String, Object> params = Converts.convert(log,SecurityElement.SIGN);
        //擦除log的sign
        String signMessage= JSONObject.toJSONString(params);
        //设置sign message
        Converts.bestow(antLog,signMessage,SecurityElement.SIGN_MESSAGE);

        //擦除log的hash
        params = Converts.convert(log,SecurityElement.SIGN,SecurityElement.HASH);
        String hashMessage= JSONObject.toJSONString(params);
        //设置hash message
        Converts.bestow(antLog,hashMessage,SecurityElement.HASH_MESSAGE);
        return antLog;
    }
}
