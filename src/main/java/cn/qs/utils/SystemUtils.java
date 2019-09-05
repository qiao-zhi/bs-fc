package cn.qs.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import cn.qs.bean.user.User;

public class SystemUtils {
	private SystemUtils() {
	}

	public static User getLoginUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			user = new User();
			user.setFullname("系统管理员");
			user.setUsername("admin");
		}

		return user;
	}

	public static String getLoginUsername(HttpServletRequest request) {
		return getLoginUser(request).getUsername();
	}

	public static <T> T getContextBean(Class<T> clazz) {
		WebApplicationContext currentWebApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		 T bean = currentWebApplicationContext.getBean(clazz);// 根据类型获取对象

		return bean;
	}
}
