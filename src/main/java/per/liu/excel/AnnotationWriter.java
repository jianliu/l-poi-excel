package per.liu.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.liu.excel.annotation.Column;
import per.liu.excel.util.ExcelUtil;

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

    List<Field> columnFields;

    private Map<String, Column> fileAnnotationMap = new HashMap<>();

    public AnnotationWriter(Class<T> targetClass) {
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


    //    private static

    @Override
    public void writeRow(T obj) {

        for (Field f : columnFields) {
            Column column = fileAnnotationMap.get(f.getName());
            try {
                addCell(column.value(), f.get(obj).toString());
            } catch (Exception e) {
                LOGGER.error("设置cell失败", e);
            }
        }
    }
}
