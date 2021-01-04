package com.zhao.commonservice.reqvo;

import java.io.Serializable;

/**
 * 文件上传请求VO
 * @Author: zhaolianqi
 * @Date: 2020/11/20 15:15
 * @Version: v1.0
 */
public class FileUploadReqVO implements Serializable {

    private String md5;
    private String fileName;
    private int fileSize;
    private int totalShard;
    private int currShard;


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

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
