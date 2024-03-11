package com.example.ssk;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.ssk.entity.Student;
import com.example.ssk.entity.User;
import com.example.ssk.entity.UserList;
import com.example.ssk.mapper.ListMapper;
import com.example.ssk.mapper.StudentMapper;
import com.example.ssk.mapper.UserListMapper;
import com.example.ssk.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class SskApplicationTests {

    @Resource
    @Autowired
    private ListMapper listMapper;

    @Resource
    private UserListMapper userListMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private UserMapper userMapper;
    @Test
    void contextLoads() {
        Integer lid=7;
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
        for(int i=0;i<lists.size();i++){
            System.out.println(lists.get(i).getUid()+"  "+lists.get(i).getName());
        }

    }

}
