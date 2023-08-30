package com.zeb.water_monitor.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zeb.water_monitor.common.BaseContext;
import com.zeb.water_monitor.common.CustomException;
import com.zeb.water_monitor.common.Result;
import com.zeb.water_monitor.entity.Station;
import com.zeb.water_monitor.entity.User;
import com.zeb.water_monitor.entity.WaterQuality;
import com.zeb.water_monitor.enums.RoleEnum;
import com.zeb.water_monitor.service.StationService;
import com.zeb.water_monitor.service.UserService;
import com.zeb.water_monitor.service.WaterQualityService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author zeb
 * @since 2023-05-21
 */
@RestController
@RequestMapping("/station")
public class StationController {

    @Autowired
    private StationService stationService;

    @Autowired
    private WaterQualityService waterQualityService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "查询指定区域监测船", notes = "注意：获取监测船的接口之前写过，但此接口可以返回监测船的id，用于修改和删除操作；根据监测区域获取区域下所有监测船；area为监测区域，不传值代表查询所有区域")
    @GetMapping("/list")
    public Result<List> getAllStations(String area) {

        LambdaQueryWrapper<Station> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(area != null && !"".equals(area), Station::getArea, area);
        List<Station> list = stationService.list(wrapper);

        return Result.success(list);

    }

    @ApiOperation(value = "添加监测船", notes = "无需传输id")
    @PostMapping("/add")
    public Result<String> addStation(@RequestBody Station station) {
        stationService.save(station);
        return Result.success("添加成功");
    }

    @ApiOperation(value = "删除监测船", notes = "根据监测船id进行删除")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteArea(@PathVariable("id") Integer id) {
        Station station = stationService.getById(id);

        if (station == null) {
            throw new CustomException("无此id数据");
        }

        //查看水质数据表中是否有该监测船
        LambdaQueryWrapper<WaterQuality> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(station.getName() != null, WaterQuality::getStation, station.getName());
        List<WaterQuality> list = waterQualityService.list(wrapper);

        if (list != null && !list.isEmpty()) {
            throw new CustomException("删除失败，请先删除或修改水质数据表中监测船为" + station.getName() + "的字段");
        }
        stationService.removeById(id);
        return Result.success("删除成功");
    }

    @ApiOperation(value = "修改监测船", notes = "根据id修改监测船名称、所属的监测区域")
    @PutMapping("/edit")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> editArea(@RequestBody Station station) {
        Integer userId = BaseContext.getCurrentId();
        User user = userService.getById(userId);

        if (user.getRole() == RoleEnum.USER) {
            return Result.error("用户权限不足");
        }

        Station byId = stationService.getById(station.getId());
        if (byId == null) {
            throw new CustomException("无此id数据");
        }

        //修改station表中的数据
        stationService.updateById(station);

        //修改水质数据表中的数据，可改成UpdateWrapper
        LambdaQueryWrapper<WaterQuality> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(station.getName() != null, WaterQuality::getArea, byId.getName());
        List<WaterQuality> list = waterQualityService.list(wrapper);
        list.stream().map(item -> {
            item.setArea(station.getArea());
            item.setStation(station.getName());
            waterQualityService.updateById(item);
            return item;
        }).collect(Collectors.toList());

        return Result.success("修改成功");
    }
}
