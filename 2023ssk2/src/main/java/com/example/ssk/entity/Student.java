package com.example.ssk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2023-03-02
 */
@Getter
@Setter
  @ApiModel(value = "Student对象", description = "")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String name;

    private Integer age;

    private String address;

    private String birthplace;

    private String phone;

    private String email;

    private String sex;

    private String stuid;

    private Integer uid;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime date;
}
