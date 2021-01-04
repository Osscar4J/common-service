package com.zhao.commonservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 角色-菜单关联表
 * @Author: zhaolianqi
 * @Date: 2020/10/30 17:27
 * @Version: v1.0
 */
@TableName("role_menu_tb")
public class RoleMenu extends SuperBaseEntity {
    private static final long serialVersionUID = 382895718393554217L;

    private Integer roleId;
    private Integer menuId;
    private Date createTime;

    public RoleMenu (){}
    public RoleMenu (Integer roleId, Integer menuId){
        this.roleId = roleId;
        this.menuId = menuId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
