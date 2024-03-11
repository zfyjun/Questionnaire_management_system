package com.example.ssk.controller.dto;

import lombok.Data;

/*
* 接受前端登录请求的参数
* */
@Data
public class UserDto {
    private String id;
    private String account;
    private String password;
    private String type;


    private String validCode;

}
