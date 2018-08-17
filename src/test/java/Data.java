import com.jianliu.excel.annotation.Column;

/**
 * 测试实体
 * Created by cdliujian1 on 2017/6/1.
 */
public class Data {

//    @Column(value = "ID", order = 0)
    private int id;

    @Column(value = "ID", order = 0, typeConvertor = SimpleDataConvertor.class)
    private SimpleData simpleData;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
