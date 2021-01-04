package com.zhao.commonservice.dao;

import com.zhao.commonservice.entity.Menu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMapper extends MyBaseMapper<Menu> {

    /**
     * 查询角色的菜单列表（含子菜单）
     * @param roleIds 角色id列表
     * @return
     */
    List<Menu> selectListByRoles(@Param("roleIds") List<Integer> roleIds);

    @Select("select max(id) from menu_tb where type=#{type}")
    int selectMaxIdByType(@Param("type") int type);

}
