package com.zhao.commonservice.reqvo;

import java.io.Serializable;

/**
 * 文件上传请求VO
 * @Author: zhaolianqi
 * @Date: 2020/11/20 15:15
 * @Version: v1.0
 */
public class FileUploadReqVO implements Serializable {

    private String key;
    private String fileName;
    private int fileSize;
    private int totalShard;
    private int currShard;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getTotalShard() {
        return totalShard;
    }

    public void setTotalShard(int totalShard) {
        this.totalShard = totalShard;
    }

    public int getCurrShard() {
        return currShard;
    }

    public void setCurrShard(int currShard) {
        this.currShard = currShard;
    }
}
