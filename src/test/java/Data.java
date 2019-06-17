import com.jianliu.excel.annotation.Column;
import com.jianliu.excel.convertor.DateTypeConvertor;

import java.util.Date;

/**
 * 测试实体
 * Created by cdliujian1 on 2017/6/1.
 */
public class Data {

    @Column(value = "ID")
    private int id;

    @Column(value = "code")
    private double code;

    @Column(value = "日期", typeConvertor = DateTypeConvertor.class, pattern = "yyyy-MM-dd HH:mm")
    private Date date;


    @Column(value = "Simple", typeConvertor = SimpleDataConvertor.class)
    private SimpleData simpleData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCode() {
        return code;
    }

    public void setCode(double code) {
        this.code = code;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SimpleData getSimpleData() {
        return simpleData;
    }

    public void setSimpleData(SimpleData simpleData) {
        this.simpleData = simpleData;
    }
}
