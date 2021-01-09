package com.zhao.commonservice.service.impl;

import com.zhao.commonservice.dao.UserRoleMapper;
import com.zhao.commonservice.entity.UserRole;
import com.zhao.commonservice.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserRoleServiceImpl extends MyBaseService<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public List<String> getCreateTableSql() {
        return Arrays.asList(
                "SET FOREIGN_KEY_CHECKS=0;",
                "CREATE TABLE `user_role_tb` (\n" +
                        "  `user_id` int(11) NOT NULL,\n" +
                        "  `role_id` int(11) NOT NULL,\n" +
                        "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                        "  PRIMARY KEY (`user_id`,`role_id`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
        );
    }
}
