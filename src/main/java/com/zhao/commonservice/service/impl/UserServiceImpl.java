package com.zhao.commonservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zhao.common.entity.UserInfo;
import com.zhao.common.exception.BusinessException;
import com.zhao.common.model.TokenModel;
import com.zhao.common.respvo.ResponseStatus;
import com.zhao.common.utils.Asserts;
import com.zhao.common.utils.JwtTokenUtil;
import com.zhao.common.utils.MD5Utils;
import com.zhao.commonservice.dao.RoleMapper;
import com.zhao.commonservice.dao.UserMapper;
import com.zhao.commonservice.entity.Menu;
import com.zhao.commonservice.entity.Role;
import com.zhao.commonservice.entity.User;
import com.zhao.commonservice.entity.UserRole;
import com.zhao.commonservice.reqvo.BaseReqVO;
import com.zhao.commonservice.reqvo.UserReqVO;
import com.zhao.commonservice.service.CacheService;
import com.zhao.commonservice.service.MenuService;
import com.zhao.commonservice.service.UserRoleService;
import com.zhao.commonservice.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends MyBaseService<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuService menuService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public boolean updateById(User entity) {
        entity.setAccount(null);
        entity.setPasswd(null);
        return super.updateById(entity);
    }

    @Override
    public TokenModel login(UserReqVO reqVO) {
        if (StringUtils.isEmpty(reqVO.getPasswd()) || StringUtils.isEmpty(reqVO.getAccount()))
            throw new BusinessException(ResponseStatus.USER_ACCOUNT_OR_PWD_WRONG);
        String password = MD5Utils.Bit32(reqVO.getPasswd());
        User user = userMapper.selectByAccount(reqVO.getAccount());
        if (user == null || !password.equalsIgnoreCase(user.getPasswd()))
            throw new BusinessException(ResponseStatus.USER_ACCOUNT_OR_PWD_WRONG);
        if (user.getStatus() == UserStatus.FREEZON)
            throw new BusinessException(ResponseStatus.USER_FREEZED);
        this.refreshAuthCache(user.getId());
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user, userInfo);
        return new TokenModel().setToken(JwtTokenUtil.generateToken(userInfo));
    }

    @Override
    public User getByOpenid(String openid) {
        // TODO 通过openid查询用户

        return null;
    }

    @Override
    public void refreshAuthCache(int userId) {
        List<Menu> menus = menuService.getListByUserId(userId);
        cacheService.removeMapCache("user-auth-" + userId);
        if (!menus.isEmpty()){
            int seconds = 3600*24*7;
            menus.forEach(menu -> {
                cacheService.putMapCache(
                        "user-auth-" + userId,
                        menu.getId().toString(),
                        String.valueOf(menu.getId()),
                        seconds);
                if (menu.getSubMenus() != null && !menu.getSubMenus().isEmpty()){
                    menu.getSubMenus().forEach(subMenu -> cacheService.putMapCache("user-auth-" + userId,
                            subMenu.getId().toString(),
                            String.valueOf(subMenu.getId()),
                            seconds));
                }
            });
        }
    }

    @Transactional
    @Override
    public boolean updateRoles(int id, List<Role> roles) {
        Asserts.notNull(roles);
        boolean res = userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", id));
        if (roles.size() > 0){
            List<UserRole> userRoles = new ArrayList<>(4);
            roles.forEach(menu -> userRoles.add(new UserRole(id, menu.getId())));
            res = userRoleService.saveBatch(userRoles);
        }
        return res;
    }

    @Override
    public boolean resetPassword(int userId, String oldPassword, String newPassword) {
        Asserts.notEmpty(oldPassword);
        Asserts.notEmpty(newPassword);
        User user = this.getById(userId);
        Asserts.notNull(user, "用户不存在");
        Asserts.state(MD5Utils.Bit32(oldPassword).equalsIgnoreCase(user.getPasswd()), "密码错误");
        return this.update(new UpdateWrapper<User>().set("passwd", MD5Utils.Bit32(newPassword)).eq("id", userId));
    }

    @Override
    public String resetPassword(int userId) {
        User user = this.getById(userId);
        Asserts.notNull(user, "用户不存在");
        String pwd = String.valueOf((int)(100000 + Math.random() * 899999));
        if (this.update(new UpdateWrapper<User>().set("passwd", MD5Utils.Bit32(pwd)).eq("id", userId)))
            return pwd;
        throw new BusinessException(ResponseStatus.FAILURE);
    }

    @Override
    public boolean save(User entity) {
        Asserts.notEmpty(entity.getAccount(), ResponseStatus.USER_ACCOUNT_CANT_BE_EMPTY);
        synchronized (this){
            User user = this.getOne(new QueryWrapper<User>().eq("account", entity.getAccount()));
            if (user != null)
                throw new BusinessException("重复的账号");
            if (!StringUtils.isEmpty(entity.getPasswd())){
                entity.setPasswd(MD5Utils.Bit32(entity.getPasswd()));
            } else {
                entity.setPasswd("");
            }
            if (entity.getPhone() == null)
                entity.setPhone("");
            return super.save(entity);
        }
    }

    @Override
    public User getDetail(BaseReqVO reqvo) {
        User user = super.getDetail(reqvo);
        if (user == null)
            return null;
        List<Role> roles = roleMapper.selectByUserId(user.getId());
        user.setRoles(roles);
        if (roles != null && !roles.isEmpty()){
            user.setMenus(menuService.getListByRoles(
                    roles.stream().map(Role::getId).collect(Collectors.toList())
            ));
        }
        return user;
    }

    @Transactional
    @Override
    public boolean removeById(Serializable id) {
        if (super.removeById(id)){
            // 删除用户的角色信息
            QueryWrapper<UserRole> queryWrapper = new QueryWrapper<UserRole>().eq("user_id", id);
            if (userRoleService.count(queryWrapper) > 0)
                return userRoleService.remove(queryWrapper);
            return true;
        }
        return false;
    }

    @Override
    public List<String> getCreateTableSql() {
        return Arrays.asList(
                "SET FOREIGN_KEY_CHECKS=0;",
                "CREATE TABLE `user_tb` (\n" +
                        "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                        "  `status` tinyint(4) NOT NULL DEFAULT '1',\n" +
                        "  `is_delete` bit(1) NOT NULL DEFAULT b'0',\n" +
                        "  `account` varchar(16) NOT NULL,\n" +
                        "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                        "  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                        "  `nickname` varchar(64) DEFAULT NULL,\n" +
                        "  `gender` tinyint(4) NOT NULL DEFAULT '2',\n" +
                        "  `phone` varchar(16) NOT NULL,\n" +
                        "  `real_name` varchar(16) DEFAULT NULL,\n" +
                        "  `passwd` varchar(32) NOT NULL,\n" +
                        "  `avatar` varchar(255) DEFAULT NULL,\n" +
                        "  `mail` varchar(128) DEFAULT NULL,\n" +
                        "  `address` varchar(255) DEFAULT NULL,\n" +
                        "  `remark` varchar(255) DEFAULT NULL,\n" +
                        "  PRIMARY KEY (`id`),\n" +
                        "  UNIQUE KEY `un_account` (`account`)\n" +
                        ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;"
        );
    }
}
