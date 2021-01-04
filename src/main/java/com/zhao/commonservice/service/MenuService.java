package com.zhao.commonservice.service;


import com.zhao.commonservice.entity.Menu;

import java.util.List;

public interface MenuService extends BaseService<Menu> {

    /**
     * 菜单类型
     * @Author zhaolianqi
     * @Date 2020/10/30 17:23
     */
    interface Type {
        /**
         * 导航
         * @Author zhaolianqi
         * @Date 2020/10/30 17:24
         */
        int NAV = 1;
        /**
         * 功能
         * @Author zhaolianqi
         * @Date 2020/10/30 17:24
         */
        int FUNC = 2;
    }

    /**
     * 查询角色的菜单列表
     * @param roleId 角色id
     * @return
     */
    List<Menu> getListByRole(int roleId);

    /**
     * 查询用户的菜单列表（含权限信息）
     * @Author zhaolianqi
     * @Date 2020/12/11 16:06
     */
    List<Menu> getListByUserId(int userId);

}
