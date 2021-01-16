package com.zhao.commonservice.upload;

import java.io.File;

public interface FileUploader {

    interface Mode {
        String LOCAL = "local";
        String ALIOSS = "alioss";
    }

    String upload(File file, String filename);

}
