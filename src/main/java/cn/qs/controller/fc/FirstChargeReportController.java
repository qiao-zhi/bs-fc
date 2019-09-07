package cn.qs.controller.fc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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

import cn.qs.service.fc.FirstChargeReportService;
import cn.qs.utils.ExcelExporter;
import cn.qs.utils.ExcelExporter.OfficeVersion;
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

		Map<String, Object> tmpCondition = resetCondition(condition);
		logger.debug("tmpCondition - > {}", tmpCondition);

		// 开始分页
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> listFirstChargeReport = firstChargeReportService.listFirstChargeReport(tmpCondition);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(listFirstChargeReport);

		return pageInfo;
	}

	private Map<String, Object> resetCondition(Map condition) {
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

		return tmpCondition;
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

	@RequestMapping("/downFirstcharge_report")
	public void downFirstcharge_report(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map condition) throws IOException {

		// 查数据
		Map<String, Object> resetCondition = resetCondition(condition);
		List<Map<String, Object>> listFirstChargeReport = firstChargeReportService
				.listFirstChargeReport(resetCondition);

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

}
