package com.zeb.water_monitor.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zeb.water_monitor.dto.ExcelModel;
import com.zeb.water_monitor.entity.WaterQuality;
import com.zeb.water_monitor.server.ExcelListener;
import com.zeb.water_monitor.service.WaterQualityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zeb
 * @Date 2024-02-15 15:38
 */
@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private WaterQualityService waterQualityService;

    /**
     * 导出所有水质数据
     *
     * @param response
     */
    @GetMapping("/export/all/water-data")
    public void exportAllData(HttpServletResponse response) {
        try {
            this.setExcelResponseProp(response, "水质数据列表");
            List<WaterQuality> waterQualityList = waterQualityService.list();
            EasyExcel.write(response.getOutputStream())
                    .head(WaterQuality.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("水质数据列表")
                    .doWrite(waterQualityList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/export/water-data")
    public void exportData(HttpServletResponse response, @RequestParam("start") LocalDateTime start, @RequestParam("end") LocalDateTime end) {
        try {
            this.setExcelResponseProp(response, "水质数据列表");
            LambdaQueryWrapper<WaterQuality> wrapper = Wrappers.lambdaQuery(WaterQuality.class).ge(WaterQuality::getDate, start).le(WaterQuality::getDate, end);
            List<WaterQuality> waterQualityList = waterQualityService.list(wrapper);
            EasyExcel.write(response.getOutputStream())
                    .head(WaterQuality.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("水质数据列表")
                    .doWrite(waterQualityList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置响应结果
     *
     * @param response    响应结果对象
     * @param rawFileName 文件名
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    private void setExcelResponseProp(HttpServletResponse response, String rawFileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(rawFileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }

    @RequestMapping("uploadExcel")
    @ResponseBody
    public void uploadExcel(MultipartFile file) throws IOException {
        log.info("file: " + file);
        ExcelListener listener = new ExcelListener();
        EasyExcel.read(file.getInputStream(), ExcelModel.class, listener).sheet(0).doReadSync();  //ExcelModel 上面创建的实体类
        System.out.println("解析完之后的数据：---" + listener.getDatas());
        List<ExcelModel> datas = listener.getDatas();
//        baseService.insertExcelService(datas);//入库
        log.info("datas: " + datas);
        datas.clear();//结束后销毁不用的资源
        System.out.println(datas); //查看销毁后的结果
    }

}
