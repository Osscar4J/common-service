package com.zhao.commonservice.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

/**
 * UpdateEntity
 * @Author: zhaolianqi
 * @Date: 2020/12/9 16:17
 * @Version: v1.0
 */
public class UpdateEntity extends BaseEntity {
    private static final long serialVersionUID = -8698683074666668447L;

    @TableField(fill = FieldFill.UPDATE)
    protected Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
