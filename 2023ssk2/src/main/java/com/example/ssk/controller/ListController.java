package com.example.ssk.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ssk.common.Result;
import com.example.ssk.entity.Student;
import com.example.ssk.entity.User;
import com.example.ssk.entity.UserList;
import com.example.ssk.mapper.ListMapper;
import com.example.ssk.mapper.StudentMapper;
import com.example.ssk.mapper.UserListMapper;
import com.example.ssk.mapper.UserMapper;
import com.example.ssk.service.IListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/List")
public class ListController {
    @Resource
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private UserMapper userMapper;

    @Resource
    private ListMapper listMapper;

    @Resource
    private UserListMapper UserListMapper;

    @Resource
    private IListService listService;

    @GetMapping("/{id}")
    public Result findList(@PathVariable Integer id) {
        return Result.success(listMapper.selectById(id));
    }

//    @GetMapping("/batch")
//    public Result findbatchList(@RequestBody Integer ids[]) {
//        return Result.success(listMapper.selectList());
//    }

    @PostMapping
    public Result save(@RequestBody Integer id){
        com.example.ssk.entity.List list1=listMapper.selectById(id);
        list1.setState("已发布");
        listMapper.updateById(list1);
        List<User> list2 = userMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getType,"学生"));
        for(int i=0;i<list2.size();i++){
            UserList userList = new UserList();
            userList.setUid(list2.get(i).getId());
            userList.setLid(id);
            userList.setState("未参与");
            UserListMapper.insert(userList);
        }
        return Result.success();
    }

    @PostMapping("/DeleteFalse")
    public Result deleteFalse(@RequestBody Integer id){
        com.example.ssk.entity.List list1=listMapper.selectById(id);
        list1.setIsDelete(1);
        list1.setState("未发布");
        listMapper.updateById(list1);
        UserListMapper.delete(Wrappers.<UserList>lambdaQuery().eq(UserList::getLid,id));
        return Result.success();
    }

    @PostMapping("/recover")
    public Result Recover(@RequestBody Integer id){
        com.example.ssk.entity.List list1=listMapper.selectById(id);
        list1.setIsDelete(0);
//        list1.setState("未发布");
        listMapper.updateById(list1);
        UserListMapper.delete(Wrappers.<UserList>lambdaQuery().eq(UserList::getLid,id));
        return Result.success();
    }

    @DeleteMapping("/DeleteTrue/{id}")
    public Result deleteTrue(@PathVariable String id){
        return Result.success(listService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        listService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll(){
        return Result.success(listMapper.selectList(null));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<com.example.ssk.entity.List> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("is_delete",1);
        queryWrapper.orderByAsc("id");
        return Result.success(listService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/StudentList/{uid}")
    public Result findOne(@PathVariable Integer uid){
        return Result.success(UserListMapper.selectList(Wrappers.<UserList>lambdaQuery().eq(UserList::getUid,uid)));
    }

    @PostMapping("/sendlist")//校验用户名
    public Result sendlist(@RequestBody Map body ){//前端传后端
        Map body1=(HashMap)body.get("table");
        Map body2=(HashMap)body.get("address");

        Integer flag=stuIdCheckout((String) body1.get("stu_id"));
        if(flag==0){//不存在账号（学号没有用过）
            Student student=new Student();
            student.setName((String) body1.get("username"));
            student.setSex((String) body1.get("sex"));
            String sage=(String)body1.get("age");
            Integer age=Integer.parseInt(sage);
            student.setAge(age);
            student.setStuid((String) body1.get("stu_id"));
            String address=(String) body2.get("province")+" "+(String) body2.get("city");
            student.setAddress(address);
            studentMapper.insert(student);
            return Result.success();
        }
        else if(flag==1){//存在学号但没报道
            Student student=new Student();
            student.setName((String) body1.get("username"));
            student.setSex((String) body1.get("sex"));
            String sage=(String)body1.get("age");
            Integer age=Integer.parseInt(sage);
            student.setAge(age);
            String address=(String) body2.get("province")+" "+(String) body2.get("city");
            student.setAddress(address);
            studentMapper.update(student,(Wrappers.<Student>lambdaQuery().eq(Student::getStuid,(String) body1.get("stu_id"))));
            return Result.success();
        }
        return Result.error("400","错误：该学生已报道！请检查您的学号是否正确");
    }
    @PostMapping("/confirm")//验证用户名
    public Result confirm(@RequestBody String account){
        User user=userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getAccount,account));
        if(user!=null){
            return Result.error("400","账户名已存在！请更换");
        }
        return Result.success();
    }
    @PostMapping("/register")//注册
    public Result register(@RequestBody Map bady){
        Map form=(HashMap)bady.get("form");
        String stuId=(String) bady.get("stuId");
        //////
        Integer flag=stuIdCheckout(stuId);
        User user1=userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getAccount,(String) form.get("account")));
        if(user1!=null){
            return Result.error("400","账户名已存在！请更换");
        }
        if(flag!=0){//存在账号
            Student student=studentMapper.selectOne(Wrappers.<Student>lambdaQuery().eq(Student::getStuid,stuId));
            if(student.getUid()==null){
                User user=new User();
                user.setAccount((String) form.get("account"));
                user.setPassword((String) form.get("password"));
                String phone=(String) form.get("phone");
                if(form.get("phone")!=null){
                    if(phoneone(phone)){
                        student.setPhone((String) form.get("phone"));
                    }
                    else {
                        return Result.error("500","该电话号码已被注册！");
                    }
                }
                student.setEmail((String) form.get("email"));
                userMapper.insert(user);
                student.setUid(user.getId());
                studentMapper.updateById(student);
            }
            else {
                return Result.error("400","错误：该学号学生已注册账户");
            }
        }
        else {//不存在账号
            System.out.println("没有报道过");
            User user=new User();
            user.setAccount((String) form.get("account"));
            user.setPassword((String) form.get("password"));
            /////
            Student student=new Student();
            student.setStuid(stuId);
            //
            String phone=(String) form.get("phone");
            if(form.get("phone")!=null){
                if(phoneone(phone)){
                    student.setPhone((String) form.get("phone"));
                }
                else {
                    return Result.error("500","该电话号码已被注册！");
                }
            }
            student.setEmail((String) form.get("email"));

            userMapper.insert(user);
            student.setUid(user.getId());
            studentMapper.insert(student);
        }
        return Result.success();
    }
    //验证该学号学生是否报道


    public Integer stuIdCheckout(String stuId){
        Integer flag=0;//不存在账号
        Student student=studentMapper.selectOne(Wrappers.<Student>lambdaQuery().eq(Student::getStuid,stuId));
        if(student!=null&&student.getName()==null){//存在账号但没有报道
            flag=1;
        }
        else if(student!=null&&student.getName()!=null){//存在账号并且已经报道
            flag=2;
        }
        return flag;
    }
    public boolean phoneone(String phone){
        boolean flag=false;
        if(phone.length()<=0||phone.equals("")){//判断电话为空
            flag=false;
        }
        else {
            Student student=studentMapper.selectOne(Wrappers.<Student>lambdaQuery().eq(Student::getPhone,phone));
            if(student==null){
                flag=true;
            }
        }
        return flag;
    }
}
