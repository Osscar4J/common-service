package com.zhao.commonservice.service;

import java.util.List;

/**
 * DataSource service
 * @Author: zhaolianqi
 * @Date: 2020/12/10 14:12
 * @Version: v1.0
 */
public interface DataSourceService {

    /**
     * 检测是否存在某个表
     * @param tableName 表名
     * @Author zhaolianqi
     * @Date 2020/12/10 14:13
     */
    boolean tableIsExist(String tableName);

    /**
     * 执行sql
     * @param sql 要执行的sql列表
     * @Author zhaolianqi
     * @Date 2020/12/10 14:19
     */
    boolean executeSql(List<String> sql);

}
