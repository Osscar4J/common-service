package com.zhao.commonservice.context;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.common.utils.JwtTokenUtil;
import com.zhao.commonservice.annotations.Auth;
import com.zhao.commonservice.entity.Menu;
import com.zhao.commonservice.service.BaseService;
import com.zhao.commonservice.service.DataSourceService;
import com.zhao.commonservice.service.MenuService;
import com.zhao.commonservice.service.impl.MyBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@Component
public class ServerStartup implements ApplicationRunner {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${jwt.secret}")
	private String jwtSecret;
	@Value("${jwt.exp}")
	private long jwtExp;
	@Autowired
	private RequestMappingInfoHandlerMapping requestMappingInfoHandlerMapping;
	@Autowired
	private MenuService menuService;

	public ServerStartup(ApplicationContext applicationContext){
		Map<String, BaseService> serviceMap = applicationContext.getBeansOfType(BaseService.class);
		DataSourceService dataSourceService = applicationContext.getBean(DataSourceService.class);
		for (String key: serviceMap.keySet()){
			BaseService service = serviceMap.get(key);
			Type genType = service.getClass().getSuperclass().getGenericSuperclass();
			if (genType instanceof ParameterizedType){
				Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
				if (params.length > 1){
					((MyBaseService)service).autoCreateTable(dataSourceService, (Class<?>)params[1]);
				}
			}
		}
	}

	@Transactional
	@Override
	public void run(ApplicationArguments args) {
		logger.info("============= 初始化JWT参数 =============");
		JwtTokenUtil.initConfig(jwtSecret, jwtExp);
		logger.info("============= 初始化JWT参数完成 =============");
		// 自动生成接口权限列表
		getAuthResources();
	}

	/**
	 * 扫描并返回所有需要权限处理的接口资源
	 */
	private void getAuthResources() {
		// 先删除旧的数据
		menuService.remove(new QueryWrapper<Menu>().eq("type", MenuService.Type.FUNC));
		// 拿到所有接口信息，并开始遍历
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingInfoHandlerMapping.getHandlerMethods();
		Map<Integer, List<Menu>> menuMap = new HashMap<>(32);
		handlerMethods.forEach((info, handlerMethod) -> {
			// 拿到类(模块)上的权限注解
			Auth moduleAuth = handlerMethod.getBeanType().getAnnotation(Auth.class);
			// 拿到接口方法上的权限注解
			Auth methodAuth = handlerMethod.getMethod().getAnnotation(Auth.class);
			// 模块注解和方法注解缺一个都代表不进行权限处理
			if (moduleAuth == null || methodAuth == null) {
				return;
			}
			List<Menu> list = menuMap.get(moduleAuth.id());
			if (list == null){
				// 创建上级功能权限
				list = new ArrayList<>(8);
				Menu menu = new Menu();
				menu.setId(moduleAuth.id());
				menu.setName(moduleAuth.name());
				menu.setType(MenuService.Type.FUNC);
				menu.setMethod("*");
				RequestMapping rm = handlerMethod.getBeanType().getAnnotation(RequestMapping.class);
				menu.setUrl(rm.value()[0]);
				list.add(menu);
				menuMap.put(moduleAuth.id(), list);
			}
			// 拿到该接口方法的请求方式(GET、POST等)
			Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
			// 如果一个接口方法标记了多个请求方式，权限id是无法识别的，不进行处理
			if (methods.size() != 1) {
				return;
			}
			// 将权限名、路径、类型组装成对象，并添加集合中
			Menu menu = new Menu();
			menu.setUrl(String.valueOf(info.getPatternsCondition().getPatterns().toArray()[0]));
			menu.setType(MenuService.Type.FUNC);
			menu.setName(methodAuth.name());
			menu.setParentId(moduleAuth.id());
			menu.setMethod(String.valueOf(methods.toArray()[0]));
			menu.setId(moduleAuth.id() + methodAuth.id());
			list.add(menu);
		});
		menuMap.forEach((key, menus) -> menuService.saveBatch(menus));
	}

}
