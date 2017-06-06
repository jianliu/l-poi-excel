基于apach poi Excel读写框架的excel快速读写小工具
===
## l-poi-excel
### 基于poi框架实现的excel读写工具
poi是一个开源的文档操作框架，支持且不仅支持从excel文件读取单元格数据，同时也支持写数据到excel中去，google一下，你就知道

[poi传送门](https://github.com/apache/poi)

在poi操作excel文件的api，包装了一层高效的api，使用者可以基于一个接口做如下事：
* 从excel中获取每一行数据，转换为java对象（仅支持bean\pojo的转换，不支持原始类型及其封装类型，如Integer,String）
* 将java对象转换成excel的一行,写入sheet中
* 内部在写文件的时候，会默认在每个excel sheet中最大写入65535行数据，超出则会自动写入到下一个sheet中。


### 涉及到的maven依赖包
```java
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>3.7</version>
</dependency>
```
## 读写文件
### 写excel文件
```java
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
```

### 读excel文件
```java
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
```
