package com.jianliu.excel.convertor;

import com.jianliu.excel.ConvertContext;
import com.jianliu.excel.util.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日期转换器
 * Created by cdliujian1 on 2018/8/17.
 */
public class DateTypeConvertor implements TypeConvertor<Date> {

    private final static Logger LOGGER = LoggerFactory.getLogger(DateTypeConvertor.class);

    private final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    protected static final List<String> DEFAULT_DATE_PARTERNS = new ArrayList<>();

    static {
        DEFAULT_DATE_PARTERNS.add("yyyy-MM-dd HH:mm:ss");
        DEFAULT_DATE_PARTERNS.add("yyyy-MM-dd");
    }


    @Deprecated
    private ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat();
        }
    };

    @Override
    public Date convert(String value, ConvertContext convertContext) {
        if (!ExcelUtil.isStringNonEmpty(value)) {
            return null;
        }

        String customPattern = getCustomPatternOrDefault(convertContext);
        if (ExcelUtil.isStringNonEmpty(customPattern)) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(customPattern);
                return simpleDateFormat.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
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


    @Override
    public String transfermToString(Date date, ConvertContext convertContext) {
        String customPattern = getCustomPatternOrDefault(convertContext);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(customPattern);
        return simpleDateFormat.format(date);
    }

    private String getCustomPatternOrDefault(ConvertContext convertContext) {
        if (convertContext.getColumn() == null) {
            return DEFAULT_DATE_PATTERN;
        }
        String customPattern = convertContext.getColumn().pattern();
        return ExcelUtil.isStringNonEmpty(customPattern) ? customPattern : DEFAULT_DATE_PATTERN;
    }
}
