import com.jianliu.excel.AnnotationResolver;
import com.jianliu.excel.AnnotationWriter;
import com.jianliu.excel.ExcelHelper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import com.jianliu.excel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdliujian1 on 2017/6/1.
 */
public class TestExcel {

    /**
     * windows-4 cpu cores-8 GB Memory ,use 1270ms
     */
    @Test
    public void write() {
        String filepath = "D:" + "/" + "l-test-write.xls";
        new File(filepath).delete();
        long start = System.currentTimeMillis();
        List<Data> dataList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Data d = new Data();
            d.setId(i);
            dataList.add(d);
        }

        ExcelHelper excelHelper = new ExcelHelper(10000, true);
        excelHelper.writeExcel(filepath, dataList);


        System.out.println("共耗时：" + (System.currentTimeMillis() - start) + " ms");
    }

    /**
     *
     * windows-4 cpu cores-8 GB Memory ,use 439~550ms
     * @throws IOException
     * @throws InvalidFormatException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void read() throws IOException, InvalidFormatException, NoSuchFieldException, IllegalAccessException {
        String filepath = "D:" + "/" + "l-test-write.xls";
        //new File(filepath).delete();
        long start = System.currentTimeMillis();

        ExcelHelper excelHelper = new ExcelHelper();

//        List<Data> dataListFromExcel = excelHelper.readExcel(new FileInputStream(filepath), Data.class,
//                new ExcelResolver<Data>() {
//                    @Override
//                    public boolean resolve(Data data) {
//                        data.setId(Integer.valueOf(getCellValue("ID")));
//                        return true;
//                    }
//
//                    //自定义生成对象的方式，不重写此方法时，默认使用反射生成对象
//                    @Override
//                    public Data customInitialiseObj() {
//                        return new Data();
//                    }
//                }, 0);

        List<Data> dataListFromExcel = excelHelper.readExcel(new FileInputStream(filepath), Data.class, 0);

        System.out.println("size：" + dataListFromExcel.size() + " ms");
        System.out.println("共耗时：" + (System.currentTimeMillis() - start) + " ms");
    }

}
