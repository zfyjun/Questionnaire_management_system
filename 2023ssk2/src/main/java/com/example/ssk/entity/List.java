package com.example.ssk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("list")
public class List {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String listname;
    private String question;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime btime;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime etime;
    private String state;
    private Integer isDelete;
}
