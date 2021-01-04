package com.zhao.commonservice.service.impl;

import com.zhao.commonservice.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @ClassName: DataSourceServiceImpl
 * @Description: TODO
 * @Author: zhaolianqi
 * @Date: 2020/12/10 14:14
 * @Version: v1.0
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataSource dataSource;

    @Override
    public boolean tableIsExist(String tableName) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        boolean res = false;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("show tables like '" + tableName + "';");
            res = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null){
                    statement.close();
                    statement = null;
                }
                if (connection != null){
                    connection.close();
                    connection = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public boolean executeSql(List<String> sqls) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            for (String sql: sqls)
                statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (statement != null){
                    statement.close();
                    statement = null;
                }
                if (connection != null){
                    connection.close();
                    connection = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
