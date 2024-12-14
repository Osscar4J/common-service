package com.zhao.commonservice.api;

import com.zhao.common.entity.UserInfo;
import com.zhao.common.respvo.BaseResponse;
import com.zhao.commonservice.annotations.Auth;
import com.zhao.commonservice.annotations.CurrentUser;
import com.zhao.commonservice.annotations.LoginRequired;
import com.zhao.commonservice.annotations.PartBody;
import com.zhao.commonservice.entity.Role;
import com.zhao.commonservice.entity.User;
import com.zhao.commonservice.reqvo.BatchReqEntityVO;
import com.zhao.commonservice.reqvo.UserReqVO;
import com.zhao.commonservice.service.BaseService;
import com.zhao.commonservice.service.CacheService;
import com.zhao.commonservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 * @Author: zhaolianqi
 * @Date: 2020/10/28 14:11
 * @Version: v1.0
 */
@LoginRequired
@Auth(id = 10900, name = "用户管理")
@RequestMapping("/api/user")
@RestController
public class UserApi extends BaseApi<User, UserReqVO> {

    @Autowired
    private UserService userService;
    @Autowired
    private CacheService cacheService;

    @Override
    public BaseService<User> getService() {
        return userService;
    }

    /**
     * 获取用户信息
     * @Author zhaolianqi
     * @Date 2020/11/3 14:45
     */
    @GetMapping("/info")
    public User userInfo(@CurrentUser UserInfo user){
        return userService.getDetail(new UserReqVO().setId(user.getId()));
    }

    /**
     * 更新用户的角色信息
     * @Author zhaolianqi
     * @Date 2021/1/7 20:08
     */
    @PutMapping("/roles")
    public Boolean updateRoleMenus(@RequestBody BatchReqEntityVO<Role> reqVO){
        return userService.updateRoles(reqVO.getId(), reqVO.getRecords());
    }

    /**
     * 退出
     * @Author zhaolianqi
     * @Date 2020/11/5 14:26
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> logout(@CurrentUser UserInfo user){
        // 清除用户相关的缓存
        cacheService.removeMapCache("user-auth-" + user.getId());
        return BaseResponse.SUCCESS();
    }

    /**
     * 当前用户修改密码
     * @Author zhaolianqi
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     */
    @PostMapping("/resetPwd")
    public BaseResponse<Boolean> resetPassword(@CurrentUser UserInfo user, @PartBody("oldPwd") String oldPwd, @PartBody("newPwd") String newPwd){
        return BaseResponse.SUCCESS(userService.resetPassword(user.getId(), oldPwd, newPwd));
    }

    /**
     * 重置用户的密码
     * @Author zhaolianqi
     * @return 新的密码
     */
    @Auth(id = 13, name = "重置用户密码")
    @PutMapping("/resetPwd")
    public BaseResponse<String> resetPassword(@PartBody("userId") Integer userId){
        return BaseResponse.SUCCESS(userService.resetPassword(userId));
    }

}
