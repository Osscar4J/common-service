package com.zhao.commonservice.dao;

import com.zhao.commonservice.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper extends MyBaseMapper<Role> {

    /**
     * 查询用户的角色列表
     * @param userId 用户id
     * @Author zhaolianqi
     * @Date 2021/1/7 20:16
     */
    @Select("select self.* from role_tb self " +
            "left join user_role_tb ur on ur.role_id=self.id " +
            "where ur.user_id=#{userId}")
    List<Role> selectByUserId(@Param("userId") int userId);

}
