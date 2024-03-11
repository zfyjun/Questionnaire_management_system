package com.example.ssk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("code")
public class Code {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String pCode;
    private String eCode;
}
