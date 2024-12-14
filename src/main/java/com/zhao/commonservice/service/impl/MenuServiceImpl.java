package com.zhao.commonservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.common.exception.BusinessException;
import com.zhao.commonservice.dao.MenuMapper;
import com.zhao.commonservice.entity.Menu;
import com.zhao.commonservice.entity.RoleMenu;
import com.zhao.commonservice.entity.UserRole;
import com.zhao.commonservice.service.MenuService;
import com.zhao.commonservice.service.RoleMenuService;
import com.zhao.commonservice.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends MyBaseService<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public boolean save(Menu entity) {
        if (entity.getType() == Type.NAV){
            synchronized (this){
                Integer maxId = menuMapper.selectMaxIdByType(Type.NAV);
                if (maxId == null)
                    maxId = 0;
                entity.setId(maxId + 1);
                return super.save(entity);
            }
        }
        return super.save(entity);
    }

    @Override
    public List<Menu> getListByRole(int roleId) {
        return this.getListByRoles(Collections.singletonList(roleId));
    }

    @Override
    public List<Menu> getListByRoles(List<Integer> roleIds) {
        return menuMapper.selectListByRoles(roleIds);
    }

    @Override
    public List<Menu> getListByUserId(int userId) {
        List<UserRole> userRoles = userRoleService.list(new QueryWrapper<UserRole>()
                .select("role_id")
                .eq("user_id", userId));
        if (userRoles.isEmpty())
            return Collections.emptyList();
        return menuMapper.selectListByRoles(userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList()));
    }

    @Override
    public boolean removeById(Serializable id) {
        RoleMenu roleMenu = roleMenuService.getOne(new QueryWrapper<RoleMenu>().eq("menu_id", id), false);
        if (roleMenu != null)
            throw new BusinessException("有角色引用了此菜单");
        return super.removeById(id);
    }

    @Override
    public List<String> getCreateTableSql() {
        return Arrays.asList(
                "SET FOREIGN_KEY_CHECKS=0;",
                "CREATE TABLE `menu_tb` (\n" +
                        "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                        "  `is_delete` bit(1) NOT NULL DEFAULT b'0',\n" +
                        "  `status` tinyint(4) NOT NULL DEFAULT '1',\n" +
                        "  `parent_id` int(11) NOT NULL DEFAULT '0',\n" +
                        "  `name` varchar(16) NOT NULL,\n" +
                        "  `type` tinyint(4) NOT NULL DEFAULT '1',\n" +
                        "  `sort_no` smallint(5) unsigned NOT NULL DEFAULT '0',\n" +
                        "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                        "  `method` varchar(6) NOT NULL DEFAULT 'GET',\n" +
                        "  `icon` varchar(16) DEFAULT NULL,\n" +
                        "  `url` varchar(128) DEFAULT NULL,\n" +
                        "  `remark` varchar(255) DEFAULT NULL,\n" +
                        "  PRIMARY KEY (`id`)\n" +
                        ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;"
        );
    }
}
