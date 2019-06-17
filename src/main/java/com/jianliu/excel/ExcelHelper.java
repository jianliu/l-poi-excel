package com.jianliu.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel操作帮助类，基于本类进行read或write操作
 * Created by cdliujian1 on 2015/7/28.
 */
public class ExcelHelper {

    /**
     * 一个sheet多少条记录，65535是poi允许的最大值
     */
    private final static int Default_PAGE_DATA_SIZE;

    /**
     * sheetname 格式
     */
    private final static String Default_Sheet_Name_Format = "Sheet {0}";

    private Integer customPageDataSize;

    /**
     * 当数据个数超过每页最大个数时，自动生成新的sheet
     */
    private boolean autoCreateNewSheet = true;

    static {
        int ps = Integer.valueOf(System.getProperty("excelhelper.max.pagesize", "65535"));
        if (ps > 65535) {
            Default_PAGE_DATA_SIZE = 65535;
        } else {
            Default_PAGE_DATA_SIZE = ps;
        }
    }

    private final Map<String, Integer> idxMap = new HashMap<String, Integer>();

    /**
     * read时的自定义sheetNo
     */
    private int targetSheetIdxR;

    public ExcelHelper() {
        this(null, true);
    }

    public ExcelHelper(Integer customPageDataSize, boolean autoCreateNewSheet) {
        this.customPageDataSize = customPageDataSize != null ? customPageDataSize : Default_PAGE_DATA_SIZE;
        this.autoCreateNewSheet = autoCreateNewSheet;

    }

    public void setTargetSheetIdxR(int targetSheetIdxR) {
        this.targetSheetIdxR = targetSheetIdxR;
    }


    //    @SuppressWarnings("unchecked")
//    public Class<T> getClz() {
//        if (tClass == null) {
//            tClass = (Class<T>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
//        }
//        return tClass;
//    }

