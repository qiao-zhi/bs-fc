package cn.qs.listener;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.qs.bean.user.User;
import cn.qs.service.user.UserService;
import cn.qs.utils.CrawUtils;
import cn.qs.utils.SpringBootUtils;

@WebListener
public class StartApplicationListener implements ServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(StartApplicationListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				LOGGER.info("容器启动，开启线程同步数据创建默认用户");
				try {
					Thread.sleep(1 * 60 * 1000L);
				} catch (InterruptedException e) {
					// ignore
				}

				doCreateAdmin();

				doCrawDatas();
			}

			private void doCrawDatas() {
				Calendar now = Calendar.getInstance();
				int defaultSyncTimes = 30;
				while (defaultSyncTimes > 0) {
					Date time = now.getTime();
					String formatedTime = DateFormatUtils.format(time, "yyyy-MM-dd");
					CrawUtils.doCrawData(formatedTime, formatedTime);

					System.out.println(formatedTime);
					now.add(Calendar.DAY_OF_MONTH, -1);
					defaultSyncTimes--;
				}
			}

			private void doCreateAdmin() {
				String adminUserName = "admin";

				UserService userService = SpringBootUtils.getBean(UserService.class);
				User findUserByUsername = userService.findUserByUsername(adminUserName);
				if (findUserByUsername == null) {
					User user = new User();
					String adminPassword = DigestUtils.md5Hex("123456");
					user.setPassword(adminPassword);
					user.setUsername(adminUserName);
					user.setFullname("系统管理员");

					userService.addUser(user);
					LOGGER.info("系统管理员创建成功");
				} else {
					LOGGER.info("系统管理员已经存在");
				}
			}
		}).start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// ignore
	}

}
