package com.example.ssk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-03-07
 */
@Getter
@Setter
  @ApiModel(value = "UserList对象", description = "")
public class UserList implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private Integer uid;

    private Integer lid;

    private String answer;

    private LocalDateTime time;

    private String state;


}
