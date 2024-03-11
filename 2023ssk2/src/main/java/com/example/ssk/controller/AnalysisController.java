package com.example.ssk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.ssk.common.Result;
import com.example.ssk.entity.Answer;
import com.example.ssk.entity.Question;
import com.example.ssk.entity.UserList;
import com.example.ssk.mapper.ListMapper;
import com.example.ssk.mapper.StudentMapper;
import com.example.ssk.mapper.UserListMapper;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Analysis")
public class AnalysisController {
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private UserListMapper userListMapper;

    @Autowired
    private ListMapper listMapper;

    @GetMapping("/getlistbyid")//获取试卷
    public Result getlistbyid(@RequestParam Integer lid){
        QueryWrapper<com.example.ssk.entity.List> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("listname","btime","etime").eq("id",lid).eq("state","已发布").eq("is_delete",0);
        com.example.ssk.entity.List list=listMapper.selectOne(queryWrapper);
        if(list==null){
            return Result.error("500","未查询到相关数据，问卷可能已被下架或删除");
        }
        return Result.success(list);
    }
    @GetMapping("/getanalysis")//获取试卷所有的回答详情
    public Result getstudenanswer(@RequestParam Integer lid,@RequestParam Integer flag){
        List<Question> questions=setQuestion(lid,flag);
        if(questions.size()==0){
            return Result.error("500","没有该题型");
        }
        QueryWrapper<UserList> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("answer").eq("lid",lid).eq("state","已参与");
        List<UserList> lists=userListMapper.selectList(queryWrapper);
        String []text=new String[questions.size()];//记录简答
        for(int i=0;i<lists.size();i++){//每次循环都是不同的学生
            JSONArray jsonArrayone=JSONArray.parseArray(lists.get(i).getAnswer());//获取到这个学生每道题的回答

            Integer jiandaflag=0;
            for(int j=0;j<jsonArrayone.size();j++){//进入该学生不同问题的回答
                JSONObject jsonObject=jsonArrayone.getJSONObject(j);
                String question=jsonObject.getString("question");
                if(jsonObject.getInteger("type")!=3&&flag==1){//为选择题才能进入
                    Question questionone=new Question();
                    Integer index=-1;
                    for(int l=0;l<questions.size();l++){//获取问题模板
                        if(questions.get(l).getQuestion().equals(question)){
                            questionone=questions.get(l);
                            index=l;
                            break;
                        }
                    }
                    if(questionone!=null){//成功获取到了问题模板
                        String studentAnswer=jsonObject.getString("studentAnswer");
                        List<Answer> answers=questionone.getAnswerlist().toJavaList(Answer.class);//答案选项模板
                        if(jsonObject.getInteger("type")==2){//如果当前问题为多选
                            if(studentAnswer.contains("[")&&studentAnswer.contains("]")){//选了多个选项
                                JSONArray jsonArray=JSONArray.parseArray(studentAnswer);//获取学生回答
                                for(int l=0;l<jsonArray.size();l++){
                                    String answer=jsonArray.getString(l);
                                    for(int p=0;p<answers.size();p++){
                                        if(answers.get(p).getName().equals(answer)){//对应选项加1
                                            answers.get(p).setValue(answers.get(p).getValue()+1);
                                            break;
                                        }
                                    }
                                }
                            }
                            else {//只选了一个选项
                                for(int p=0;p<answers.size();p++){
                                    if(answers.get(p).getName().equals(studentAnswer)){//对应选项加1
                                        answers.get(p).setValue(answers.get(p).getValue()+1);
                                        break;
                                    }
                                }
                            }
                        }
                        else if(jsonObject.getInteger("type")==1){//单选
                            for(int p=0;p<answers.size();p++){
                                if(answers.get(p).getName().equals(studentAnswer)){//对应选项加1
                                    answers.get(p).setValue(answers.get(p).getValue()+1);
                                    break;
                                }
                            }
                        }
                        JSONArray jsonArray=JSONArray.parseArray(JSON.toJSONString(answers));
                        questionone.setAnswerlist(jsonArray);
                        questions.set(index,questionone);
                    }

                }
                else if(jsonObject.getInteger("type")==3&&flag==2){//简答
                    Question questionone=new Question();
                    for(int l=0;l<questions.size();l++){//获取问题模板
                        if(questions.get(l).getQuestion().equals(question)){
                            questionone=questions.get(l);
                            break;
                        }
                    }
                    if(questionone!=null){//成功获取问题模板
                        String studentAnswer=jsonObject.getString("studentAnswer");
                        if((!studentAnswer.equals(null)&&!studentAnswer.equals(""))){
                            text[jiandaflag]=text[jiandaflag]+studentAnswer+" ";
                            jiandaflag++;
                        }
                    }
                }
            }
        }
        if(flag==2){//获取每道题的高频词
            for(int i=0;i<questions.size();i++){
                List<Answer> words=gaoping(text[i]);
                JSONArray jsonArray=JSONArray.parseArray(JSON.toJSONString(words));
                questions.get(i).setAnswerlist(jsonArray);
            }
        }
        return Result.success(questions);
    }

