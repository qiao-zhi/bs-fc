package cn.qs.controller.fc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.qs.bean.fc.FirstCharge;
import cn.qs.bean.user.User;
import cn.qs.service.fc.FirstChargeReportService;
import cn.qs.utils.ExcelExporter;
import cn.qs.utils.ExcelExporter.OfficeVersion;
import cn.qs.utils.FCNumberUtils;
import cn.qs.utils.SystemUtils;
import cn.qs.utils.ValidateCheck;
import cn.qs.utils.WebUtils;

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

	@RequestMapping("/update")
	@ResponseBody
	public String update(FirstCharge firstCharge) {
		firstChargeReportService.update(firstCharge);
		return "ok";
	}

	@RequestMapping("/firstcharge_report2")
	public String firstcharge_report2(ModelMap map, HttpServletRequest request) {
		User loginUser = SystemUtils.getLoginUser(request);

		List<String> parentNames = new ArrayList<>();
		String userblank = loginUser.getUserblank();
		if (StringUtils.isNotBlank(userblank)) {
			parentNames = Arrays.asList(userblank.split(","));
		} else {
			parentNames = firstChargeReportService.listDistinctParentName();

		}
		map.addAttribute("parentNames", parentNames);

		return "firstcharge/firstcharge_report2";
	}

	@RequestMapping("pageFirstChargeReport")
	@ResponseBody
	public PageInfo<Map<String, Object>> pageFirstChargeReport(@RequestParam Map condition,
			HttpServletRequest request) {

		int pageNum = 1;
		if (ValidateCheck.isNotNull(MapUtils.getString(condition, "pageNum"))) { // 如果不为空的话改变当前页号
			pageNum = MapUtils.getInteger(condition, "pageNum");
		}
		int pageSize = 50;
		if (ValidateCheck.isNotNull(MapUtils.getString(condition, "pageSize"))) { // 如果不为空的话改变当前页大小
			pageSize = MapUtils.getInteger(condition, "pageSize");
		}

		Map<String, Object> tmpCondition = resetCondition(condition, 15, false, request);
		logger.debug("tmpCondition - > {}", tmpCondition);

		// 开始分页
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> listFirstChargeReport = firstChargeReportService.listFirstChargeReport(tmpCondition);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(listFirstChargeReport);

		return pageInfo;
	}

	@RequestMapping("pageFirstChargeReport2")
	@ResponseBody
	public PageInfo<Map<String, Object>> pageFirstChargeReport2(@RequestParam Map condition,
			HttpServletRequest request) {

		int pageNum = 1;
		if (ValidateCheck.isNotNull(MapUtils.getString(condition, "pageNum"))) { // 如果不为空的话改变当前页号
			pageNum = MapUtils.getInteger(condition, "pageNum");
		}
		int pageSize = 50;
		if (ValidateCheck.isNotNull(MapUtils.getString(condition, "pageSize"))) { // 如果不为空的话改变当前页大小
			pageSize = MapUtils.getInteger(condition, "pageSize");
		}

		Map<String, Object> tmpCondition = resetCondition(condition, 30, true, request);
		logger.debug("tmpCondition - > {}", tmpCondition);

		// 开始分页
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> listFirstChargeReport = firstChargeReportService.listFirstChargeReport2(tmpCondition);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(listFirstChargeReport);

		return pageInfo;
	}

	private Map<String, Object> resetCondition(Map condition, int days, boolean addFirstDay,
			HttpServletRequest request) {

		String gmtCreated = "";
		if (condition == null || !condition.containsKey("gmtCreated")) {
			gmtCreated = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		} else {
			gmtCreated = (String) condition.get("gmtCreated");
		}

		Map<String, Object> tmpCondition = new HashMap<>();
		tmpCondition.put("gmtCreated", gmtCreated);

		List<String> syncDates = getDates(gmtCreated, days, addFirstDay);
		tmpCondition.put("syncDates", syncDates);

		List<String> parentNames = new ArrayList<>();
		if (condition.containsKey("parentName") && StringUtils.isNotBlank((String) condition.get("parentName"))) {
			parentNames.add((String) condition.get("parentName"));
		} else {
			User loginUser = SystemUtils.getLoginUser(request);
			String userblank = loginUser.getUserblank();
			if (StringUtils.isNotBlank(userblank)) {
				parentNames = Arrays.asList(userblank.split(","));
			} else {
				parentNames = firstChargeReportService.listDistinctParentName();
			}
		}
		tmpCondition.put("parentNames", parentNames);

		if (MapUtils.isNotEmpty(condition)) {
			tmpCondition.putAll(condition);
		}

		return tmpCondition;
	}

	private List<String> getDates(String dateStr, int i, boolean addFirstDay) {
		List<String> result = new ArrayList<>();
		if (addFirstDay) {
			result.add(dateStr);
		}

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

	@RequestMapping("/downFirstcharge_report")
	public void downFirstcharge_report(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map condition) throws IOException {

		// 查数据
		Map<String, Object> resetCondition = resetCondition(condition, 15, false, request);
		List<Map<String, Object>> listFirstChargeReport = firstChargeReportService
				.listFirstChargeReport(resetCondition);

		if (CollectionUtils.isNotEmpty(listFirstChargeReport)) {
			setCouuntInfo(listFirstChargeReport);
		}

		// 写入文件中
		String[] headerNames = new String[] { "会员账号", "当天", "1天", "2天", "3天", "4天", "5天", "6天", "7天", "8天", "9天", "10天",
				"11天", "12天", "13天", "14天", "15天" };
		ExcelExporter hssfWorkExcel = new ExcelExporter(headerNames, "会员 留存报表", OfficeVersion.OFFICE_03);

		String[] keys = new String[] { "user_name", "当天", "第1天", "第2天", "第3天", "第4天", "第5天", "第6天", "第7天", "第8天", "第9天",
				"第10天", "第11天", "第12天", "第13天", "第14天", "第15天" };
		hssfWorkExcel.createTableRows(listFirstChargeReport, keys);

		File tmpFile = SystemUtils.getTmpFile();
		try {
			hssfWorkExcel.exportExcel(new FileOutputStream(tmpFile));
		} catch (FileNotFoundException ignore) {
			// ignore
		}

		// 获取输入流
		FileInputStream openInputStream = FileUtils.openInputStream(tmpFile);

		String fileName = WebUtils.getFileName("会员留存报表", "xls");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
		response.setContentType("multipart/form-data");

		IOUtils.copy(openInputStream, response.getOutputStream());
	}

	@RequestMapping("/downFirstcharge_report2")
	public void downFirstcharge_report2(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map condition) throws IOException {

		// 查数据
		Map<String, Object> resetCondition = resetCondition(condition, 30, true, request);
		List<Map<String, Object>> listFirstChargeReport = firstChargeReportService
				.listFirstChargeReport2(resetCondition);

		if (CollectionUtils.isNotEmpty(listFirstChargeReport)) {
			setCouuntInfo2(listFirstChargeReport);
		}

		// 写入文件中
		String[] headerNames = new String[] { "会员账号", "子账号", "上级", "首充日期", "当天" };
		String[] keys = new String[] { "user_name", "second_parent_name", "parent_name", "gmt_created", "第0天" };

		for (int i = 0; i < 30; i++) {
			headerNames = ArrayUtils.add(headerNames, (i + 1) + "天");
			keys = ArrayUtils.add(keys, "第" + (i + 1) + "天");
		}

		ExcelExporter hssfWorkExcel = new ExcelExporter(headerNames, "留存查询", OfficeVersion.OFFICE_03);
		hssfWorkExcel.createTableRows(listFirstChargeReport, keys);

		File tmpFile = SystemUtils.getTmpFile();
		try {
			hssfWorkExcel.exportExcel(new FileOutputStream(tmpFile));
		} catch (FileNotFoundException ignore) {
			// ignore
		}

		// 获取输入流
		FileInputStream openInputStream = FileUtils.openInputStream(tmpFile);

		String fileName = WebUtils.getFileName("留存查询", "xls");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
		response.setContentType("multipart/form-data");

		IOUtils.copy(openInputStream, response.getOutputStream());
	}

	private void setCouuntInfo2(List<Map<String, Object>> listFirstChargeReport) {
		int length = listFirstChargeReport.size();

		// 插入空行
		Map<String, Object> empty = new HashMap<>();
		listFirstChargeReport.add(empty);

		// 创建最后一行汇总行
		Map<String, Object> countMap = new HashMap<>();
		countMap.put("user_name", "汇总信息");
		countMap.put("gmt_created", "");
		countMap.put("第0天", 0);
		for (int i = 0; i < 30; i++) {
			// 总值
			countMap.put("第" + (i + 1) + "天", 0);
			// 标记多少个1
			countMap.put("第" + (i + 1) + "天rate", 0);
		}

		for (Map<String, Object> result : listFirstChargeReport) {
			Set<Entry<String, Object>> entrySet = result.entrySet();

			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();

				if ("user_name".equals(key) || "second_parent_name".equals(key) || "parent_name".equals(key)) {
					continue;
				}

				if ("gmt_created".equals(key)) {
					if (StringUtils.isNotBlank((String) entry.getValue())) {
						result.put(key, entry.getValue().toString().substring(0, 10));
					}
					continue;
				}

				Object value = entry.getValue();
				if (!"-".equals(value)) {
					double doubleValue = NumberUtils.toDouble(value.toString());
					result.put(key, "1" + " / " + FCNumberUtils.toFixedDecimal(doubleValue, 2));

					countMap.put(key,
							FCNumberUtils.toFixedDecimal(doubleValue + MapUtils.getDoubleValue(countMap, key, 0D), 2));
					countMap.put(key + "rate", MapUtils.getInteger(countMap, key + "rate", 0) + 1);
				}
			}
		}

		// 处理汇总行
		for (int i = 0; i <= 30; i++) {
			String key = "第" + i + "天";
			String rateKey = "第" + i + "天rate";
			Object value = countMap.get(key);
			Integer rateValue = MapUtils.getInteger(countMap, rateKey);

			String result = rateValue + "(" + FCNumberUtils.toFixedDecimalWithPercent((double) rateValue / length, 2)
					+ value + ")";
			countMap.put(key, result);
		}

		listFirstChargeReport.add(countMap);
	}

	private void setCouuntInfo(List<Map<String, Object>> listFirstChargeReport) {
		int length = listFirstChargeReport.size();

		// 插入空行
		Map<String, Object> empty = new HashMap<>();
		listFirstChargeReport.add(empty);

		Map<String, Object> countMap = new HashMap<>();
		countMap.put("user_name", "汇总信息");
		countMap.put("当天", 0);
		countMap.put("第1天", 0);
		countMap.put("第2天", 0);
		countMap.put("第3天", 0);
		countMap.put("第4天", 0);
		countMap.put("第5天", 0);
		countMap.put("第6天", 0);
		countMap.put("第7天", 0);
		countMap.put("第8天", 0);
		countMap.put("第9天", 0);
		countMap.put("第10天", 0);
		countMap.put("第11天", 0);
		countMap.put("第12天", 0);
		countMap.put("第13天", 0);
		countMap.put("第14天", 0);
		countMap.put("第15天", 0);

		for (Map<String, Object> result : listFirstChargeReport) {
			Set<Entry<String, Object>> entrySet = result.entrySet();

			for (Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();

				if (key != null && "user_name".equals(key)) {
					continue;
				}

				Object value = entry.getValue();
				if ("1".equals(value)) {
					int tmpCount = NumberUtils.toInt(countMap.get(key).toString(), 0);
					countMap.put(key, ++tmpCount);
				}
			}
		}

		Map<String, Object> result = new HashMap<>();
		for (Entry<String, Object> entry : countMap.entrySet()) {
			String key = entry.getKey();
			Object tmpValue = entry.getValue();

			if ("user_name".equals(key)) {
				result.put(key, tmpValue);
				continue;
			}

			NumberFormat nt = NumberFormat.getPercentInstance();// 获取百分数实例
			nt.setMinimumFractionDigits(2);
			String rate = nt.format(Float.valueOf(String.valueOf(tmpValue)) / (float) length);
			result.put(key, tmpValue + "(" + rate + ")");
		}
		listFirstChargeReport.add(result);
	}
}
