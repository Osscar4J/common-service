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
    private String fileMd5;
    /** 文件格式（jpg、doc、mp3等） */
    private String format;
    /** 文件大小，单位：字节 */
    private Integer fileSize;
    private Integer width;
    private Integer height;
    private Integer duration;
    private Integer r;
    private Integer g;
    private Integer b;
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

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getB() {
        return b;
    }

    public void setB(Integer b) {
        this.b = b;
    }

    public Integer getG() {
        return g;
    }

    public void setG(Integer g) {
        this.g = g;
    }

    public Integer getR() {
        return r;
    }

    public void setR(Integer r) {
        this.r = r;
    }
}
