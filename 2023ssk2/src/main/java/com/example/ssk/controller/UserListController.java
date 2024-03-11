package com.example.ssk.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ssk.common.Result;
import com.example.ssk.entity.UserList;
import com.example.ssk.mapper.UserListMapper;
import com.example.ssk.service.IUserListService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-03-07
 */
@RestController
@RequestMapping("/userList")
public class UserListController {

    @Resource
    private IUserListService userListService;

    @Resource
    private UserListMapper userListMapper;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody UserList userList) {
        userListService.saveOrUpdate(userList);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        userListService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        userListService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userListService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userListService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        QueryWrapper<UserList> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(userListService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @PostMapping("/update")
    public Result Update(@RequestBody UserList userList) {
        Integer uid=userList.getUid();
        Integer lid=userList.getLid();
        UserList userList0=userListMapper.selectOne(Wrappers.<UserList>lambdaQuery().eq(UserList::getUid,uid).eq(UserList::getLid,lid));
        userList0.setAnswer(userList.getAnswer());
        userList0.setTime(LocalDateTime.now());
        userList0.setState("已参与");
        userListMapper.updateById(userList0);
        return Result.success();
    }

}

