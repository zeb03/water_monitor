package com.zeb.water_monitor.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zeb.water_monitor.common.BaseContext;
import com.zeb.water_monitor.common.CustomException;
import com.zeb.water_monitor.common.Result;
import com.zeb.water_monitor.entity.Area;
import com.zeb.water_monitor.entity.User;
import com.zeb.water_monitor.entity.WaterQuality;
import com.zeb.water_monitor.enums.RoleEnum;
import com.zeb.water_monitor.service.AreaService;
import com.zeb.water_monitor.service.UserService;
import com.zeb.water_monitor.service.WaterQualityService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zeb
 * @since 2023-05-21
 */
@Slf4j
@RestController
@RequestMapping("/area")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @Autowired
    private UserService userService;

    @Autowired
    private WaterQualityService waterQualityService;

    @ApiOperation(value = "查询所有区域")
    @GetMapping("/list")
    public Result<List> getAllAreas() {
        List<Area> list = areaService.list();
        return Result.success(list);
    }

    @ApiOperation(value = "添加区域", notes = "只需传输区域名称参数name即可")
    @PostMapping("/add")
    public Result<String> addArea(@RequestBody Area area) {
        areaService.save(area);
        return Result.success("添加成功");
    }

    @ApiOperation(value = "删除区域", notes = "根据id进行删除")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteArea(@PathVariable("id") Integer id) {
        //通过id获取区域
        Area area = areaService.getById(id);
        if (area == null) {
            throw new CustomException("无此id数据");
        }

        //查看水质数据表中是否有改区域
        LambdaQueryWrapper<WaterQuality> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(area.getName() != null, WaterQuality::getArea, area.getName());
        List<WaterQuality> list = waterQualityService.list(wrapper);

        log.info("list:" + list);
        if (list != null && !list.isEmpty()) {
            throw new CustomException("删除失败，请先删除或修改水质数据表中区域名为" + area.getName() + "的字段");
        }
        areaService.removeById(id);
        return Result.success("删除成功");
    }

    @ApiOperation(value = "修改区域", notes = "")
    @PutMapping("/edit")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> editArea(@RequestBody Area area) {
        Integer userId = BaseContext.getCurrentId();
        User user = userService.getById(userId);

        if (user.getRole() == RoleEnum.USER) {
            return Result.error("用户权限不足");
        }

        Area byId = areaService.getById(area.getId());
        if (byId == null) {
            throw new CustomException("无此id数据");
        }

        //修改area表中的数据
        areaService.updateById(area);

        //修改水质数据表中的数据，可改成UpdateWrapper
        LambdaQueryWrapper<WaterQuality> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(area.getName() != null, WaterQuality::getArea, byId.getName());
        List<WaterQuality> list = waterQualityService.list(wrapper);
        log.info("list:" + list);
        list.stream().map(item -> {
            item.setArea(area.getName());
            waterQualityService.updateById(item);
            return item;
        }).collect(Collectors.toList());

        return Result.success("修改成功");
    }
}
