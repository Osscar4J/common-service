package com.zhao.commonservice.service.impl;

import com.zhao.commonservice.dao.MyFileMapper;
import com.zhao.commonservice.entity.MyFile;
import com.zhao.commonservice.service.MyFileService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MyFileServiceImpl extends MyBaseService<MyFileMapper, MyFile> implements MyFileService {

    @Override
    public List<String> getCreateTableSql() {
        return Arrays.asList(
                "SET FOREIGN_KEY_CHECKS=0;",
                "CREATE TABLE `file_tb` (\n" +
                        "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                        "  `status` tinyint(4) NOT NULL DEFAULT '1',\n" +
                        "  `is_delete` bit(1) NOT NULL DEFAULT b'0',\n" +
                        "  `file_md5` varchar(32) NOT NULL,\n" +
                        "  `user_id` int(11) NOT NULL,\n" +
                        "  `name` varchar(255) NOT NULL,\n" +
                        "  `format` varchar(12) NOT NULL,\n" +
                        "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                        "  `file_size` int(11) NOT NULL DEFAULT '0',\n" +
                        "  `width` mediumint(8) unsigned NOT NULL DEFAULT '0',\n" +
                        "  `height` mediumint(8) unsigned NOT NULL DEFAULT '0',\n" +
                        "  `duration` mediumint(255) unsigned NOT NULL,\n" +
                        "  `url` varchar(255) NOT NULL,\n" +
                        "  PRIMARY KEY (`id`),\n" +
                        "  UNIQUE KEY `uni_file_md5` (`file_md5`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;"
        );
    }
}
