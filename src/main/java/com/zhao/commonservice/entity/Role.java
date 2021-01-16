package com.zhao.commonservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 角色
 * @Author: zhaolianqi
 * @Date: 2020/10/30 17:07
 * @Version: v1.0
 */
@TableName("role_tb")
public class Role extends UpdateEntity {
    private static final long serialVersionUID = -563353914439401650L;

    private String code;
    private String name;
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
