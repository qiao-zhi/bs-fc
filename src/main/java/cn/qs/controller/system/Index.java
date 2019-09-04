package cn.qs.controller.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.qs.utils.CrawUtils;

@Controller
public class Index {

	private static final Logger LOGGER = LoggerFactory.getLogger(Index.class);

	@RequestMapping("/index")
	public String index(ModelMap map) {
		LOGGER.debug("111");
		return "index";
	}

	@RequestMapping("/welcome")
	public String welcome(ModelMap map) {
		return "welcome";
	}
}
