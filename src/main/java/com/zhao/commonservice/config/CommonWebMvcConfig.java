package com.zhao.commonservice.config;

import com.zhao.commonservice.interceptors.AccessLimitIntercept;
import com.zhao.commonservice.interceptors.PermissionInterceptor;
import com.zhao.commonservice.resolver.CurrentUserResolver;
import com.zhao.commonservice.resolver.PartBodyResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@ComponentScan("com.zhao")
public class CommonWebMvcConfig implements WebMvcConfigurer {

    @Value("${upload-path}")
    private String uploadPath;

    @Autowired
    private AccessLimitIntercept accessLimitIntercept;
    @Autowired
    private PermissionInterceptor permissionInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + uploadPath);
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLimitIntercept).addPathPatterns("/api/**");
        registry.addInterceptor(permissionInterceptor).addPathPatterns("/api/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserResolver());
        resolvers.add(new PartBodyResolver());
    }
}
