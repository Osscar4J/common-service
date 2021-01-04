package com.zhao.commonservice.service.impl;

import com.zhao.commonservice.dao.RoleMenuMapper;
import com.zhao.commonservice.entity.RoleMenu;
import com.zhao.commonservice.service.RoleMenuService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RoleMenuServiceImpl extends MyBaseService<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Override
    public List<String> getCreateTableSql() {
        return Arrays.asList(
                "SET FOREIGN_KEY_CHECKS=0;",
                "CREATE TABLE `role_menu_tb` (\n" +
                        "  `role_id` int(11) NOT NULL,\n" +
                        "  `menu_id` int(11) NOT NULL,\n" +
                        "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                        "  PRIMARY KEY (`role_id`,`menu_id`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
        );
    }
}
