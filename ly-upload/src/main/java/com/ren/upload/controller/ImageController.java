package com.ren.upload.controller;

import com.ren.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class ImageController {

    @Autowired
    private UploadService uploadService;
    /**
     * 接受 请求name = file 的图片文件上传请求
     * 保存文件, 并返回路径
     * @return
     */
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(MultipartFile file) {

        return ResponseEntity.ok(uploadService.store(file));
    }
}
