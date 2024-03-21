package com.plugin.wallet.security.business.handle.sign;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.plugin.wallet.security.business.handle.SecuritySignHandler;
import com.plugin.wallet.security.business.annotation.SecurityMapping;
import com.plugin.wallet.security.business.enums.SecurityElement;
import com.plugin.wallet.security.business.reflectasm.ReflectRecord;
import com.plugin.wallet.security.business.utils.Converts;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;
import java.util.Map;

/**
 * @author : laoA
 * @describe : 设置对象的index和pre hash
 * @email : laoa@markcoin.net
 */
public class MigrateSignHandler extends SecuritySignHandler {

    @Override
    public <S> void doHandler(S t, Map<String, Object> carrierMap) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, IOException, IllegalAccessException {
        Class<?> clazz = t.getClass();
        MethodAccess access = ReflectRecord.getMethodAccess(clazz);
        SecurityMapping securityMapping;
        List<Field> fields = ReflectRecord.getFields(t.getClass());
        for(Field privateField:fields){
            if ((securityMapping = privateField.getAnnotation(SecurityMapping.class)) != null && (securityMapping.type().equals(SecurityElement.PRE_HASH))) {
                SecurityElement type = securityMapping.type();
                Object value = null;
//                if(type.equals(SecurityElement.INDEX)){
//                    if(carrierMap.get(SecurityElement.INDEX.name())!=null){
//                        value=Long.valueOf(carrierMap.get(SecurityElement.INDEX.name()).toString())+1;
//                    }else{
//                        value=FIRST_INDEX;
//                    }
//                }else if(type.equals(SecurityElement.PRE_HASH)){
//                    value=carrierMap.get(SecurityElement.HASH.name());
//                }
                value=carrierMap.get(SecurityElement.HASH.name());
                String fieldName = privateField.getName();
                String setKey = clazz.getName() + Converts.SET_METHOD + fieldName;
                Integer setIndex = ReflectRecord.getSetIndex(setKey);
                if(setIndex==null){
                    setIndex = access.getIndex(Converts.SET_METHOD + StringUtils.capitalize(fieldName));
                    ReflectRecord.putSetIndex(setKey,setIndex);
                }
                access.invoke(t, setIndex, value);
            }
        }
        next.doHandler(t,carrierMap);
    }
}
