package com.jianliu.excel;

import com.jianliu.excel.annotation.Column;

/**
 * class ConvertContenxt
 * 类型转换上下文
 * @author jianliu
 * @since 2019/6/17
 */
public class ConvertContext {

    private Column column;


    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }
}
