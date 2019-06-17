import com.jianliu.excel.ConvertContext;
import com.jianliu.excel.convertor.TypeConvertor;

/**
 * Created by cdliujian1 on 2018/8/17.
 */
public class SimpleDataConvertor implements TypeConvertor<SimpleData> {

    @Override
    public SimpleData convert(String value, ConvertContext convertContext) {
        SimpleData simpleData = new SimpleData();
        simpleData.setId(Integer.valueOf(value));
        return simpleData;
    }


    @Override
    public String transfermToString(SimpleData o, ConvertContext convertContext) {
        return o.getId().toString();
    }
}
