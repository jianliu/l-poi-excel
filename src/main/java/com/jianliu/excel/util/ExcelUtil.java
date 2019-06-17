package com.jianliu.excel.util;

import com.jianliu.excel.ConvertContext;
import com.jianliu.excel.annotation.Column;
import com.jianliu.excel.convertor.ExcelConvertors;
import com.jianliu.excel.convertor.TypeConvertor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by cdliujian1 on 2018/8/17.
 */
public class ExcelUtil {


    /**
     * 获取对象的mongoField域
     *
     * @param clazz
     * @return
     */
    public static List<Field> getOrderColumnFields(Class clazz) {

        List<Field> columnFields = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                columnFields.add(field);
            }

            field.setAccessible(true);
        }

        Collections.sort(columnFields, new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                return Integer.compare(o1.getAnnotation(Column.class).order(),
                        o2.getAnnotation(Column.class).order());
            }
        });

        return columnFields;
    }

    public static Object convertFromString(String cellValue, Class clazz, ConvertContext convertContext) {

        if (clazz.isPrimitive()) {
            return convertPrimitive(cellValue, clazz);
        }

        if (cellValue == null || cellValue.equals("")) {
            return null;
        }

        if (clazz == String.class) {
            return cellValue;
        }

        if (clazz == Boolean.class) {
            return Boolean.valueOf(cellValue);
        } else if (clazz == Character.class) {
            return (cellValue.charAt(0));
        } else if (clazz == Byte.class) {
            return Byte.valueOf(cellValue);
        } else if (clazz == Short.class) {
            return Short.valueOf(cellValue);
        } else if (clazz == Integer.class) {
            return Integer.valueOf(cellValue);
        } else if (clazz == Long.class) {
            return Long.valueOf(cellValue);
        } else if (clazz == Float.class) {
            return Float.valueOf(cellValue);
        } else if (clazz == Double.class) {
            return Double.valueOf(cellValue);
        }

        TypeConvertor typeConvertor = ExcelConvertors.getConvertor(clazz);
        Class convertTypeCClazz = convertContext.getColumn().typeConvertor();
        if (convertTypeCClazz != TypeConvertor.class && TypeConvertor.class.isAssignableFrom(convertTypeCClazz)) {
            try {
                typeConvertor = (TypeConvertor) convertTypeCClazz.newInstance();
                //注册实例，防止频繁创建
                ExcelConvertors.registerCustomConvertors(convertTypeCClazz, typeConvertor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (typeConvertor != null) {
            return typeConvertor.convert(cellValue, convertContext);
        }

        return null;
    }

//    public static Object convertFrom(Object cellValue, Class clazz, Column column) {
//
//        if (clazz.isPrimitive()) {
//            return convertPrimitive(cellValue, clazz);
//        }
//
//        if (cellValue == null || cellValue.equals("")) {
//            return null;
//        }
//
//        if (clazz == String.class) {
//            return cellValue;
//        }
//
//        if (clazz == Boolean.class) {
//            return Boolean.valueOf(cellValue);
//        } else if (clazz == Character.class) {
//            return (cellValue.charAt(0));
//        } else if (clazz == Byte.class) {
//            return Byte.valueOf(cellValue);
//        } else if (clazz == Short.class) {
//            return Short.valueOf(cellValue);
//        } else if (clazz == Integer.class) {
//            return Integer.valueOf(cellValue);
//        } else if (clazz == Long.class) {
//            return Long.valueOf(cellValue);
//        } else if (clazz == Float.class) {
//            return Float.valueOf(cellValue);
//        } else if (clazz == Double.class) {
//            return Double.valueOf(cellValue);
//        }
//
//        Class typeCClazz = column.typeConvertor();
//        if (typeCClazz != Object.class && TypeConvertor.class.isAssignableFrom(typeCClazz)) {
//            TypeConvertor typeConvertor = CustomClassTypeConvertors.get(typeCClazz);
//            if (typeConvertor == null) {
//                try {
//                    typeConvertor = (TypeConvertor) typeCClazz.newInstance();
//                } catch (InstantiationException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//                CustomClassTypeConvertors.putIfAbsent(typeCClazz, typeConvertor);
//            }
//            return typeConvertor.convert(cellValue);
//        }
//
//        TypeConvertor typeConvertor = ClassTypeConvertors.get(clazz);
//        if (typeConvertor != null) {
//            return typeConvertor.convert(cellValue);
//        }
//
//        return null;
//    }

    /**
     * @param cellValue
     * @param clazz
     * @return
     * @see java.lang.Boolean#TYPE
     * @see java.lang.Character#TYPE
     * @see java.lang.Byte#TYPE
     * @see java.lang.Short#TYPE
     * @see java.lang.Integer#TYPE
     * @see java.lang.Long#TYPE
     * @see java.lang.Float#TYPE
     * @see java.lang.Double#TYPE
     */
    private static Object convertPrimitive(String cellValue, Class clazz) {

        if (clazz == boolean.class) {
            return Boolean.valueOf(cellValue);
        } else if (clazz == char.class) {
            return (cellValue != null && !cellValue.equals("")) ? (cellValue.charAt(0)) : null;
        } else if (clazz == byte.class) {
            return Byte.valueOf(cellValue);
        } else if (clazz == short.class) {
            return Short.valueOf(cellValue);
        } else if (clazz == int.class) {
            return Integer.valueOf(cellValue);
        } else if (clazz == long.class) {
            return Long.valueOf(cellValue);
        } else if (clazz == float.class) {
            return Float.valueOf(cellValue);
        } else if (clazz == double.class) {
            return Double.valueOf(cellValue);
        }

        return null;
    }

    public static boolean isStringNonEmpty(String str) {
        return str != null && !str.equals("");
    }

}
