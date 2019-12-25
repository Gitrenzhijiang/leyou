package com.ren.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.ren.common.utils.enums.ExceptionEnum;
import com.ren.common.utils.exception.LyException;
import com.ren.upload.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {

    @Autowired
    private UploadProperties prop;

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;


    public String store(MultipartFile file){
        // 检查文件是否是合法的图片文件
        final String contentType = file.getContentType();
        if (!prop.getAllowTypes().contains(contentType)) {
            throw new LyException(ExceptionEnum.INVALID_IMAGE_TYPE);
        }
        try {
            BufferedImage bi = ImageIO.read(file.getInputStream());
            System.out.println("width: " + bi.getWidth());
            System.out.println("height: " + bi.getHeight());
            System.out.println("size: " + file.getSize());
        } catch (IOException e) {
            throw new LyException(ExceptionEnum.INVALID_IMAGE_TYPE);
        }
        StorePath storePath = null;
        try {

            InputStream inputStream = file.getInputStream();
            final String fileName = file.getOriginalFilename();

            final String end = StringUtils.substringAfterLast(fileName, ".");
            storePath = this.storageClient.uploadFile(
                    inputStream, inputStream.available(), end, null
            );
            // 返回地址
            final String path = prop.getBaseUrl() + storePath.getFullPath();

            return path;
        } catch (IOException e) {
            log.error("保存文件失败!" + e);
            throw new LyException(ExceptionEnum.SAVE_FILE_FAIL);
        }
    }
}