    /**
     * 从文件中解析excel
     *
     * @param filename
     * @param clazz         准备读出的对象类型class
     * @param excelResolver 根据准备读出的对象类型 自定义一个excelResolver
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public <T> List<T> readExcel(String filename, Class<T> clazz, ExcelResolver<T> excelResolver) throws IOException, InvalidFormatException {
        return readExcel(filename, clazz, excelResolver, 0);
    }

    /**
     * 从文件中解析excel
     *
     * @param filename
     * @param clazz         准备读出的对象类型class
     * @param excelResolver 根据准备读出的对象类型 自定义一个excelResolver
     * @param sheetNo       第几个sheet，从0开始
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public <T> List<T> readExcel(String filename, Class<T> clazz, ExcelResolver<T> excelResolver, int sheetNo) throws IOException, InvalidFormatException {
        InputStream is = new FileInputStream(filename);
        Workbook workbook;
        try {
            if (filename.endsWith("xls")) {
                //未测试 excel 2003
                workbook = new HSSFWorkbook(is);
            } else {
                workbook = WorkbookFactory.create(is);
            }
            Sheet sheet = workbook.getSheetAt(sheetNo);
            if (sheet == null) {
                return null;
            }
            //赋值idxMap
            resolveHeader(sheet);
            excelResolver.setIdxMap(idxMap);
            return genResultList(clazz, excelResolver, sheet);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                //no op
            }
        }
    }

    /**
     * 从文件中解析excel,第一个sheet
     *
     * @param is
     * @param clazz         准备读出的对象类型class
     * @param excelResolver 根据准备读出的对象类型 自定义一个excelResolver
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public <T> List<T> readExcel(InputStream is, Class<T> clazz, ExcelResolver<T> excelResolver) throws IOException, InvalidFormatException {
        return readExcel(is, clazz, excelResolver, 0);
    }

    /**
     * 从文件中解析excel
     *
     * @param is
     * @param clazz   准备读出的对象类型class
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public <T> List<T> readExcel(InputStream is, Class<T> clazz) throws IOException, InvalidFormatException {
        try {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(this.targetSheetIdxR);
            if (sheet == null) {
                return null;
            }

            ExcelResolver excelResolver = new AnnotationResolver<T>(clazz);
            //赋值idxMap
            resolveHeader(sheet);
            excelResolver.setIdxMap(idxMap);
            return genResultList(clazz, excelResolver, sheet);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                //no op
            }
        }
    }

    /**
     * 从文件中解析excel
     *
     * @param is
     * @param clazz         准备读出的对象类型class
     * @param excelResolver 根据准备读出的对象类型 自定义一个excelResolver
     * @param sheetNo       第几个sheet，从0开始
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public <T> List<T> readExcel(InputStream is, Class<T> clazz, ExcelResolver<T> excelResolver, int sheetNo) throws IOException, InvalidFormatException {
        try {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(sheetNo);
            if (sheet == null) {
                return null;
            }
            //赋值idxMap
            resolveHeader(sheet);
            excelResolver.setIdxMap(idxMap);
            return genResultList(clazz, excelResolver, sheet);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                //no op
            }
        }
    }

    /**
     * 导出使用xlsx格式
     * 使用AnnotationWriter来写excel
     * @param filename
     * @param dataList
     */
    public <T> void writeExcel(String filename, List<T> dataList) {
        if (dataList == null) {
            return;
        }
        ExcelWriter<T> excelWriter = new AnnotationWriter<T>((Class<T>) dataList.get(0).getClass());

        Workbook wb = writeExcel(dataList, excelWriter);
        // 最后一步，将文件存到指定位置
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(filename);
            wb.write(fout);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fout != null)
                    fout.close();
            } catch (Exception e) {
                //no op
            }
        }
    }

    /**
     * 导出使用xlsx格式
     *
     * @param filename
     * @param dataList
     */
    public <T> void writeExcel(String filename, List<T> dataList, ExcelWriter<T> excelWriter) {
        if (dataList == null) {
            return;
        }
        Workbook wb = writeExcel(dataList, excelWriter);
        // 最后一步，将文件存到指定位置
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(filename);
            wb.write(fout);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fout != null)
                    fout.close();
            } catch (Exception e) {
                //no op
            }
        }
    }

    /**
     * 导出使用xls格式，由于ie8的兼容性问题
     *
     * @param dataList
     */
    public <T> Workbook writeExcel(List<T> dataList, ExcelWriter<T> excelWriter) {
        if (dataList == null) {
            return null;
        }
        //1.创建Excel工作薄对象
        Workbook wb = new HSSFWorkbook();
        int sheetIdx = 1;

        boolean needMoreSheet = dataList.size() > customPageDataSize;

        if (!needMoreSheet) {
            writeSheet(dataList, excelWriter, wb, sheetIdx);
            return wb;
        }

        if (!autoCreateNewSheet) {
            throw new RuntimeException("数据总条数超过每个sheet允许的最大个数:" + customPageDataSize);
        }
        //分页写数据到不同的sheet里面
        int start = 0;
        do {
            int end = start + customPageDataSize;
            if (end > dataList.size()) {
                end = dataList.size();
            }
            System.out.println("start:" + start + "  end:" + end);
            writeSheet(dataList.subList(start, end), excelWriter, wb, sheetIdx);
            sheetIdx++;
            start += customPageDataSize;
        } while (start < dataList.size());

        //设置单元格内容   cell.setCellValue("单元格内容");
        return wb;
    }

    private <T> void writeSheet(List<T> dataList, ExcelWriter<T> excelWriter, Workbook wb, int sheetIdx) {
        //1.创建Excel工作表对象
        Sheet sheet = wb.createSheet(MessageFormat.format(Default_Sheet_Name_Format, sheetIdx));
        //2.创建Excel工作表的行
        int headerRowIdx = 0;
        //从第一行开始写内容
        int contentRowIdx = headerRowIdx + 1;
        //从第一行开始写数据
        ConvertContext convertContext = new ConvertContext();
        for (T data : dataList) {
            excelWriter.setRow(sheet.createRow(contentRowIdx));
            excelWriter.writeRow(data, convertContext);
            contentRowIdx++;
        }
        //最后写header
        Row header = sheet.createRow(headerRowIdx);
        excelWriter.setRow(header);
        for (String headerValue : excelWriter.getIdxMap().keySet()) {
            excelWriter.addCell(headerValue, headerValue);
        }
    }


    /**
     * 解析header头，获取每个header对应的位置
     *
     * @param sheet
     */
    private void resolveHeader(Sheet sheet) {
        Row header = sheet.getRow(sheet.getFirstRowNum());
        for (int i = 0; i < header.getLastCellNum(); i++) {
            Cell cell = header.getCell(i);
            String h = cell.getStringCellValue();
            idxMap.put(h.trim(), i);
        }
    }

    /**
     * 解析单元格数据，返回list列表
     *
     * @param clazz
     * @param excelTranslator
     * @param sheet
     * @return
     */
    private <T> List<T> genResultList(Class<T> clazz, ExcelResolver<T> excelTranslator, Sheet sheet) {
        List<T> results = new ArrayList<T>();
        T obj;
        Row row;

        ConvertContext convertContext = new ConvertContext();

        //遍历每一行，知道最好一会
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            try {
                //获取用户自定义对象，如果为空，则调用
                obj = excelTranslator.customInitialiseObj();
                if (obj == null) {
                    obj = clazz.newInstance();
                }
                excelTranslator.setRow(row);
                if (excelTranslator.resolve(obj, convertContext)) {
                    results.add(obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return results;
    }

}
