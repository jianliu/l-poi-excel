package per.liu.excel.convertor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.liu.excel.util.ExcelUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cdliujian1 on 2018/8/17.
 */
public class DateTypeConvertor implements TypeConvertor<Date> {

    private final static Logger LOGGER = LoggerFactory.getLogger(DateTypeConvertor.class);


    protected static final List<String> DEFAULT_DATE_PARTERNS = new ArrayList<>();

    static {
        DEFAULT_DATE_PARTERNS.add("yyyy-MM-dd HH:mm:ss");
        DEFAULT_DATE_PARTERNS.add("yyyy-MM-dd");
    }

    public synchronized static void registerPattern(String p) {
        if (DEFAULT_DATE_PARTERNS.size() > 100) {
            //ignore
            LOGGER.warn("添加的时间格式过多，添加失败");
            return;
        }
        DEFAULT_DATE_PARTERNS.add(p);

    }

    private ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat();
        }
    };

    @Override
    public Date convert(String value) {
        if(ExcelUtil.isStringEmpty(value)){
            return null;
        }

        for (String p : DEFAULT_DATE_PARTERNS) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(p);
                return simpleDateFormat.parse(value);
            } catch (Exception e) {
                //ignore
            }
        }
        return null;
    }
}
