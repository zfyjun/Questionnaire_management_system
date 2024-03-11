package com.example.ssk.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ssk.common.Constants;
import com.example.ssk.controller.dto.UserDto;
import com.example.ssk.entity.Code;
import com.example.ssk.entity.Student;
import com.example.ssk.mapper.CodeMapper;
import com.example.ssk.mapper.StudentMapper;
import com.example.ssk.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.ssk.common.Result;
import com.example.ssk.service.IUserService;
import com.example.ssk.entity.User;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-03-02
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;
    @Resource
    private StudentMapper studentMapper;

    @Resource
    private CodeMapper codeMapper;

    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto){
        String account=userDto.getAccount();
        String password=userDto.getPassword();
        if(StrUtil.isBlank(account)||StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDto dto=userService.login(userDto);
        return Result.success(dto);
    }
    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody User user) {
        userService.saveOrUpdate(user);
        return Result.success();
    }

    @Autowired
    private UserMapper userMapper;


    @PostMapping("loginbyphone")
    public Result loginbyphone(@RequestBody Map map){//设置并发送验证码
        String phone=(String) map.get("phone");
        String codenumber=(String)map.get("code");
        Student student=studentMapper.selectOne(Wrappers.<Student>lambdaQuery().eq(Student::getPhone,phone));
        Integer id=student.getUid();
        Code code=codeMapper.selectById(id);
        System.out.println(codenumber+"  "+code.getPCode());
        if(codenumber.equals(code.getPCode())){
            User user=userMapper.selectById(id);
            return Result.success(user);
        }
        return Result.error("500","验证码错误！");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        userService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        userService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        System.out.println(userService.list());
        return Result.success(userService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }
}

