package com.zeb.water_monitor.server;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.zeb.water_monitor.dto.ExcelModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zeb
 * @Date 2024-02-18 20:42
 */
public class ExcelListener extends AnalysisEventListener<ExcelModel> {

    private final List<ExcelModel> datas = new ArrayList<>();
    private static final int BATCH_COUNT = 3000;

    @Override
    public void invoke(ExcelModel excelModel, AnalysisContext analysisContext) {//此方法一行一行的读取Excel内容
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (datas.size() >= BATCH_COUNT) {
//            saveData();
            // 存储完成清理 list
            datas.clear();
        }
        datas.add(excelModel);//数据存储到list，供批量处理，或后续自己业务逻辑处理。
    }

    public void doAfterAllAnalysed(AnalysisContext context) {
        // datas.clear();//解析结束销毁不用的资源，也可待数据入库后在进行销毁，根据具体业务需求自行更改
    }

    public List<ExcelModel> getDatas() { //把存储的数据通过此方法暴露出去
        return datas;
    }
}