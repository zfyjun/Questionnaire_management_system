package com.example.ssk.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.ssk.common.Cmap;
import com.example.ssk.entity.Student;
import com.example.ssk.entity.TimeDate;
import com.example.ssk.mapper.StudentMapper;
import com.example.ssk.utils.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Map")
public class MapController {
    @Resource
    @Autowired
    private StudentMapper studentMapper;
    /*下面是接口*/
    @GetMapping("/getaddress")
    public List getaddress() throws IOException {
        Cmap CMap =new Cmap();
        //分组查询(查到城市与对应的数量)
        QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("count(*) as count,address").groupBy("address");
        List<Map<String,Object>> list=studentMapper.selectMaps(queryWrapper.isNotNull("address"));
        //开始配置数据
        List body=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            Map city=new HashMap<>();
            String name=(String) list.get(i).get("address");
            city.put("name",name);//放入城市名称
            String []coordinate= CMap.getCoordinate(name);
            List value=new ArrayList<>();
            value.add(coordinate[0]);
            value.add(coordinate[1]);
            value.add(list.get(i).get("count"));
            city.put("value",value);
            body.add(city);
        }
        return body;
    }
    @GetMapping("/getsex")
    public List getsex(){
        QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("count(*) as value,sex").groupBy("sex");
        List<Map<String,Object>> list=studentMapper.selectMaps(queryWrapper.isNotNull("sex"));
        List<Map<String,Object>> list1=new ArrayList<>();
        list1.add(list.get(1));
        list1.add(list.get(0));
        return list1;
    }

    @GetMapping("/getsum")
    public List getNum() {
         List<Student> students=studentMapper.selectList((Wrappers.<Student>lambdaQuery().isNotNull(Student::getName)));
         Integer l=students.size();
         Map map=new HashMap<>();
         map.put("value",l);
         List list=new ArrayList<>();
         list.add(map);
         return list;
    }

    @GetMapping("/getmigrate")//查询迁徙
    public List getmigrate() throws IOException {
        Cmap CMap =new Cmap();
        //分组查询(查到城市与对应的数量)
        QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("count(*) as count,address").groupBy("address");
        List<Map<String,Object>> list=studentMapper.selectMaps(queryWrapper.isNotNull("address"));
        //开始配置数据
        List body=new ArrayList<>();//需要返回的list表单
        for(int i=0;i<list.size();i++){//封装body
            Map city=new HashMap<>();
            String name=(String) list.get(i).get("address");
            city.put("fromName",name);//放入城市名称（出发地）
            city.put("toName","重庆交通大学");//放入目的地名称（固定为重庆交通大学）
            String []coordinate= CMap.getCoordinate(name);
            //
            List value1=new ArrayList<>();//出发地经纬度
            value1.add(coordinate[0]);
            value1.add(coordinate[1]);
            //
            List value2=new ArrayList<>();//目的地经纬度(固定为重庆交通大学)
            value2.add("106.57148");
            value2.add("29.49051");
            //
            List coords=new ArrayList<>();
            coords.add(value1);
            coords.add(value2);
            city.put("count","1");
            city.put("amount",(list.get(i).get("count")));
            city.put("coords",coords);//经纬度
            body.add(city);
        }
        return body;
    }

    @GetMapping("/gethisto")//每天报道人数
    public  List gethisto(){
        List body=new ArrayList<>();
        List<Student> studentList=studentMapper.selectList(Wrappers.<Student>lambdaQuery().isNotNull(Student::getName));
        List<TimeDate> times=new ArrayList<>();
        for(int i=0;i<studentList.size();i++){
            TimeDate timeDate=new TimeDate();
            LocalDateTime localDateTime=studentList.get(i).getDate();
            String time=localDateTime.format(DateTimeFormatter.ofPattern("MM-dd"));
            timeDate.setTime(time);
            times.add(timeDate);
        }
        Map<String,Long> result=times.stream().collect(Collectors.groupingBy(o->o.getTime(),Collectors.counting()));
        for(String key:result.keySet()){
            Map map=new HashMap<>();
            map.put("time",key);
            map.put("value",result.get(key));
            map.put("name","系列一");
            body.add(map);
        }
        return body;
    }

    @GetMapping("/getnum")//获取城市各地区报道人数
    public List getAddressNum() throws IOException {
        QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("count(*) as value,address").groupBy("address");
        List<Map<String,Object>> list=studentMapper.selectMaps(queryWrapper.isNotNull("address"));
        List MapList=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map map=new HashMap<>();
            String name= (String) list.get(i).get("address");
            map.put("name","系列二");
            map.put("time",name);
            map.put("value",list.get(i).get("value"));
            MapList.add(map);
        }
        Map<String,Object> mapSort=new HashMap<>();
        mapSort.put("key","sort");
        mapSort.put("value","asc");
        SortUtil.sortMap(mapSort,MapList,"value");
        if(MapList.size()>5){
            MapList=MapList.subList(0,5);
        }
        return MapList;
    }

}
