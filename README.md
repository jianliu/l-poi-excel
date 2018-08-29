基于apach poi Excel读写框架的excel快速读写小工具
===
## l-poi-excel
### 基于poi框架实现的excel读写工具
poi是一个开源的文档操作框架，支持且不仅支持从excel文件读取单元格数据，同时也支持写数据到excel中去，google一下，你就知道

[poi传送门](https://github.com/apache/poi)

此工具目前仅支持规则的excel读取和写入

在poi操作excel文件的api，包装了一层高效的api，使用者可以基于一个接口做如下事：
* 从excel中获取每一行数据，转换为java对象（仅支持bean\pojo的转换，不支持原始类型及其封装类型，如Integer,String）
* 将java对象转换成excel的一行,写入sheet中
* 内部在写文件的时候，会默认在每个excel sheet中最大写入65535行数据，超出则会自动写入到下一个sheet中。
* 支持注解根据列的title自动识别列功能

### 适用场景
* 规则的excel文件，不存在一个cell横跨多列或多行
* 由于实现使用的是poi的用户模式，会将excel一次性读到内存中，因此适用于读取not large的excel文件

### 注解功能
* 支持基于自定义代码的解析、读取，两行代码即可实现写入、读取 
* 支持基于注解的自动解析、读取,自动识别基本封装类型如Integer\String\... 和Date类型，用户可以自定义转换器，实现单个cell解析为复杂对象的功能  

### 引入方式 maven依赖包
```java
<dependency>
    <groupId>com.github.jianliu</groupId>
    <artifactId>excel-poi</artifactId>
    <version>1.1</version>
</dependency>
```

## 读写文件
实体类

```java
         /**
          * 测试实体
          */
         public class Data {

             //@Column(value = "ID", order = 0),自动转换原始类型及其封装类型、Date类型
             private int id;

             //复杂对象自定义转换器,框架会自动生成器转换器实例
             @Column(value = "ID", order = 0, typeConvertor = SimpleDataConvertor.class) 
             private SimpleData simpleData;

             public Integer getId() {
                 return id;
             }

             public void setId(Integer id) {
                 this.id = id;
             }
         }
```


### 写excel文件

通过注解，两行代码即可写入数据列表到excel中

```java
    @Test
    public void write() {
        String filepath = "E:" + "/" + "l-test-write.xls";
        new File(filepath).delete();
        long start = System.currentTimeMillis();

        //模拟数据
        List<Data> data = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Data d = new Data();
            d.setId(i);
            data.add(d);
        }


        ExcelHelper excelHelper = new ExcelHelper(10000, true);
        //基于注解自动写入
        excelHelper.writeExcel(filepath, data);

        //手动写入
        //  excelHelper.writeExcel(filepath, data, new ExcelWriter<Data>() {
        //            @Override
        //            public void writeRow(Data data) {
        //                addCell("ID", data.getId);
        //            }
        //        });

        System.out.println("共耗时：" + (System.currentTimeMillis() - start) + " ms");
    }
```

### 读excel文件

基于注解，两行代码即可获取数据列表

```java
    @Test
    public void read() throws IOException, InvalidFormatException, NoSuchFieldException, IllegalAccessException {
        String filepath = "E:" + "/" + "l-test-write.xls";
        //new File(filepath).delete();
        long start = System.currentTimeMillis();

        ExcelHelper excelHelper = new ExcelHelper();
        //基于注解自动解析
        List<Data> dataListFromExcel = excelHelper.readExcel(new FileInputStream(filepath), Data.class, 0);

        //自定义Resolver，手动对应列，可以在不依赖任何转换器的情况下为Data赋值
        /**
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

        */

        System.out.println("size：" + dataListFromExcel.size() + " ms");
        System.out.println("共耗时：" + (System.currentTimeMillis() - start) + " ms");
    }
```
