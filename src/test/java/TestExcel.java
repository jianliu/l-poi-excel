import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import per.liu.excel.ExcelHelper;
import per.liu.excel.ExcelResolver;
import per.liu.excel.ExcelWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdliujian1 on 2017/6/1.
 */
public class TestExcel {

    @Test
    public void write() {
        String filepath = "E:" + "/" + "l-test-write.xls";
        new File(filepath).delete();
        long start = System.currentTimeMillis();
        List<Integer> data = new ArrayList<Integer>();
        for (int i = 0; i < 100000; i++) {
            data.add(i);
        }

        ExcelHelper excelHelper = new ExcelHelper(10000, true);
        excelHelper.writeExcel(filepath, data, new ExcelWriter<Integer>() {
            @Override
            public void writeRow(Integer id) {
                addCell("ID", id);
            }
        });


        System.out.println("共耗时：" + (System.currentTimeMillis() - start) + " ms");
    }

    @Test
    public void read() throws IOException, InvalidFormatException, NoSuchFieldException, IllegalAccessException {
        String filepath = "E:" + "/" + "l-test-write.xls";
        //new File(filepath).delete();
        long start = System.currentTimeMillis();

        ExcelHelper excelHelper = new ExcelHelper();

        List<Data> dataListFromExcel = excelHelper.readExcel(new FileInputStream(filepath), Data.class,
                new ExcelResolver<Data>() {
                    @Override
                    public boolean resolve(Data data) {
                        data.setId(Integer.valueOf(getCellValue("ID")));
                        return true;
                    }

                    //自定义生成对象的方式，不重写此方法时，默认使用反射生成对象
                    @Override
                    public Data customInitialiseObj() {
                        return new Data();
                    }
                }, 2);

        System.out.println("size：" + dataListFromExcel.size() + " ms");
        System.out.println("共耗时：" + (System.currentTimeMillis() - start) + " ms");
    }

}
