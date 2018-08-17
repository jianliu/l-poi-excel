package jianliu.excel.convertor;

/**
 * 类型转换器
 * Created by cdliujian1 on 2018/8/17.
 */
public interface TypeConvertor<T> {

    final TypeConvertor NULL = null;

    /**
     * 将字符串转换为固定对象
     * @param value
     * @return
     */
    T convert(String value);

}
