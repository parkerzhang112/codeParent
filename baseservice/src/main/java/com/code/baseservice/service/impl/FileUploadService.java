package com.code.baseservice.service.impl;

import com.code.baseservice.base.enums.ResultEnum;
import com.code.baseservice.base.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadService {

    private final String uploadFile = "/data/image";

    public String uploadFile(MultipartFile file) {
        try{
            // 获取原始文件名和文件扩展名
            String originalFileName = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFileName);

            // 生成基于时间和随机UUID的唯一文件名
            String uniqueFileName = generateUniqueFileName(fileExtension);

            // 创建指定的存储目录（如果不存在）
            File uploadDir = new File(uploadFile);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // 使用 mkdirs() 可以创建多级目录
            }

            // 保存文件到指定路径
            File destinationFile = new File(uploadDir, uniqueFileName);
            Files.copy(file.getInputStream(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;
        }catch (Exception e){
            log.error("文件上传异常", e);
        }
        throw  new BaseException(ResultEnum.ERROR);
    }

    // 生成唯一文件名
    private String generateUniqueFileName(String fileExtension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomUUID = UUID.randomUUID().toString().replace("-", "");
        return timestamp + "_" + randomUUID + fileExtension;
    }

    // 获取文件扩展名
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1) {
            return fileName.substring(dotIndex);
        }
        return ""; // 无扩展名时返回空字符串
    }
}
