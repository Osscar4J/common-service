package com.zhao.commonservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.common.utils.Asserts;
import com.zhao.common.utils.ImageColorSolution;
import com.zhao.common.utils.ImageUtils;
import com.zhao.commonservice.dao.MyFileMapper;
import com.zhao.commonservice.entity.MyFile;
import com.zhao.commonservice.service.MyFileService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
                        "  `duration` mediumint unsigned NOT NULL DEFAULT '0',\n" +
                        "  `r` smallint unsigned NOT NULL DEFAULT '0',\n" +
                        "  `g` smallint unsigned NOT NULL DEFAULT '0',\n" +
                        "  `b` smallint unsigned NOT NULL DEFAULT '0',\n" +
                        "  `url` varchar(255) NOT NULL DEFAULT '',\n" +
                        "  PRIMARY KEY (`id`),\n" +
                        "  UNIQUE KEY `uni_file_md5` (`file_md5`)\n" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;"
        );
    }

    @Override
    public boolean updateByMd5(MyFile file) {
        Asserts.notNull(file.getFileMd5());
        return this.update(file, new QueryWrapper<MyFile>().eq("file_md5", file.getFileMd5()));
    }

    @Async
    @Override
    public void updateRGBInfo(MyFile file, File imageFile) {
        String rgb = ImageUtils.getImageColorSolution(imageFile);
        String[] rgbArr = rgb.split(",");
        file.setR(Integer.parseInt(rgbArr[0]));
        file.setG(Integer.parseInt(rgbArr[1]));
        file.setB(Integer.parseInt(rgbArr[2]));
        // 获取图片的宽高
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            file.setWidth(bufferedImage.getWidth());
            file.setHeight(bufferedImage.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.updateByMd5(file);
    }
}
