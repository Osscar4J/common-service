package com.zhao.commonservice.service.impl;

import com.zhao.commonservice.dao.LongTextMapper;
import com.zhao.commonservice.entity.LongText;
import com.zhao.commonservice.service.LongTextService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class LongTextServiceImpl extends MyBaseService<LongTextMapper, LongText> implements LongTextService {

    @Override
    public List<String> getCreateTableSql() {
        return Arrays.asList(
                "SET FOREIGN_KEY_CHECKS=0;",
                "CREATE TABLE `longtext_tb` (\n" +
                        "  `id` int(10) unsigned NOT NULL,\n" +
                        "  `content` text NOT NULL,\n" +
                        "  `create_time` datetime NOT NULL,\n" +
                        "  PRIMARY KEY (`id`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
        );
    }
}
