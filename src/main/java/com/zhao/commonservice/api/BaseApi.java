package com.zhao.commonservice.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhao.commonservice.annotations.Auth;
import com.zhao.commonservice.entity.SuperBaseEntity;
import com.zhao.commonservice.reqvo.BaseReqVO;
import com.zhao.commonservice.service.BaseService;
import org.springframework.web.bind.annotation.*;

/**
 * 基础增删改查API
 * @Author: zhaolianqi
 * @Date: 2020/10/28 14:03
 * @Version: v1.0
 */
public abstract class BaseApi<T extends SuperBaseEntity, E extends BaseReqVO> {

    public abstract BaseService<T> getService();

    @Auth(id = 10, name = "查询")
    @GetMapping
    public IPage<T> page(E reqVO){
        return getService().getPage(reqVO);
    }

    @Auth(id = 11, name = "新增/修改")
    @PostMapping
    public Boolean saveOrUpdate(@RequestBody T entity){
        return getService().saveOrUpdate(entity);
    }

    @Auth(id = 12, name = "删除")
    @DeleteMapping("/{id}")
    public Boolean remove(@PathVariable Integer id){
        return getService().removeById(id);
    }

}
