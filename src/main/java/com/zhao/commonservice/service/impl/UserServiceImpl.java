package com.zhao.commonservice.service.impl;

import com.zhao.common.entity.UserInfo;
import com.zhao.common.exception.BusinessException;
import com.zhao.common.model.TokenModel;
import com.zhao.common.respvo.ResponseStatus;
import com.zhao.common.utils.Asserts;
import com.zhao.common.utils.JwtTokenUtil;
import com.zhao.common.utils.MD5Utils;
import com.zhao.commonservice.dao.UserMapper;
import com.zhao.commonservice.entity.Menu;
import com.zhao.commonservice.entity.User;
import com.zhao.commonservice.reqvo.BaseReqVO;
import com.zhao.commonservice.reqvo.UserReqVO;
import com.zhao.commonservice.service.CacheService;
import com.zhao.commonservice.service.MenuService;
import com.zhao.commonservice.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl extends MyBaseService<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuService menuService;
    @Autowired
    private CacheService cacheService;

    @Override
    public TokenModel login(UserReqVO reqVO) {
        if (StringUtils.isEmpty(reqVO.getPasswd()) || StringUtils.isEmpty(reqVO.getAccount()))
            throw new BusinessException(ResponseStatus.USER_ACCOUNT_OR_PWD_WRONG);
        String password = MD5Utils.Bit32(reqVO.getPasswd());
        User user = userMapper.selectByAccount(reqVO.getAccount());
        if (user == null || !password.equalsIgnoreCase(user.getPasswd()))
            throw new BusinessException(ResponseStatus.USER_ACCOUNT_OR_PWD_WRONG);
        if (user.getStatus() == UserStatus.FREEZON)
            throw new BusinessException(ResponseStatus.USER_FREEZED);
        this.refreshAuthCache(user.getId());
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user, userInfo);
        return new TokenModel().setToken(JwtTokenUtil.generateToken(userInfo));
    }

    @Override
    public User getByOpenid(String openid) {
        // TODO 通过openid查询用户

        return null;
    }

    @Override
    public void refreshAuthCache(int userId) {
        List<Menu> menus = menuService.getListByUserId(userId);
        cacheService.removeMapCache("user-auth-" + userId);
        if (!menus.isEmpty()){
            int seconds = 3600*24*7;
            menus.forEach(menu -> {
                cacheService.putMapCache(
                        "user-auth-" + userId,
                        menu.getId().toString(),
                        String.valueOf(menu.getId()),
                        seconds);
                if (menu.getSubMenus() != null && !menu.getSubMenus().isEmpty()){
                    menu.getSubMenus().forEach(subMenu -> cacheService.putMapCache("user-auth-" + userId,
                            subMenu.getId().toString(),
                            String.valueOf(subMenu.getId()),
                            seconds));
                }
            });
        }
    }

    @Override
    public boolean save(User entity) {
        Asserts.notEmpty(entity.getAccount(), ResponseStatus.USER_ACCOUNT_CANT_BE_EMPTY);
        return super.save(entity);
    }

    @Override
    public User getDetail(BaseReqVO reqvo) {
        User user = super.getDetail(reqvo);
        user.setMenus(menuService.getListByRole(user.getRoleId()));

        return user;
    }

    @Override
    public List<String> getCreateTableSql() {
        return Arrays.asList(
                "SET FOREIGN_KEY_CHECKS=0;",
                "CREATE TABLE `user_tb` (\n" +
                        "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                        "  `status` tinyint(4) NOT NULL DEFAULT '1',\n" +
                        "  `is_delete` bit(1) NOT NULL DEFAULT b'0',\n" +
                        "  `account` varchar(16) NOT NULL,\n" +
                        "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                        "  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                        "  `role_id` int(10) unsigned NOT NULL DEFAULT '0',\n" +
                        "  `nickname` varchar(64) DEFAULT NULL,\n" +
                        "  `gender` tinyint(4) NOT NULL DEFAULT '2',\n" +
                        "  `phone` varchar(16) NOT NULL,\n" +
                        "  `real_name` varchar(16) DEFAULT NULL,\n" +
                        "  `passwd` varchar(32) NOT NULL,\n" +
                        "  `avatar` varchar(255) DEFAULT NULL,\n" +
                        "  `mail` varchar(128) DEFAULT NULL,\n" +
                        "  `address` varchar(255) DEFAULT NULL,\n" +
                        "  `remark` varchar(255) DEFAULT NULL,\n" +
                        "  PRIMARY KEY (`id`),\n" +
                        "  UNIQUE KEY `un_account` (`account`)\n" +
                        ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;"
        );
    }
}
