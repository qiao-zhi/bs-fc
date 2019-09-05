package cn.qs.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.qs.utils.CrawUtils;

@Component
public class CrawJob {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrawJob.class);

	@Scheduled(cron = "0 0 23 * * ?")
	public void cron() {
		LOGGER.info("开始爬虫");
		CrawUtils.doCrawData();
		LOGGER.info("结束爬虫");
	}
}
