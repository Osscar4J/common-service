package com.zhao.commonservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 各种文件
 * @Author: zhaolianqi
 * @Date: 2020/11/16 15:52
 * @Version: v1.0
 */
@TableName("file_tb")
public class MyFile extends BaseEntity {
    private static final long serialVersionUID = -5933247111999831025L;

    private Integer userId;
    private String name;
    /** 文件格式（jpg、doc、mp3等） */
    private String format;
    /** 文件大小，单位：字节 */
    private Integer fileSize;
    private String url;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}