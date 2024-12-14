package com.zhao.commonservice.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

@TableName("longtext_tb")
public class LongText extends SuperBaseEntity {

    @TableId(type = IdType.INPUT)
    private Integer id;
    private String content;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
