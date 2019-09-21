package cn.qs.controller.fc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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

import cn.qs.bean.user.User;
import cn.qs.service.fc.FirstChargeReportService;
import cn.qs.utils.DefaultValue;
import cn.qs.utils.ExcelExporter;
import cn.qs.utils.ExcelExporter.OfficeVersion;
import cn.qs.utils.SystemUtils;
import cn.qs.utils.ValidateCheck;
import cn.qs.utils.WebUtils;

@Controller
@RequestMapping("memberCount")
public class MemberCountController {

	private static final Logger logger = LoggerFactory.getLogger(MemberCountController.class);

	@Autowired
	private FirstChargeReportService firstChargeReportService;

	/**
	 * 充值报表
	 * 
	 * @return
	 */
	@RequestMapping("/totalChargeReport")
	public String chargeReport(ModelMap map, HttpServletRequest request) {
		User loginUser = SystemUtils.getLoginUser(request);

		List<String> parentNames = new ArrayList<>();
		String userblank = loginUser.getUserblank();
		if (StringUtils.isNotBlank(userblank)) {
			parentNames = Arrays.asList(userblank.split(","));
		} else {
			parentNames = firstChargeReportService.listDistinctParentName();

		}
		map.addAttribute("parentNames", parentNames);

		return "memberCount/totalChargeReport";
	}

	@RequestMapping("pageTotalChargeReport")
	@ResponseBody
	public PageInfo<Map<String, Object>> pageTotalChargeReport(@RequestParam Map condition,
			HttpServletRequest request) {

		int pageNum = 1;
		if (ValidateCheck.isNotNull(MapUtils.getString(condition, "pageNum"))) { // 如果不为空的话改变当前页号
			pageNum = MapUtils.getInteger(condition, "pageNum");
		}
		int pageSize = DefaultValue.PAGE_SIZE;
		if (ValidateCheck.isNotNull(MapUtils.getString(condition, "pageSize"))) { // 如果不为空的话改变当前页大小
			pageSize = MapUtils.getInteger(condition, "pageSize");
		}

		Map<String, Object> tmpCondition = resetCondition(condition, request);
		logger.debug("tmpCondition - > {}", tmpCondition);

		// 开始分页
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> listFirstChargeReport = firstChargeReportService.listTotalChargeReport(tmpCondition);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(listFirstChargeReport);

		return pageInfo;
	}

	@RequestMapping("/downTotalChargeReport")
	public void downTotalChargeReport(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map condition) throws IOException {

		// 查数据
		Map<String, Object> resetCondition = resetCondition(condition, request);
		List<Map<String, Object>> listFirstChargeReport = firstChargeReportService
				.listTotalChargeReport(resetCondition);

		// 写入文件中
		String[] headerNames = new String[] { "会员账号", "子账号", "子账号描述", "上级", "注册日期", "首充日期", "充值金额" };
		ExcelExporter hssfWorkExcel = new ExcelExporter(headerNames, "累计充值报表", OfficeVersion.OFFICE_03);

		String[] keys = new String[] { "user_name", "second_parent_name", "second_parent_name_remark", "parent_name",
				"register_time", "gmt_created", "total" };
		hssfWorkExcel.createTableRows(listFirstChargeReport, keys);

		File tmpFile = SystemUtils.getTmpFile();
		try {
			hssfWorkExcel.exportExcel(new FileOutputStream(tmpFile));
		} catch (FileNotFoundException ignore) {
			// ignore
		}

		// 获取输入流
		FileInputStream openInputStream = FileUtils.openInputStream(tmpFile);

		String fileName = WebUtils.getFileName("累计充值报表", "xls");
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
		response.setContentType("multipart/form-data");

		IOUtils.copy(openInputStream, response.getOutputStream());
	}

	private Map<String, Object> resetCondition(Map condition, HttpServletRequest request) {
		if (!condition.containsKey("startTime")) {
			condition.put("startTime", getDefaultTime());
		}
		if (!condition.containsKey("endTime")) {
			condition.put("endTime", getDefaultTime());
		}

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
		condition.put("parentNames", parentNames);

		return condition;
	}

	private String getDefaultTime() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_MONTH, -1);
		String format = DateFormatUtils.format(now.getTime(), "yyyy-MM-dd");
		return format;
	}

}
