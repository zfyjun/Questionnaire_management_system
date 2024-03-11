package com.example.ssk.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.ssk.common.Result;
import com.example.ssk.entity.Student;
import com.example.ssk.entity.UserList;
import com.example.ssk.mapper.ListMapper;
import com.example.ssk.mapper.StudentMapper;
import com.example.ssk.mapper.UserListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Plist")
public class PlistController {//表单发布
    @Resource
    @Autowired
    private ListMapper listMapper;

    @Resource
    private UserListMapper userListMapper;

    @Resource
    private StudentMapper studentMapper;

    @GetMapping("/getlist")//根据id获取相应的问卷
    public Result getlist(@RequestParam Integer id){
        com.example.ssk.entity.List list=listMapper.selectById(id);
        return Result.success(list);
    }

    @PostMapping("/newlist")//新建调查问卷
    public Result register(@RequestBody Map bady){
        Map form=(HashMap)bady.get("form");
        Integer id=Integer.parseInt((String) bady.get("id")) ;
        List list=(ArrayList)bady.get("question");
        String question= JSON.toJSONString(list);
        String btime=(String) form.get("btime");
        String etime=(String) form.get("etime");
        DateTimeFormatter ftf=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");//设置时间戳
        LocalDateTime begintimes=LocalDateTime.parse(btime,ftf);
        LocalDateTime endtimes=LocalDateTime.parse(etime,ftf);

        if(id==0){//新增
            com.example.ssk.entity.List list1=new com.example.ssk.entity.List();
            list1.setListname((String) form.get("listname"));
            list1.setQuestion(question);
            list1.setBtime(begintimes);
            list1.setEtime(endtimes);
            listMapper.insert(list1);
        }
        else {//修改
            com.example.ssk.entity.List list1=listMapper.selectById(id);
            list1.setListname((String) form.get("listname"));
            list1.setQuestion(question);
            list1.setBtime(begintimes);
            list1.setEtime(endtimes);
            listMapper.updateById(list1);
        }
        return Result.success();
    }

    @PostMapping("/setstudentstate")//新建调查问卷
    public Result setstudentstate(@RequestBody Map map){
        List ids=(ArrayList) map.get("list");
        Integer lid=(Integer) map.get("lid");
        for(int i=0;i<ids.size();i++){
            Student student=studentMapper.selectById((Integer)ids.get(i));
            UserList userList=new UserList();
            userList.setUid(student.getUid());
            userList.setLid(lid);
            userListMapper.insert(userList);
        }
        return Result.success();
    }


    ///下面是更新后的函数


    @GetMapping("/getRelease")//获取已发布
    public Result getRelease(@RequestParam String search){
        Map map=new HashMap<>();
        List<com.example.ssk.entity.List> lists=listMapper.selectList(Wrappers.<com.example.ssk.entity.List>lambdaQuery().eq(com.example.ssk.entity.List::getState,"已发布").eq(com.example.ssk.entity.List::getIsDelete,0).like(com.example.ssk.entity.List::getListname,search));
        map.put("list",lists);
        List number=new ArrayList<>();
        for(int i=0;i<lists.size();i++){
            Integer id=lists.get(i).getId();
            List<UserList> list=userListMapper.selectList(Wrappers.<UserList>lambdaQuery().eq(UserList::getLid,id).eq(UserList::getState,"已参与"));
            number.add(list.size());
        }
        map.put("number",number);
        return Result.success(map);
    }

    @GetMapping("/getdone")//获取当前调查问卷已完成和未完成的学生名单(根据当前id)
    public Result getdone(@RequestParam Integer id ){
        List doneundone=new ArrayList<>();
        List<UserList> userLists1=userListMapper.selectList(Wrappers.<UserList>lambdaQuery().eq(UserList::getLid,id));
        for(int i=0;i<userLists1.size();i++){//获取详细信息
            HashMap mapdone=new HashMap<>();
            Student student=studentMapper.selectOne(Wrappers.<Student>lambdaQuery().eq(Student::getUid,userLists1.get(i).getUid()));
            if(student!=null){
                mapdone.put("time",userLists1.get(i).getTime());
                mapdone.put("name",student.getName());
                mapdone.put("sid",student.getStuid());
                mapdone.put("uid",userLists1.get(i).getUid());
                mapdone.put("state",userLists1.get(i).getState());
                doneundone.add(mapdone);
            }
        }
        ///
        return Result.success(doneundone);
    }

    @GetMapping("/getstudentanswer")//获取学生回答详情
    public Result getstudenanswer(@RequestParam Integer lid,@RequestParam Integer uid){
        String question=userListMapper.selectOne(Wrappers.<UserList>lambdaQuery().eq(UserList::getUid,uid).eq(UserList::getLid,lid)).getAnswer();
        return Result.success(question);
    }

    @GetMapping("/getstudent")//获取没有该调查问卷的学生
    public Result getstudent(@RequestParam Integer lid){
        List<UserList> userLists=userListMapper.selectList(Wrappers.<UserList>lambdaQuery().eq(UserList::getLid,lid));
        QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
        List<Student> lists=studentMapper.selectList(queryWrapper.isNotNull("uid"));

        for(int i=0;i<lists.size();i++){
            for(int j=0;j<userLists.size();j++){
                if(lists.get(i).getUid()==userLists.get(j).getUid()){
                    lists.remove(i);
                    userLists.remove(j);
                    i--;
                    break;
                }
            }
        }

        return Result.success(lists);
    }
}
