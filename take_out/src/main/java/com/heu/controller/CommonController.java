package com.heu.controller;

import com.heu.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 文件上传和下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;


    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        // file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.getName());

        // 原始文件名
        String originalName = file.getOriginalFilename();
        // 获取原文件名的文件格式
        String suffix = originalName.substring(originalName.lastIndexOf("."));
        // 使用UUID生成新的文件名，防止文件名重复
        String fileName = UUID.randomUUID().toString() + suffix;
        // 创建一个目录对象
        File dir = new File(basePath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }


    // 文件下载(回显)
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try (
                // 获得字节输入流
                FileInputStream is = new FileInputStream(basePath + name);
                // 获得字节输出流
                ServletOutputStream os = response.getOutputStream()
        ) {
            int len;
            byte[] bytes = new byte[1024];
            while((len = is.read(bytes)) != -1){
                os.write(bytes,0,len);
                os.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
