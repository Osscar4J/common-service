package com.zhao.commonservice.dao;

import com.zhao.commonservice.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends MyBaseMapper<User> {

    /**
     * 通过账号查询用户
     * @param account 账号
     * @return
     */
    User selectByAccount(@Param("account") String account);

}
