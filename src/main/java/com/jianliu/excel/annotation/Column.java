package com.jianliu.excel.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标识field可以写入excel或可以从excel中获取值
 * Created by cdliujian1 on 2018/8/17.
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface Column {
    /**
     * 列名称
     */
    String value() default "";

    /**
     * 排序编号，调用Integer.compare
     *
     * @return
     */
    int order() default 0;

    /**
     * 自定义类型转换器，将cell中的value转换为某个对象
     */
    Class<?> typeConvertor() default Object.class;


}

