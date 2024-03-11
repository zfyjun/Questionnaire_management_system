package com.example.ssk.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.ssk.common.Verification;
import com.example.ssk.entity.Code;
import com.example.ssk.entity.Student;
import com.example.ssk.mapper.CodeMapper;
import com.example.ssk.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/Sms")
public class CodeController {
    @Resource
    @Autowired
    private CodeMapper codeMapper;
    @Autowired
    private StudentMapper studentMapper;

    @PostMapping("sendsms")
    public void sentsms(@RequestBody Map map){//设置并发送验证码
        String phone=String.valueOf(map.get("phone"));
        Verification verification=new Verification(phone);
        Integer id=getIdByPhoen(phone);
        if(id!=0){
            String p_code=verification.makecode();//获取验证码
            Code codes=codeMapper.selectById(id);
            codes.setPCode(p_code);//设置验证码
            codeMapper.updateById(codes);
            verification.sms();//发送验证码
            System.out.println(codes);
        }
    }
    //根据电话号码获取id
    public Integer getIdByPhoen(String phone){
        Integer id=0;
        //是学生的情况
        Student student=studentMapper.selectOne(Wrappers.<Student>lambdaQuery().eq(Student::getPhone,phone));
        if(student!=null){
            id=student.getUid();
        }
        return id;
    }
}
