package com.zeb.water_monitor.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeb.water_monitor.common.CustomException;
import com.zeb.water_monitor.entity.User;
import com.zeb.water_monitor.enums.RoleEnum;
import com.zeb.water_monitor.mapper.UserMapper;
import com.zeb.water_monitor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zeb
 * @since 2023-05-06
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public void editPassword(String originPassword, String newPassword, Integer id) {
        User user = this.getById(id);
        if (user == null) {
            throw new CustomException("用户异常");
        }
        if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(originPassword.getBytes()))) {
            throw new CustomException("密码错误");
        }
        user.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        log.info("user：" + user);
        this.saveOrUpdate(user);
    }

    @Override
    public void register(User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        User one = this.getOne(lambdaQueryWrapper);
        if (one != null) {
            throw new CustomException("用户名" + one.getUsername() + "已经存在");
        }
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        //设置为普通用户
        user.setRole(RoleEnum.USER);
        this.save(user);
    }
}
