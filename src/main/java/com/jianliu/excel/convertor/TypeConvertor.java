package com.jianliu.excel.convertor;

import com.jianliu.excel.ConvertContext;

/**
 * 类型转换器
 * 用于读取excel列数据
 * Created by cdliujian1 on 2018/8/17.
 */
public interface TypeConvertor<T> {

    TypeConvertor NULL = null;

    /**
     * 将字符串转换为固定对象
     * @param value
     * @param convertContext
     * @return
     */
    T convert(String value, ConvertContext convertContext);

    /**
     * 转换成excel支持的对象
     * @param t java对象
     * @param convertContext
     * @return 目前返回String
     */
    String transfermToString(T t, ConvertContext convertContext);


}
