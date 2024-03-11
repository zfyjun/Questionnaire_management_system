package com.example.ssk.service;

import com.example.ssk.controller.dto.UserDto;
import com.example.ssk.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-03-02
 */
public interface IUserService extends IService<User> {

    UserDto login(UserDto userDto);
}
