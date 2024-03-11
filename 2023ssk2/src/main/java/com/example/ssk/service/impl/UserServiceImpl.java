package com.example.ssk.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.ssk.common.Constants;
import com.example.ssk.controller.dto.UserDto;
import com.example.ssk.entity.User;
import com.example.ssk.exception.ServiceException;
import com.example.ssk.mapper.UserMapper;
import com.example.ssk.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-03-02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public UserDto login(UserDto userDto) {
        User one =getUserInfo(userDto);
        if(one!=null){
            BeanUtil.copyProperties(one,userDto,true);

//            //设置token
//            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
//            userDto.setToken(token);
//
//            String role =one.getRole();
//
//
//            List<Menu> roleMenus = getRoleMenus(role);
//            userDto.setMenus(roleMenus);
            return userDto;
        }else{
//            return null;
            return null;
          /*  throw new ServiceException(Constants.CODE_600,"用户名或密码错误");*/
        }
    }

    private User getUserInfo(UserDto userDto) {

        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        //前一个参数是数据库表的表头，后一个数据是前端传来的参数
        queryWrapper.eq("account",userDto.getAccount());
        queryWrapper.eq("password",userDto.getPassword());
        User one;
        try{
            one = getOne(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
    }
}