    @GetMapping("/getsimple")
    public Result getsimple( @RequestParam Integer lid,@RequestParam Integer index,@RequestParam String question,@RequestParam String search){
        List<Question> questions=setQuestion(lid,2);
        List list=new ArrayList<>();
        if(questions.get(index).getQuestion().equals(question)){//验证正确才进入
            QueryWrapper<UserList> queryWrapper=new QueryWrapper<>();
            queryWrapper.select("answer").eq("lid",lid).eq("state","已参与");
            List<UserList> lists=userListMapper.selectList(queryWrapper);
            for(int i=0;i<lists.size();i++){//每次进入都是不同的学生
                JSONArray jsonArrayone=JSONArray.parseArray(lists.get(i).getAnswer());//获取到这个学生每道题的回答
                JSONObject jsonObject=jsonArrayone.getJSONObject(index);//获取该学生第index这个问题的回答和问题
                Integer pflag=0;
                if(jsonObject.getString("question").equals(question)){
                    pflag=1;
                }
                else {//没有获取到这个问题
                    for (int j = 0; j < jsonArrayone.size(); j++) {//尝试一个个找这个问题
                        jsonObject = jsonArrayone.getJSONObject(j);
                        if (jsonObject.getString("question").equals(question)){
                            index = j;
                            pflag=1;
                        };
                    }
                }
                if(pflag==1){
                    String answer=jsonObject.getString("studentAnswer");
                    if(!answer.equals(null)&&!answer.equals("")&&answer.contains(search)){
                        Map map=new HashMap<>();
                        map.put("answer",answer);
                        list.add(map);
                    }
                }
            }
        }
        else {
            return Result.error("500","错误！未查询到该问题，当前问题可能被编辑或删除");
        }
        return Result.success(list);
    }


    public List<Answer> gaoping( String text){//获取高频词
        List<Answer> words=new ArrayList<>();
        // 使用 HanLP 分词
        if(text!=null&&!text.equals(null)&&!text.equals("")){
            List<Term> terms = HanLP.segment(text);
            // 创建词汇表
            Map<String, Integer> wordCount = new HashMap<>();
            // 统计词频
            for (Term term : terms) {
                String word = term.word;
                if (wordCount.containsKey(word)) {
                    wordCount.put(word, wordCount.get(word) + 1);
                } else {
                    wordCount.put(word, 1);
                }
            }
            // 将词汇表转换为列表，便于排序
            List<Map.Entry<String, Integer>> wordList = new ArrayList<>(wordCount.entrySet());
            // 按词频排序
            wordList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
            // 输出结果
            Integer number=0;
            for (Map.Entry<String, Integer> entry : wordList) {//统计前10的关键词
                Answer word=new Answer();
                if(!entry.getKey().equals(" ")&&entry.getKey().length()>=2&&!entry.getKey().equals(null)){
                    word.setName(entry.getKey());
                    word.setValue(entry.getValue());
                    words.add(word);
                    number++;
                    if(number==10){
                        break;
                    }
                }
            }
        }
        return words;
    }
    //////////////////////////获取问题模板
    public List<Question> setQuestion(Integer lid,Integer flag){

        ////获取题目数量
        com.example.ssk.entity.List list=listMapper.selectById(lid);
        JSONArray questionnumber = JSONArray.parseArray(list.getQuestion());//获取了该问卷单位其所有问题
        Integer num=questionnumber.size();//题目数量（问题数量）
        ////开始创建相应的回答list
        List<Question> questions=new ArrayList<>();//记录每个问题所有回答（以问题为单位）
        for(int i=0;i<num;i++){//每个问题都有建一个容器
            JSONObject questionnumberJSONObject=questionnumber.getJSONObject(i);//将当前问题转化为json
            if(questionnumberJSONObject.getInteger("type")!=3&&flag==1){//选择题类型才会创建这个问题的回答容器
                JSONArray jsonArray=JSONArray.parseArray(questionnumberJSONObject.getString("answer"));
                Question question=new Question();
                question.setQuestion(questionnumberJSONObject.getString("question"));
                List<Answer> answers=new ArrayList<>();
                for(int j=0;j<jsonArray.size();j++){
                    Answer answer=new Answer();
                    answer.setName((String) jsonArray.get(j));
                    answer.setValue(0);
                    answers.add(answer);
                }
                JSONArray jsonArray1=JSONArray.parseArray(JSON.toJSONString(answers));
                question.setAnswerlist(jsonArray1);
                questions.add(question);
            }
            else if(questionnumberJSONObject.getInteger("type")==3&&flag==2){//简答题模板
                Question question=new Question();
                question.setQuestion(questionnumberJSONObject.getString("question"));
                questions.add(question);
            }
        }
        return questions;
    }
}
