package com.window.system.common;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class InstallProgressConverter implements Converter<String> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public String convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        return cellData.getStringValue();
    }

    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        String label = value;
        if ("WAITING".equals(value)) label = "等待中";
        else if ("SCHEDULED".equals(value)) label = "已排期";
        else if ("INSTALLING".equals(value)) label = "安装中";
        else if ("FINISHED".equals(value)) label = "已完成";
        return new WriteCellData<>(label);
    }
}
