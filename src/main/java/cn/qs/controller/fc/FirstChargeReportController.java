package cn.qs.controller.fc;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.qs.bean.user.User;
import cn.qs.service.fc.FirstChargeReportService;
import cn.qs.utils.DefaultValue;
import cn.qs.utils.ValidateCheck;

@Controller
@RequestMapping("firstcharge")
public class FirstChargeReportController {

	private static final Logger logger = LoggerFactory.getLogger(FirstChargeReportController.class);

	@Autowired
	private FirstChargeReportService firstChargeReportService;

	@RequestMapping("/firstcharge_report")
	public String firstcharge_report() {
		return "firstcharge/firstcharge_report";
	}

	@RequestMapping("pageFirstChargeReport")
	@ResponseBody
	public PageInfo<Map<String, Object>> pageFirstChargeReport(@RequestParam Map condition) {
		int pageNum = 1;
		if (ValidateCheck.isNotNull(MapUtils.getString(condition, "pageNum"))) { // 如果不为空的话改变当前页号
			pageNum = MapUtils.getInteger(condition, "pageNum");
		}
		int pageSize = 50;
		if (ValidateCheck.isNotNull(MapUtils.getString(condition, "pageSize"))) { // 如果不为空的话改变当前页大小
			pageSize = MapUtils.getInteger(condition, "pageSize");
		}

		String gmtCreated = "";
		if (condition == null || !condition.containsKey("gmtCreated")) {
			gmtCreated = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		} else {
			gmtCreated = (String) condition.get("gmtCreated");
		}

		Map<String, Object> tmpCondition = new HashMap<>();
		tmpCondition.put("gmtCreated", gmtCreated);
		List<String> syncDates = getDates(gmtCreated, 15);
		tmpCondition.put("syncDates", syncDates);

		if (MapUtils.isNotEmpty(condition)) {
			tmpCondition.putAll(condition);
		}

		logger.debug("tmpCondition - > {}", tmpCondition);

		// 开始分页
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> listFirstChargeReport = firstChargeReportService.listFirstChargeReport(tmpCondition);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(listFirstChargeReport);

		return pageInfo;
	}

	private List<String> getDates(String dateStr, int i) {
		List<String> result = new ArrayList<>();

		try {
			Date parseDate = DateUtils.parseDate(dateStr, "yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(parseDate);

			while (i > 0) {
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				i--;

				Date time = calendar.getTime();
				String formatedTime = DateFormatUtils.format(time, "yyyy-MM-dd");
				result.add(formatedTime);
			}
		} catch (ParseException e) {
			// ignore
		}

		return result;
	}

}
