package com.zhao.commonservice.service;


import com.zhao.commonservice.entity.Menu;
import com.zhao.commonservice.entity.Role;

import java.util.List;

public interface RoleService extends BaseService<Role> {

    /**
     * 更新角色的菜单权限列表
     * @Author zhaolianqi
     * @Date 2020/10/30 17:36
     */
    boolean updateRoleMenus(int roleId, List<Menu> menus);

    /**
     * 查询用户的角色列表
     * @return
     */
    List<Role> getListByUserId(int userId);

}
