package com.zhao.commonservice.upload;

import com.zhao.commonservice.config.PropertiesUtil;
import com.zhao.commonservice.upload.impl.AliOSSFileUploader;
import com.zhao.commonservice.upload.impl.LocalFileUploader;

public class FileUploaderFactory {

    private FileUploaderFactory(){}

    public static FileUploader getUploader(){
        String uploadMode = (String) PropertiesUtil.getProperty("upload-mode");
        if (uploadMode == null)
            uploadMode = FileUploader.Mode.LOCAL;
        switch (uploadMode){
            case FileUploader.Mode.LOCAL:
                return new LocalFileUploader();
            case FileUploader.Mode.ALIOSS:
                return new AliOSSFileUploader();
        }
        return new LocalFileUploader();
    }

}
