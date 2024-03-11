package com.example.ssk.entity;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class Question {
    private String question;
    private JSONArray answerlist;
}
