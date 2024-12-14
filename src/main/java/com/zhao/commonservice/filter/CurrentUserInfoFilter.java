package com.zhao.commonservice.filter;

import com.zhao.common.constants.SysConstants;
import com.zhao.common.model.TokenModel;
import com.zhao.common.utils.JwtTokenUtil;
import com.zhao.commonservice.utils.UserInfoUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 用来封装当前用户信息
 * @Author: zhaolianqi
 * @Date: 2021/1/28 10:50
 * @Version: v1.0
 */
@Component
public class CurrentUserInfoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        TokenModel tokenModel = JwtTokenUtil.token2tokenModal(request.getHeader(SysConstants.TOKEN));
        if (tokenModel != null)
            UserInfoUtils.setCurrentUser(tokenModel.getUser());
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
