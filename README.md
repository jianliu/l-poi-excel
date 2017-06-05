基于apach poi的excel快速读写小工具
===
## l-poi-excel
### 基于poi框架实现的excel读写工具


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

        ExcelHelper<Integer> excelHelper = new ExcelHelper<Integer>(10000, true);
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
//        new File(filepath).delete();
        long start = System.currentTimeMillis();

        ExcelHelper<Data> excelHelper = new ExcelHelper<Data>();

        List<Data> data =  excelHelper.readExcel(filepath, Data.class, new ExcelResolver<Data>() {
            @Override
            public boolean resolve(Data data) {
                data.setId(Integer.valueOf(getCellValue("ID")));
                return true;
            }
        },2);

        System.out.println("size：" +data.size()+ " ms");
        System.out.println("共耗时：" + (System.currentTimeMillis() - start) + " ms");
    }

```
