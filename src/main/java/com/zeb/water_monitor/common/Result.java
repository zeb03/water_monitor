package com.zeb.water_monitor.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统一返回结果类
 * @author zeb
 * @param <T>
 */
@Data
@ApiModel("统一返回对象")
public class Result<T> {

    /**
     * 编码：200成功，其它数字为失败
     */
    @ApiModelProperty("状态码")
    private Integer code;

    /**
     * 错误信息
     */
    @ApiModelProperty("错误信息")
    private String msg;

    /**
     * 数据
     */
    @ApiModelProperty("返回数据")
    private T data;

//    /**
//     * 动态数据
//     */
//    @ApiModelProperty("动态数据")
//    private Map map = new HashMap();

    public static <T> Result<T> success(T object) {
        Result<T> r = new Result<T>();
        r.data = object;
        r.code = 200;
        return r;
    }

    public static <T> Result<T> error(String msg) {
        Result r = new Result();
        r.msg = msg;
        r.code = 500;
        return r;
    }

    public static <T> Result<T> error(Integer code, String msg) {
        Result r = new Result();
        r.msg = msg;
        r.code = code;
        return r;
    }

//    public Result<T> add(String key, Object value) {
//        this.map.put(key, value);
//        return this;
//    }

}
