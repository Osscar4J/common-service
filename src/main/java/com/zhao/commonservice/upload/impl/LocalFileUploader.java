package com.zhao.commonservice.upload.impl;

import com.zhao.common.utils.FileUtil;
import com.zhao.commonservice.config.PropertiesUtil;
import com.zhao.commonservice.upload.FileUploader;

import java.io.File;

public class LocalFileUploader implements FileUploader {

    private String uploadPath = null;

    @Override
    public String upload(File file, String filename) {
        if (uploadPath == null){
            uploadPath = (String) PropertiesUtil.getProperty("upload-path");
        }
        if (uploadPath == null)
            return null;
        FileUtil.transferTo(file, new File(filename));
        return filename;
    }

}
