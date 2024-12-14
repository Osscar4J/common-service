package com.zhao.commonservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.common.exception.BusinessException;
import com.zhao.common.utils.Asserts;
import com.zhao.commonservice.dao.RoleMapper;
import com.zhao.commonservice.entity.Menu;
import com.zhao.commonservice.entity.Role;
import com.zhao.commonservice.entity.RoleMenu;
import com.zhao.commonservice.entity.UserRole;
import com.zhao.commonservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RoleServiceImpl extends MyBaseService<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public boolean updateRoleMenus(int roleId, List<Menu> menus) {
        Asserts.notNull(menus);
        boolean res = roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id", roleId));
        if (menus.size() > 0){
            List<RoleMenu> roleMenus = new ArrayList<>(16);
            menus.forEach(menu -> roleMenus.add(new RoleMenu(roleId, menu.getId())));
            res = roleMenuService.saveBatch(roleMenus);
        }
        if (res){
            // 更新已登录的用户的权限信息（只更新拥有当前角色的用户）
            List<String> userAuthKeys = cacheService.getKeysByPattern("user-auth-*");
            if (!userAuthKeys.isEmpty()){
                for (String authKey: userAuthKeys){
                    String[] temp = authKey.split("-");
                    if (temp.length > 2){
                        int userId = Integer.parseInt(temp[2]);
                        if (userRoleService.getOne(new QueryWrapper<UserRole>().eq("user_id", userId).eq("role_id", roleId)) != null){
                            userService.refreshAuthCache(userId);
                        }
                    }
                }
            }
        }
        return res;
    }

    @Override
    public List<Role> getListByUserId(int userId) {
        return roleMapper.selectByUserId(userId);
    }

    @Transactional
    @Override
    public boolean removeById(Serializable id) {
        UserRole userRole = userRoleService.getOne(new QueryWrapper<UserRole>().eq("role_id", id), false);
        if (userRole != null)
            throw new BusinessException("该角色正在使用中");
        if (super.removeById(id)){
            // 删除角色-菜单关联信息
            return roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id", id));
        }
        return false;
    }

    @Override
    public List<String> getCreateTableSql() {
        return Arrays.asList(
                "SET FOREIGN_KEY_CHECKS=0;",
                "CREATE TABLE `role_tb` (\n" +
                        "  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,\n" +
                        "  `is_delete` tinyint(255) unsigned NOT NULL DEFAULT '0',\n" +
                        "  `status` tinyint(255) unsigned NOT NULL DEFAULT '1',\n" +
                        "  `code` varchar(16) NOT NULL,\n" +
                        "  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,\n" +
                        "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                        "  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,\n" +
                        "  `remark` varchar(255) DEFAULT NULL,\n" +
                        "  PRIMARY KEY (`id`)\n" +
                        ") ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;"
        );
    }
}
