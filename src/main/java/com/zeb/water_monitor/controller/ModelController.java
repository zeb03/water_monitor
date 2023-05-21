package com.zeb.water_monitor.controller;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zeb
 * @since 2023-05-09
 */
@RestController
@RequestMapping("/model")
@Api
public class ModelController {

//    @Autowired
//    private ModelService modelService;
//
//    @ApiOperation(value = "查询数据模型", notes = "根据指标进行查询，如ph、temperature等")
//    @GetMapping("/list")
//    public Result<List<Model>> getAllModelByIndicator(@RequestParam(value = "indicator") String indicator) {
//        indicator = indicator.toLowerCase();
//        LambdaQueryWrapper<Model> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Model::getTarget,indicator);
//        List<Model> list = modelService.list(wrapper);
//        return Result.success(list);
//    }
//
//    @ApiOperation(value = "删除数据模型", notes = "根据id进行删除")
//    @DeleteMapping("/delete/{id}")
//    public Result<String> deleteModel(@PathVariable int id) {
//        if (!modelService.removeById(id)) {
//            return Result.error("删除失败，请联系管理员");
//        }
//        return Result.success("删除成功");
//    }
//
//    @GetMapping("/prediction")
//    public Result<Prediction> predictNextMonth(@RequestParam(value = "id") Integer id,
//                                               @RequestParam(value = "indicator") String indicator) {
//        Prediction prediction = modelService.predictNextMonth(id, indicator);
//        return Result.success(prediction);
//    }

}
