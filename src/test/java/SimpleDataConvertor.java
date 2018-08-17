import com.jianliu.excel.convertor.TypeConvertor;

/**
 * Created by cdliujian1 on 2018/8/17.
 */
public class SimpleDataConvertor implements TypeConvertor{

    @Override
    public Object convert(String value) {
        SimpleData simpleData = new SimpleData();
        simpleData.setId(Integer.valueOf(value));
        return simpleData;
    }
}
