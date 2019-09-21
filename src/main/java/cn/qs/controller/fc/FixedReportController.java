package cn.qs.controller.fc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.qs.service.fc.FixedReportService;
import cn.qs.utils.DefaultValue;
import cn.qs.utils.ExcelExporter;
import cn.qs.utils.FCNumberUtils;
import cn.qs.utils.ExcelExporter.OfficeVersion;
import cn.qs.utils.SystemUtils;
import cn.qs.utils.ValidateCheck;
import cn.qs.utils.WebUtils;

@Controller
@RequestMapping("fixedReport")
public class FixedReportController {

	private static final Logger logger = LoggerFactory.getLogger(FixedReportController.class);

	private static final String VIEW_BASE_PATH = "fixedReport/";

	@Autowired
	private FixedReportService fixedReportService;

	/**
	 * 充值报表
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	public String list() {
		return VIEW_BASE_PATH + "list";
	}

	@RequestMapping("pages")
	@ResponseBody
	public PageInfo<Map<String, Object>> pages(@RequestParam Map condition) {
		int pageNum = 1;
		if (ValidateCheck.isNotNull(MapUtils.getString(condition, "pageNum"))) { // 如果不为空的话改变当前页号
			pageNum = MapUtils.getInteger(condition, "pageNum");
		}
		int pageSize = DefaultValue.PAGE_SIZE;
		if (ValidateCheck.isNotNull(MapUtils.getString(condition, "pageSize"))) { // 如果不为空的话改变当前页大小
			pageSize = MapUtils.getInteger(condition, "pageSize");
		}

		Map<String, Object> tmpCondition = resetCondition(condition);
		logger.debug("tmpCondition - > {}", tmpCondition);

		// 开始分页
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> fixedReports = fixedReportService.listMapsByCondition(tmpCondition);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(fixedReports);
		return pageInfo;
	}

	@RequestMapping("/downloadDatas")
	public void downloadDatas(HttpServletRequest request, HttpServletResponse response, @RequestParam Map condition)
			throws IOException {

		// 查数据
		Map<String, Object> resetCondition = resetCondition(condition);
		List<Map<String, Object>> datas = fixedReportService.listMapsByCondition(resetCondition);

		if (CollectionUtils.isNotEmpty(datas)) {
			setSpecialValueToDatas(datas);
		}

		// 写入文件中
		String[] headerNames = new String[] { "日期", "注册人数", "首充人数", "总充人数", "总充金额", "中奖金额/人数", "投注金额/人数", "押注单量/人数",
				"盈利", "盈率" };
		ExcelExporter hssfWorkExcel = new ExcelExporter(headerNames, "日报表", OfficeVersion.OFFICE_03);

		String[] keys = new String[] { "sync_date", "register_count", "firstin_count", "in_count", "in_amount",
				"return_amount/return_count", "bet_amount/bet_count", "bet_count_all/bet_count", "profit",
				"profitability" };
		hssfWorkExcel.createTableRows(datas, keys);

		File tmpFile = SystemUtils.getTmpFile();
		try {
			hssfWorkExcel.exportExcel(new FileOutputStream(tmpFile));
		} catch (FileNotFoundException ignore) {
			// ignore
		}

		// 获取输入流
		FileInputStream openInputStream = FileUtils.openInputStream(tmpFile);

		String fileName = WebUtils.getFileName("日报表", "xls");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
		response.setContentType("multipart/form-data");

		IOUtils.copy(openInputStream, response.getOutputStream());
	}

	private void setSpecialValueToDatas(List<Map<String, Object>> datas) {
		int scale = 2;
		for (Map<String, Object> data : datas) {
			data.put("return_amount/return_count",
					FCNumberUtils.toFixedDecimal(MapUtils.getString(data, "return_amount"), scale) + " / "
							+ MapUtils.getString(data, "return_count"));

			data.put("bet_amount/bet_count", FCNumberUtils.toFixedDecimal(MapUtils.getString(data, "bet_amount"), scale)
					+ " / " + MapUtils.getString(data, "bet_count"));

			data.put("bet_count_all/bet_count",
					FCNumberUtils.toFixedDecimal(MapUtils.getString(data, "bet_count_all"), scale) + " / "
							+ MapUtils.getString(data, "bet_count"));

			data.put("profit", FCNumberUtils.toFixedDecimal(MapUtils.getString(data, "profit"), scale));
			data.put("profitability",
					FCNumberUtils.toFixedDecimalWithPercent(MapUtils.getString(data, "profitability"), scale));
		}
	}

	private Map<String, Object> resetCondition(Map condition) {
		if (!condition.containsKey("startTime")) {
			condition.put("startTime", getDefaultTime());
		}
		if (!condition.containsKey("endTime")) {
			condition.put("endTime", getDefaultTime());
		}

		return condition;
	}

	private String getDefaultTime() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_MONTH, -1);
		String format = DateFormatUtils.format(now.getTime(), "yyyy-MM-dd");
		return format;
	}

}
