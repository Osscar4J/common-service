package com.zhao.commonservice.upload.impl;

import com.zhao.commonservice.oss.AliOSSConfig;
import com.zhao.commonservice.oss.AliOSSUploader;
import com.zhao.commonservice.upload.FileUploader;

import java.io.File;

public class AliOSSFileUploader implements FileUploader {

    @Override
    public String upload(File file, String filename) {
        AliOSSUploader.upload(file, filename);
        return AliOSSConfig.OSS_URL + "/" + filename;
    }

}
