package com.zhao.commonservice.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 用户角色关联表
 * @Author: zhaolianqi
 * @Date: 2020/10/30 17:07
 * @Version: v1.0
 */
@TableName("user_role_tb")
public class UserRole extends SuperBaseEntity {
    private static final long serialVersionUID = -563353914439401650L;

    public UserRole(){}

    public UserRole(int userId, int roleId){
        this.userId = userId;
        this.roleId = roleId;
    }

    private Integer userId;
    private Integer roleId;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
