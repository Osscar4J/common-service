package com.zhao.commonservice.api;

import com.zhao.common.utils.Asserts;
import com.zhao.commonservice.annotations.Auth;
import com.zhao.commonservice.annotations.LoginRequired;
import com.zhao.commonservice.entity.Menu;
import com.zhao.commonservice.reqvo.BaseReqVO;
import com.zhao.commonservice.service.BaseService;
import com.zhao.commonservice.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单管理
 * @Author: zhaolianqi
 * @Date: 2020/10/28 14:11
 * @Version: v1.0
 */
@LoginRequired
@Auth(id = 10500, name = "菜单管理")
@RequestMapping("/api/menu")
@RestController
public class MenuApi extends BaseApi<Menu, BaseReqVO> {

    @Autowired
    private MenuService menuService;

    /**
     * 查询角色下的菜单列表
     * @param reqVO
     * @return
     */
    @GetMapping("/listByRole")
    public List<Menu> getListByRole(BaseReqVO reqVO){
        Asserts.notNull(reqVO.getId());
        return menuService.getListByRole(reqVO.getId());
    }

    @Override
    public BaseService<Menu> getService() {
        return menuService;
    }

}
