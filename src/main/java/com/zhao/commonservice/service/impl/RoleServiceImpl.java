package com.zhao.commonservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.common.utils.Asserts;
import com.zhao.commonservice.dao.RoleMapper;
import com.zhao.commonservice.entity.Menu;
import com.zhao.commonservice.entity.Role;
import com.zhao.commonservice.entity.RoleMenu;
import com.zhao.commonservice.service.RoleMenuService;
import com.zhao.commonservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RoleServiceImpl extends MyBaseService<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

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
        return res;
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
