package com.jianliu.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jianliu.excel.annotation.Column;
import com.jianliu.excel.util.ExcelUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by cdliujian1 on 2018/8/17.
 */
public class AnnotationResolver<T> extends ExcelResolver<T>{

    private final static Logger LOGGER = LoggerFactory.getLogger(AnnotationResolver.class);

    private Class<T> targetClass;

    private static final WeakHashMap<Class, List<Field>> ClassFieldMap = new WeakHashMap<>();

    private List<Field> columnFields;

    private Map<String, Column> fileAnnotationMap = new HashMap<>();

    private boolean rethrow = false;

    public AnnotationResolver(Class targetClass) {
        this.targetClass = targetClass;

        synchronized (ClassFieldMap) {
            columnFields = ClassFieldMap.get(targetClass);
            if (columnFields == null) {
                columnFields = ExcelUtil.getOrderColumnFields(targetClass);
                ClassFieldMap.putIfAbsent(targetClass, columnFields);
            }
        }

        for (int i = 0; i < columnFields.size(); i++) {
            Field field = columnFields.get(i);
            fileAnnotationMap.put(field.getName(), field.getAnnotation(Column.class));
        }

    }

    @Override
    public boolean resolve(T data) throws Exception{

        for(Field f: columnFields){
            Column column = fileAnnotationMap.get(f.getName());
            String cellValue = getCellValue(column.value());
            if(cellValue == null || cellValue.equals("")){
                continue;
            }

            try {
                f.set(data, ExcelUtil.convertString(cellValue, f.getType(), column));
            }catch (Exception e){
                if(rethrow){
                    throw e;
                }
                LOGGER.error("读取excel cell失败", e);
            }

        }
        return true;

    }
}
