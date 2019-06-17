package com.jianliu.excel.convertor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * class ExcelConvertors
 *
 * @author jianliu
 * @since 2019/6/17
 */
public class ExcelConvertors {

    protected final static Map<Class, TypeConvertor> ClassTypeConvertors = new HashMap<>();
    private final static ConcurrentMap<Class<TypeConvertor>, TypeConvertor> CustomTypeConvertors = new ConcurrentHashMap<>();

    static {
        ClassTypeConvertors.put(Date.class, new DateTypeConvertor());
    }

    public static void registerCustomConvertors(Class<TypeConvertor> convertTypeClass, TypeConvertor typeConvertor) {
        if (convertTypeClass == null || typeConvertor == null) {
            return;
        }
        CustomTypeConvertors.putIfAbsent(convertTypeClass, typeConvertor);
    }

    /**
     * 获取指定类型的Convertor
     *
     * @param type
     * @return
     */
    public static TypeConvertor getConvertor(Class type) {
        if (type == null) {
            return null;
        }
        return ClassTypeConvertors.get(type);
    }

    /**
     * 获取指定类型的Convertor
     *
     * @param typeConvertorClass
     * @return
     */
    public static TypeConvertor getConvertorByConvertClass(Class<TypeConvertor> typeConvertorClass) {
        if (typeConvertorClass == null) {
            return null;
        }
        return CustomTypeConvertors.get(typeConvertorClass);
    }

}
