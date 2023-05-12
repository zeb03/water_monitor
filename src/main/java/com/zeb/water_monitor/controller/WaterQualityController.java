package com.zeb.water_monitor.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zeb.water_monitor.common.BaseContext;
import com.zeb.water_monitor.common.CustomException;
import com.zeb.water_monitor.common.Result;
import com.zeb.water_monitor.dto.AddWaterQualityDTO;
import com.zeb.water_monitor.dto.WaterQualityQuery;
import com.zeb.water_monitor.entity.User;
import com.zeb.water_monitor.entity.WaterQuality;
import com.zeb.water_monitor.enums.RoleEnum;
import com.zeb.water_monitor.service.UserService;
import com.zeb.water_monitor.service.WaterQualityService;
import com.zeb.water_monitor.vo.PlotVO;
import com.zeb.water_monitor.vo.WaterQualityPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zeb
 * @since 2023-05-06
 */
@Slf4j
@RestController
@Api
@RequestMapping("/water/quality")
public class WaterQualityController {

    @Autowired
    private WaterQualityService waterQualityService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "分页查询水质数据", notes = "可以根据时间(yyyy-MM-dd）范围、站台条件分页查询")
    @GetMapping("/query")
    public Result<WaterQualityPageVO> getQueriedWaterQualities(WaterQualityQuery waterQualityQuery) {
        log.info("水质查询条件:" + waterQualityQuery);

        Integer station = waterQualityQuery.getStation();
        LocalDate startDate = waterQualityQuery.getStartDate();
        LocalDate endDate = waterQualityQuery.getEndDate();

        LambdaQueryWrapper<WaterQuality> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(station != null, WaterQuality::getStation, station);

        wrapper.ge(startDate != null, WaterQuality::getDate, startDate + " 00:00:00");

        wrapper.le(endDate != null, WaterQuality::getDate, endDate + " 23:59:59");

        wrapper.orderByDesc(WaterQuality::getDate);

        Page<WaterQuality> pageInfo = new Page<>(waterQualityQuery.getPageNum(), waterQualityQuery.getPageSize());
        Page<WaterQuality> page = waterQualityService.page(pageInfo, wrapper);

        log.info("page:" + page);

        WaterQualityPageVO waterQualityPageVO = new WaterQualityPageVO();
        waterQualityPageVO.setRecords(page.getRecords());
        waterQualityPageVO.setTotal(page.getTotal());

        return Result.success(waterQualityPageVO);
    }

    @ApiOperation(value = "查询所有水质数据", notes = "查询所有水质数据")
    @GetMapping("/all")
    public Result<List<WaterQuality>> getAllWaterQualities() {

        List<WaterQuality> waterQualities = waterQualityService.list();
        return Result.success(waterQualities);
    }

    @ApiOperation(value = "查询当日水质数据", notes = "")
    @GetMapping("/day")
    public Result<List<WaterQuality>> getWaterQualitiesByDate(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("day") LocalDate day) {
        log.info("day:" + day);
        LambdaQueryWrapper<WaterQuality> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(day != null, WaterQuality::getDate, day + " 00:00:00");
        wrapper.le(day != null, WaterQuality::getDate, day + " 23:59:59");
        List<WaterQuality> list = waterQualityService.list(wrapper);
        log.info("list:" + list);
        return Result.success(list);
    }

    @ApiOperation(value = "修改水质数据", notes = "根据id修改水质数据")
    @PutMapping("/edit")
    public Result<String> editWaterQuality(@RequestBody WaterQuality waterQuality) {

        Integer userId = BaseContext.getCurrentId();
        log.info("userId:" + userId);
        User user = userService.getById(userId);

        if (user == null) {
            throw new CustomException("用户未登录");
        }
        if (user.getRole() == RoleEnum.USER) {
            return Result.error("用户权限不足");
        }

        if (waterQualityService.getById(waterQuality.getId()) == null) {
            throw new CustomException("id错误");
        }

        waterQuality.setDate(LocalDateTime.now());

        if (!waterQualityService.updateById(waterQuality)) {
            return Result.error("修改失败，请联系管理员");
        }

        return Result.success("修改成功");
    }

    @ApiOperation(value = "删除水质数据", notes = "根据id删除水质数据")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteWaterQuality(@PathVariable("id") Integer id) {
        Integer userId = BaseContext.getCurrentId();
        User user = userService.getById(userId);
        if (user == null) {
            throw new CustomException("用户未登录");
        }
        if (user.getRole() == RoleEnum.USER) {
            return Result.error("用户权限不足");
        }

        if (waterQualityService.getById(id) == null) {
            throw new CustomException("id错误");
        }

        if (!waterQualityService.removeById(id)) {
            return Result.error("删除失败，请联系管理员");
        }

        return Result.success("删除成功");
    }

    @ApiOperation(value = "查询所有监测站", notes = "查询所有监测站：1、2、3、4、5...")
    @GetMapping("/station")
    public Result<List<Integer>> getAllStations() {

        QueryWrapper<WaterQuality> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT station").lambda().orderByAsc(WaterQuality::getStation);

        List<WaterQuality> list = waterQualityService.list(wrapper);
        List<Integer> stations = list.stream().map(WaterQuality::getStation).collect(Collectors.toList());

        return Result.success(stations);
    }

    @GetMapping("/plot")
    @ApiOperation(value = "获取数据示意图", notes = "根据监测点、时期、监测指标获取示意图，period的单位为月，例如period=2表示最近2个月，由于当前数据库无真实数据，故假设当前时间为2006-6-13；indicator为监测指标，如PH、temperature")
    public Result<PlotVO> getDataForPlot(@RequestParam(value = "station") Integer station,
                                         @RequestParam(value = "period") Integer period,
                                         @RequestParam(value = "indicator") String indicator) {
        PlotVO plotVO = waterQualityService.getDataForPlot(station, period, indicator);
        return Result.success(plotVO);
    }

    @GetMapping("/recent")
    @ApiOperation(value = "查看最近数据", notes = "查看最近num条数据，num不可为空")
    public Result<List<WaterQuality>> getRecentWaterQualities(@RequestParam(value = "num") Integer num) {
        if (num == 0) {
            throw new CustomException("num不能为空");
        }
        LambdaQueryWrapper<WaterQuality> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(WaterQuality::getDate);
        wrapper.last("limit " + num);
        List<WaterQuality> list = waterQualityService.list(wrapper);
        return Result.success(list);
    }

    @ApiOperation(value = "添加水质数据", notes = "添加水质数据")
    @PostMapping("/add")
    public Result<String> addWaterQuality(@RequestBody AddWaterQualityDTO addWaterQualityDTO) {
        WaterQuality waterQuality = new WaterQuality();
        BeanUtils.copyProperties(addWaterQualityDTO, waterQuality);
        waterQuality.setDate(LocalDateTime.now());
        if (!waterQualityService.save(waterQuality)) {
            throw new CustomException("添加失败");
        }
        return Result.success("添加成功");
    }

    @ApiOperation(value = "获取水质指标参数", notes = "如ph、temperature等")
    @GetMapping("/indicator")
    public Result<List<String>> getAllIndicators() {
        ArrayList<String> indicators = new ArrayList<>();
        indicators.add("ph");
        indicators.add("temperature");
        indicators.add("conductivity");
        indicators.add("turbidity");
        indicators.add("tds");
        return Result.success(indicators);
    }
}
