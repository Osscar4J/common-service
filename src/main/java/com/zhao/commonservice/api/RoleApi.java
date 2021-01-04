package com.zhao.commonservice.api;

import com.zhao.commonservice.annotations.Auth;
import com.zhao.commonservice.entity.Menu;
import com.zhao.commonservice.entity.Role;
import com.zhao.commonservice.reqvo.BaseReqVO;
import com.zhao.commonservice.reqvo.BatchReqEntityVO;
import com.zhao.commonservice.service.BaseService;
import com.zhao.commonservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色管理
 * @Author: zhaolianqi
 * @Date: 2020/10/28 14:11
 * @Version: v1.0
 */
@Auth(id = 10400, name = "角色管理")
@RequestMapping("/api/role")
@RestController
public class RoleApi extends BaseApi<Role, BaseReqVO> {

    @Autowired
    private RoleService roleService;

    /**
     * 更新角色的菜单权限列表
     * @Author zhaolianqi
     * @Date 2020/10/30 17:36
     */
    @PutMapping("/menus")
    public Boolean updateRoleMenus(@RequestBody BatchReqEntityVO<Menu> reqVO){
        return roleService.updateRoleMenus(reqVO.getId(), reqVO.getRecords());
    }

    @Override
    public BaseService<Role> getService() {
        return roleService;
    }
}
