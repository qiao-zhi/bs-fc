package cn.qs.listener;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang3.time.DateFormatUtils;

import cn.qs.utils.CrawUtils;

@WebListener
public class FirstSessionCreateListener implements HttpSessionListener {

	private static volatile boolean hasSyncOnce = false;

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		if (hasSyncOnce) {
			return;
		}

		hasSyncOnce = true;

		final Calendar now = Calendar.getInstance();

		int defaultSyncTimes = 5;
		while (defaultSyncTimes > 0) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					Date time = now.getTime();
					String formatedTime = DateFormatUtils.format(time, "yyyy-MM-dd");
					CrawUtils.doCrawData(formatedTime, formatedTime);

					now.add(Calendar.DAY_OF_MONTH, -1);
				}
			}).run();

			defaultSyncTimes--;
		}

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		Calendar now = Calendar.getInstance();

		int defaultSyncTimes = 5;
		while (defaultSyncTimes > 0) {
			Date time = now.getTime();
			String formatedTime = DateFormatUtils.format(time, "yyyy-MM-dd");
			CrawUtils.doCrawData(formatedTime, formatedTime);

			defaultSyncTimes--;
			now.add(Calendar.DAY_OF_MONTH, -1);
		}
	}

}
