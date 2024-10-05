package com.houssem.book.file;


import com.houssem.book.book.Book;
import jakarta.annotation.Nonnull;
import jakarta.mail.Folder;
import jakarta.mail.Multipart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.currentTimeMillis;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${application.file.photos-output-path}")
    private String fileUploadPath;

    public String saveFile(
            @Nonnull MultipartFile sourceFile,
            @Nonnull Integer userId) {
        final String fileUploadSubPath="users" + File.separator + userId;
        return uploadFile(sourceFile,fileUploadSubPath);
    }

    private String uploadFile(@Nonnull MultipartFile sourceFile, @Nonnull String fileUploadSubPath) {
        final String finalUploadFile=fileUploadPath + File.separator + fileUploadSubPath;
        File targetFolder =new File(finalUploadFile);
        if(!targetFolder.exists()){
            boolean folderCreated=targetFolder.mkdirs();
            if(!folderCreated){
                log.warn("Failed to create the target folder");
                return null;
            }
        }
        final String fileExtention=getFileExtention(sourceFile.getOriginalFilename());
        String targetFilePath=fileUploadPath + File.separator + currentTimeMillis() + "." + fileExtention;
        Path targetPath= Paths.get(targetFilePath);
        try {
            Files.write(targetPath,sourceFile.getBytes());
            log.info("file saved to "+targetFilePath);
            return targetFilePath;
        }catch (IOException e){
            log.error("File was not saved",e);
        }
        return null;
    }

    private String getFileExtention(String originalFilename) {
        if(originalFilename==null || originalFilename.isEmpty() ){
            return  "";
        }

        int lastDotIndex=originalFilename.lastIndexOf(".");
        if(lastDotIndex==-1){
            return "";
        }
        return originalFilename.substring(lastDotIndex +1).toLowerCase();
    }
}
