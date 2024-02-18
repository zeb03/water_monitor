package com.zeb.water_monitor.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zeb.water_monitor.common.BaseContext;
import com.zeb.water_monitor.common.CustomException;
import com.zeb.water_monitor.common.Result;
import com.zeb.water_monitor.dto.UserLoginDTO;
import com.zeb.water_monitor.entity.User;
import com.zeb.water_monitor.enums.RoleEnum;
import com.zeb.water_monitor.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author z
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "登录", notes = "登录功能，管理员账户及密码：")
    @PostMapping("/login")
    public Result<User> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        log.info("Login");
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, userLoginDTO.getUsername())
                .last("limit 1");
        User userInfo = userService.getOne(wrapper);
        if (userInfo == null) {
            throw new CustomException("用户不存在");
        }
        String password = DigestUtils.md5DigestAsHex(userLoginDTO.getPassword().getBytes());
        if (!userInfo.getPassword().equals(password)) {
            throw new CustomException("密码错误");
        }

        //      登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().getServletContext().setAttribute("user", userInfo.getId());
        log.info("登录成功:" + userInfo.getId());
        return Result.success(userInfo);
    }

    @ApiOperation(value = "修改密码", notes = "修改密码")
    @PutMapping("/editPassword")
    public Result<String> editPassword(@RequestParam("originPassword") String originPassword, @RequestParam("newPassword") String newPassword, @RequestParam("id") Integer id) {
        userService.editPassword(originPassword, newPassword, id);
        return Result.success("密码修改成功");
    }

    @ApiOperation(value = "获取所有用户", notes = "获取所有用户")
    @GetMapping("/list")
    public List<User> getAllUsers() {
        return userService.list();
    }

    @ApiOperation(value = "修改用户权限", notes = "")
    @PostMapping("/grant/{id}")
    public Result<String> grantAuthority(@PathVariable int id, RoleEnum role) {
        Integer currentId = BaseContext.getCurrentId();
        User byId = userService.getById(currentId);
        if (!"admin".equals(byId.getUsername())) {
            return Result.error("修改失败，当前用户权限不足");
        }
        User user = userService.getById(id);
        user.setRole(role);
        if (!userService.updateById(user)) {
            throw new CustomException("修改失败，用户id错误");
        }
        return Result.success("成功修改用户角色");
    }

    @ApiOperation(value = "注销用户", notes = "注销用户")
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Integer id) {
        if (!userService.removeById(id)) {
            throw new CustomException("注销失败，用户id错误");
        }
        return Result.success("注销成功");
    }

    @ApiOperation(value = "退出登录", notes = "退出登录")
    @GetMapping("/logout")
    public Result<String> loginOut(HttpServletRequest request) {
        //清理Session中保存的当前用户登录的id
        request.getSession().removeAttribute("user");
        BaseContext.removeThreadLocal();
        return Result.success("退出成功");
    }

    @ApiOperation(value = "注册用户", notes = "注册用户")
    @PostMapping("/register")
    public Result<String> register(String username, String password) {
        log.info("user: " + username + password);
        userService.register(new User(username, password));
        return Result.success("注册用户成功");
    }

    @ApiOperation(value = "根据名称查找用户", notes = "根据名称查找用户")
    @GetMapping("/query")
    public Result<List<User>> getQueriedUsers(@RequestParam("name") String name) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getUsername, name);
        lambdaQueryWrapper.orderByAsc(User::getId);
        List<User> users = userService.list(lambdaQueryWrapper);
        if (users == null) {
            throw new CustomException("获取用户失败，请联系管理员");
        }
        return Result.success(users);
    }

}
