package com.jianliu.excel;

import com.jianliu.excel.annotation.Column;
import com.jianliu.excel.convertor.ExcelConvertors;
import com.jianliu.excel.convertor.TypeConvertor;
import com.jianliu.excel.util.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by cdliujian1 on 2018/8/17.
 */
public class AnnotationWriter<T> extends ExcelWriter<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AnnotationWriter.class);

    private static final WeakHashMap<Class, List<Field>> ClassFieldMap = new WeakHashMap<>();

    private Class<T> targetClass;

    private List<Field> columnFields;

    private Map<String, Column> fileAnnotationMap = new HashMap<>();

    AnnotationWriter(Class<T> targetClass) {
        this.targetClass = targetClass;

        synchronized (ClassFieldMap) {
            columnFields = ClassFieldMap.get(targetClass);
            if (columnFields == null) {
                columnFields = ExcelUtil.getOrderColumnFields(targetClass);
                if (!ClassFieldMap.containsKey(targetClass)) {
                    ClassFieldMap.put(targetClass, columnFields);
                }
            }
        }

        for (Field field : columnFields) {
            fileAnnotationMap.put(field.getName(), field.getAnnotation(Column.class));
        }

    }


    //    private static

    @Override
    public void writeRow(T obj, ConvertContext convertContext) {
        for (Field f : columnFields) {
            Column column = fileAnnotationMap.get(f.getName());
            try {
                Object fieldValue = f.get(obj);
                TypeConvertor typeConvertor = ExcelConvertors.getConvertor(f.getType());
                if (column.typeConvertor() != TypeConvertor.class) {
                    typeConvertor = ExcelConvertors.getConvertorByConvertClass((Class<TypeConvertor>) column.typeConvertor());
                    if (typeConvertor == null) {
                        typeConvertor = column.typeConvertor().newInstance();
                        ExcelConvertors.registerCustomConvertors((Class<TypeConvertor>) column.typeConvertor(), typeConvertor);
                    }
                }
                //如果有convertor就先convert
                if (typeConvertor != null) {
                    convertContext.setColumn(column);
                    fieldValue = typeConvertor.transfermToString(fieldValue, convertContext);
                }

                addCell(column.value(), fieldValue);
            } catch (Exception e) {
                LOGGER.error("设置cell失败", e);
            }
        }
    }
}
