package com.plugin.wallet.security.business.reflectasm;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author : laoA
 * @describe : class对象管理工具
 * @email : laoa@markcoin.net
 */
public class ReflectRecord {
    /**
     * 方法的的缓存
     */
    private static final Map<Class<?>, MethodAccess> ACCESS_MAP = new HashMap<>(64);

    /**
     * 字段的缓存--原始的
     */
    private static final Map<Class<?>, List<Field>> FIELDS_MAP = new HashMap<>(64);

    /**
     * 字段的缓存
     */
    private static final Map<Class<?>, FieldAccess> FIELDACCESS_MAP = new HashMap<>();

    /**
     * 下标的缓存
     */
    private static final Map<String, Integer> INDEX_MAP = new HashMap<>(64);

    /**
     * 构建对象方法的的缓存
     */
    private static final Map<Class<?>, ConstructorAccess<?>> CONSTRUCTOR_ACCESS_MAP = new HashMap<>(64);


    /**
     * 获取对象句柄
     * @param cs
     * @return
     */
    public static ConstructorAccess getConstructorAccess(Class cs){
        ConstructorAccess<?> constructorAccess = CONSTRUCTOR_ACCESS_MAP.get(cs);
        if(constructorAccess==null){
            constructorAccess=ConstructorAccess.get(cs);
            CONSTRUCTOR_ACCESS_MAP.put(cs,constructorAccess);
        }
        return constructorAccess;
    }

    /**
     * 获取到对象的方法的句柄
     * @return MethodAccess  返回方法的句柄
     */
    public static MethodAccess getMethodAccess(Class cs) {
        MethodAccess sAccess = ACCESS_MAP.get(cs);
        if (sAccess == null) {
            sAccess = MethodAccess.get(cs);
            ACCESS_MAP.put(cs, sAccess);
        }
        return sAccess;
    }

    /**
     * 获取到源对象的字段值
     * @param dest 源对象
     * @return List<Field>   字段属性.
     */
    public static List<Field> getFields(Class dest) {
        List<Field> fields = FIELDS_MAP.get(dest);
        if (fields == null) {
            fields = getFieldsPrivate(dest);
            FIELDS_MAP.put(dest.getClass(), fields);
        }
        return fields;
    }

    /**
     * 获取到源对象的字段值
     * @param dest 源对象
     * @return List<Field>  字段属性.
     */
    public static FieldAccess getFieldAccess(Class dest) {
        FieldAccess fieldAccess = FIELDACCESS_MAP.get(dest.getClass());
        if (fieldAccess == null) {
            fieldAccess = FieldAccess.get(dest);
            FIELDACCESS_MAP.put(dest.getClass(), fieldAccess);
        }
        return fieldAccess;
    }

    public static Integer getSetIndex(String key){
        boolean b = INDEX_MAP.containsKey(key);
        if(b==false){
            return null;
        }
        return INDEX_MAP.get(key);
    }

    public static void putSetIndex(String key,Integer value){
        INDEX_MAP.put(key,value);
    }

    /**
     * 获取对象的所有字段
     * @param objClass
     * @return
     */
    private static List<Field> getFieldsPrivate(Class objClass) {
        List<Field> fieldList = new ArrayList<>();
        while (null != objClass) {
            fieldList.addAll(Arrays.asList(objClass.getDeclaredFields()));
            objClass = objClass.getSuperclass();
        }
        return fieldList;
    }
}
