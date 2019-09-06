package cn.qs.listener;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.qs.utils.CrawUtils;

@WebListener
public class StartApplicationListener implements ServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(StartApplicationListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2 * 60 * 1000L);
				} catch (InterruptedException e) {
					// ignore
				}

				// 爬虫数据
				Calendar now = Calendar.getInstance();
				int defaultSyncTimes = 5;
				while (defaultSyncTimes > 0) {
					Date time = now.getTime();
					String formatedTime = DateFormatUtils.format(time, "yyyy-MM-dd");
					CrawUtils.doCrawData(formatedTime, formatedTime);

					now.add(Calendar.DAY_OF_MONTH, -1);
					defaultSyncTimes--;
				}
			}
		}).start();

		LOGGER.info("容器启动，开启线程同步数据");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// ignore
	}

}
