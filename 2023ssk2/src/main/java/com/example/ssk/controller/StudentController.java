package com.example.ssk.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ssk.common.Result;
import com.example.ssk.entity.Student;
import com.example.ssk.entity.User;
import com.example.ssk.mapper.StudentMapper;
import com.example.ssk.service.IStudentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-03-02
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private IStudentService studentService;

    @Resource
    private StudentMapper studentMapper;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Student student) {
        if(student.getId()==null){
            studentMapper.insert(student);
        }
        else {
            studentMapper.updateById(student);
        }
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        studentService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        studentService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(studentService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(studentService.getById(id));
    }

    @PostMapping("/studentinfo/{id}")
    public Result Info(@PathVariable Integer id) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("uid",id);
        return Result.success(studentService.list(queryWrapper));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam (defaultValue = "")String name,
                           @RequestParam (defaultValue = "")String address,
                           @RequestParam (defaultValue = "")String stuid
    ) {
//        IPage<Student> page=new Page<>(pageNum,pageSize);
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        if(!"".equals(name)){
            queryWrapper.like("name",name);
        }
        if(!"".equals(address)){
            queryWrapper.like("address",address);
        }
        if(!"".equals(stuid)){
            queryWrapper.like("stuid",stuid);
        }
        return Result.success(studentService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

