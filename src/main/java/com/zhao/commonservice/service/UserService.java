package com.zhao.commonservice.service;


import com.zhao.common.model.TokenModel;
import com.zhao.commonservice.entity.User;
import com.zhao.commonservice.reqvo.UserReqVO;

public interface UserService extends BaseService<User> {

    interface UserStatus {
        /**1：正常状态*/
        public static final int NORMAL = 1;
        /**2：被冻结*/
        public static final int FREEZON = 2;
        /**3：登录异常*/
        public static final int LOGIN_UNNORMAL = 3;
        /**4：体验状态*/
        public static final int EXPERIENCE = 4;
    }

    TokenModel login(UserReqVO reqVO);

    User getByOpenid(String openid);

    void refreshAuthCache(int userId);

}
