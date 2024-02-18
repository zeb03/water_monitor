package com.zeb.water_monitor.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

/**
 * @author zeb
 * @Date 2024-02-18 20:42
 */
@Scope("prototype")
@Data   //此注释需要导入lombok
@ContentRowHeight(25) //数据表格列高
@HeadRowHeight(20) //标题表格列高
@ColumnWidth(25) //标题表格列宽
public class ExcelModel  {


    @ExcelProperty(value = "id", index = 0)  //index表示读取的数据在第几列，根据业务需求自行对应
    private String id;

    @ExcelProperty(value = "ph", index = 1)
    private Float ph;

    @ExcelProperty(value = "温度", index = 2)
    private Float temperature;

    @ExcelProperty(value = "电导率", index = 3)
    private Float conductivity;

    @ExcelProperty(value = "浊度", index = 4)
    private Float turbidity;

    @ExcelProperty(value = "tds", index = 5)
    private Float tds;

    @ExcelProperty(value = "date", index = 6)
    private LocalDateTime date;

    @ExcelProperty(value = "station", index = 7)
    private String station;

    @ExcelProperty(value = "area", index = 8)
    private String area;

    @ExcelProperty(value = "lng", index = 9)
    private String lng;

    @ExcelProperty(value = "lat", index = 10)
    private String lat;
}
