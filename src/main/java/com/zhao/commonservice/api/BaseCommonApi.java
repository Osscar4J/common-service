package com.zhao.commonservice.api;

import com.zhao.common.model.TokenModel;
import com.zhao.commonservice.reqvo.UserReqVO;
import com.zhao.commonservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 一些公共接口
 */
@RestController
@RequestMapping("/api")
public class BaseCommonApi {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public TokenModel login(@RequestBody UserReqVO reqVO){
        return userService.login(reqVO);
    }

}
