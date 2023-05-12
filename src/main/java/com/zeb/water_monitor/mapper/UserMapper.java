package com.zeb.water_monitor.mapper;

import com.zeb.water_monitor.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zeb
 * @since 2023-05-06
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
