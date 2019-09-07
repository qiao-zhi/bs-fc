package cn.qs.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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

	public static File getTmpFile() {
		// 获取到当前系统的临时工作目录
		String tempDirectoryPath = FileUtils.getTempDirectoryPath();
		String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		String tmpFileDir = tempDirectoryPath + date;
		FileUtils.deleteQuietly(new File(tmpFileDir));

		// 创建目录(以日期格式命名)
		File file2 = new File(tmpFileDir);
		file2.mkdir();

		// 创建临时文件,UUID产生名称
		String fileName = UUIDUtil.getUUID2();
		String tmpFileName = (tmpFileDir + "/" + fileName).replace("\\", "/");
		File file = new File(tmpFileName);
		try {
			file.createNewFile();
		} catch (IOException ignore) {
			// ignore
		}

		return file;
	}
}
