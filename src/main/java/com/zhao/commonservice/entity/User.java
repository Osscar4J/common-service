package com.zhao.commonservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

/**
 * 用户
 * @Author: zhaolianqi
 * @Date: 2020/10/28 11:12
 * @Version: v1.0
 */
@TableName("user_tb")
public class User extends UpdateEntity {

    private static final long serialVersionUID = 4439270426971603132L;

    /**
     * 账号
     * @Author zhaolianqi
     * @Date 2020/10/28 11:37
     */
    private String account;
    /**
     * 性别
     * @Author zhaolianqi
     * @Date 2020/10/28 11:37
     */
    private Integer gender;
    /**
     * 角色id
     * @Author zhaolianqi
     * @Date 2020/10/28 11:40
     */
    private Integer roleId;
    /**
     * 手机号
     * @Author zhaolianqi
     * @Date 2020/10/28 11:37
     */
    private String phone;
    /**
     * 密码
     * @Author zhaolianqi
     * @Date 2020/10/28 11:37
     */
    private String passwd;
    /**
     * 昵称
     * @Author zhaolianqi
     * @Date 2020/10/28 11:36
     */
    private String nickname;
    /**
     * 真实姓名
     * @Author zhaolianqi
     * @Date 2020/10/28 11:36
     */
    private String realName;
    /**
     * 头像
     * @Author zhaolianqi
     * @Date 2020/10/28 11:36
     */
    private String avatar;
    /**
     * 邮箱
     * @Author zhaolianqi
     * @Date 2020/10/28 11:36
     */
    private String mail;
    /**
     * 住址
     * @Author zhaolianqi
     * @Date 2020/10/28 11:36
     */
    private String address;
    /**
     * 简介
     * @Author zhaolianqi
     * @Date 2020/10/28 11:36
     */
    private String remark;

    @TableField(exist = false)
    private List<Menu> menus;
    /**
     * 角色code列表
     * @Author zhaolianqi
     * @Date 2020/11/5 14:16
     */
    @TableField(exist = false)
    private List<String> roles;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
