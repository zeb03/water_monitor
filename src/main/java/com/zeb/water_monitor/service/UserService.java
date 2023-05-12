package com.zeb.water_monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeb.water_monitor.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zeb
 * @since 2023-05-06
 */
public interface UserService extends IService<User> {

    /**
     * 修改密码
     * @param originPassword
     * @param newPassword
     * @param id
     */
    void editPassword(String originPassword, String newPassword, Integer id);

    /**
     * 注册用户
     * @param user
     */
    void register(User user);
}
