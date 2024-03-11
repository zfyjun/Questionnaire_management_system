package com.example.ssk.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ssk.common.Result;
import com.example.ssk.entity.Files;
import com.example.ssk.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

//文件上传相关接口
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FileMapper fileMapper;

    //上传接口
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException { //file是前端传递过来的文件
        String originalFilename = file.getOriginalFilename(); //获取文件名称
        String type = FileUtil.extName(originalFilename); //获取文件类型
        long size = file.getSize(); //获取文件大小

        //定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;

        File uploadFile = new File(fileUploadPath + fileUUID); //实际最终要存的file
        //判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String url;
        //上传文件到磁盘
        file.transferTo(uploadFile);
        //获取文件的md5
        String md5 = SecureUtil.md5(uploadFile);
        //从数据库查询是否存在相同的记录
        Files dbFiles = getFileByMd5(md5);
        if(dbFiles != null) {
            url=dbFiles.getUrl();
            //由于文件已存在,所以删除刚才上传的重复文件
            uploadFile.delete();
        } else {
            //数据若不存在重复文件,则不删除刚才上传的文件
            url = "http://39.107.95.5:9090/file/" + fileUUID;
        }


        //存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);

        return url;
    }

    //文件下载接口  http://localhost:9876/file/{fileUUID}
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        //根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        //设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename" + URLEncoder.encode(fileUUID,"UTF-8"));
        response.setContentType("application/octet-stream");

        //读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    //通过文件的md5查询文件
    private Files getFileByMd5(String md5) {
        //查询文件的md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

//    前端文件是否启用按钮的状态更新接口
    @PostMapping("/update")
    public Result update(@RequestBody Files files) {
        return Result.success(fileMapper.updateById(files));
    }

    //    文件删除接口
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Files files = fileMapper.selectById(id);
        files.setIsDelete(true);
        fileMapper.updateById(files);
        return Result.success();
    }

    //    文件批量删除接口
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {

        QueryWrapper<Files> wrapper = new QueryWrapper<>();
        wrapper.in("id",ids);
        List<Files> files = fileMapper.selectList(wrapper);
        for(Files file: files) {
            file.setIsDelete(true);
            fileMapper.updateById(file);
        }
        return Result.success();
    }

//文件分页查询接口
    @ResponseBody
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(defaultValue = "") String name) {
        IPage<Files> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Files> wrapper = new QueryWrapper<>();
        //查询未删除的记录
        wrapper.eq("is_delete",false);
        if(!"".equals(name)) {
            wrapper.like("name",name);
        }
        return Result.success(fileMapper.selectPage(page,wrapper));
    }

}
