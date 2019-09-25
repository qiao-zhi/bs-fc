package cn.qs.utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.qs.bean.fc.FirstCharge;
import cn.qs.bean.fc.FixedReport;
import cn.qs.bean.fc.LoginLog;
import cn.qs.bean.fc.Member;
import cn.qs.service.fc.FirstChargeService;
import cn.qs.service.fc.FixedReportService;
import cn.qs.service.fc.LoginLogService;
import cn.qs.service.fc.MemberService;

public class CrawUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrawUtils.class);

	private static final String URL_LOGIN = "http://xfcai.jiuheyikuang.cn/v1/management/manager/login";

	private static final String URL_LOGOUT = "http://xfcai.jiuheyikuang.cn/v1/management/manager/logout";

	private static final String URL_CRAW_MEMBER = "http://xfcai.jiuheyikuang.cn/v1/report/userReport?userName=&agentName=&sort=1&pageSize=20";

	private static final String URL_CRAW_FIRST_CHARGE = "http://xfcai.jiuheyikuang.cn/v1/report/firstinReport?sort=0&userName=&parentName=&pageSize=20";

	private static final String URL_CRAW_FIXED_REPORT = "http://xfcai.jiuheyikuang.cn/v1/report/tenantReport?type=1";

	private static final String URL_CRAW_LAST_LOGIN_REPORT = "http://xfcai.jiuheyikuang.cn/v1/users/loginLog?pageSize=20";

	private static Map<String, String> cookies = new HashMap<String, String>();

	public static void main(String[] args) {
		doCrawTodayData();
	}

	public static void doCrawTodayData() {
		// 不传日期代表爬当天数据
		doCrawData("", "");
	}

	public static void doCrawData(String startTime, String endTime) {
		boolean loginSuccessed = login();
		if (!loginSuccessed) {
			throw new RuntimeException("登陆失败");
		}

		crawMembers(startTime, endTime);
		crawFirstCharges(startTime, endTime);
		crawFixedReport(startTime, endTime);
		crawLoginLog(startTime, endTime);

		logout();
	}

	private static void crawLoginLog(String startTime, String endTime) {
		if (StringUtils.isBlank(startTime) && StringUtils.isBlank(endTime)) {
			Date today = new Date();
			Date yesterday = DateUtils.addDays(new Date(), -1);
			startTime = DateFormatUtils.format(yesterday, "yyyy-MM-dd");
			endTime = DateFormatUtils.format(today, "yyyy-MM-dd");
		} else if (startTime.equals(endTime)) {
			try {
				Date endTimeDate = DateUtils.parseDate(endTime, "yyyy-MM-dd");
				Date endTimeDateNextDay = DateUtils.addDays(endTimeDate, 1);
				endTime = DateFormatUtils.format(endTimeDateNextDay, "yyyy-MM-dd");
			} catch (ParseException ignored) {
				// ignored
			}

		}

		String url = URL_CRAW_LAST_LOGIN_REPORT + "&startTime=" + startTime + "&endTime=" + endTime;

		// 模拟最多有200000页数据
		int i = 1;
		while (i < 200000) {
			String tmpUrl = url + "&pageNum=" + (i++);
			LOGGER.debug("tmpUrl -> {}", tmpUrl);

			String responseInfo = requestURL(tmpUrl);
			String bodyInfo = extractContentByTag(responseInfo, "body");
			HashMap<String, Object> parseObject = JSONObject.parseObject(bodyInfo, HashMap.class);

			String dataJSON = MapUtils.getString(parseObject, "data", "");
			HashMap data = JSONObject.parseObject(dataJSON, HashMap.class);
			if (MapUtils.getInteger(data, "total", 0).equals(0) || "[]".equals(MapUtils.getString(data, "result"))) {
				break;
			}

			batchDisposeLoginLogs(MapUtils.getString(data, "result"), startTime);
		}
	}

	private static void batchDisposeLoginLogs(String string, String syncDate) {
		if (StringUtils.isEmpty(string)) {
			return;
		}

		LOGGER.debug("string -> {}", string);
		List<LoginLog> loginLogs = JSONArray.parseArray(string, LoginLog.class);
		for (LoginLog log : loginLogs) {
			log.setSyncDate(syncDate);

			LoginLogService loginLogService = SpringBootUtils.getBean(LoginLogService.class);
			LoginLog findById = loginLogService.findById(log.getId());
			if (findById != null && findById.getUserId() != null) {
				continue;
			} else {
				loginLogService.add(log);
			}
		}
	}

	private static void crawFixedReport(String startTime, String endTime) {
		startTime = StringUtils.defaultIfBlank(startTime, DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		endTime = StringUtils.defaultIfBlank(endTime, DateFormatUtils.format(new Date(), "yyyy-MM-dd"));

		String url = URL_CRAW_FIXED_REPORT + "&startTime=" + startTime + "&endTime=" + endTime;

		String responseInfo = requestURL(url);
		String bodyInfo = extractContentByTag(responseInfo, "body");
		if (StringUtils.isBlank(bodyInfo)) {
			return;
		}

		HashMap<String, Object> parseObject = JSONObject.parseObject(bodyInfo, HashMap.class);

		batchDisposeFixedReports(MapUtils.getString(parseObject, "data", ""), startTime);
	}

	private static void batchDisposeFixedReports(String data, String syncDate) {
		if (StringUtils.isBlank(data)) {
			return;
		}

		FixedReportService fixedReportService = SpringBootUtils.getBean(FixedReportService.class);
		FixedReport findBySyncDate = fixedReportService.findBySyncDate(syncDate);

		FixedReport fixedReport = JSONObject.parseObject(data, FixedReport.class);
		fixedReport.setSyncDate(syncDate);
		if (findBySyncDate != null && findBySyncDate.getSyncDate() != null) {
			fixedReport.setId(findBySyncDate.getId());
			fixedReportService.update(fixedReport);
		} else {
			fixedReportService.add(fixedReport);
		}
	}

	private static void crawMembers(String startTime, String endTime) {
		startTime = StringUtils.defaultIfBlank(startTime, DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		endTime = StringUtils.defaultIfBlank(endTime, DateFormatUtils.format(new Date(), "yyyy-MM-dd"));

		String url = URL_CRAW_MEMBER + "&startTime=" + startTime + "&endTime=" + endTime;

		// 模拟最多有200000页数据
		int i = 1;
		while (i < 200000) {
			String tmpUrl = url + "&pageNum=" + (i++);
			LOGGER.debug("tmpUrl -> {}", tmpUrl);

			String responseInfo = requestURL(tmpUrl);
			String bodyInfo = extractContentByTag(responseInfo, "body");
			HashMap<String, Object> parseObject = JSONObject.parseObject(bodyInfo, HashMap.class);

			String dataJSON = MapUtils.getString(parseObject, "data", "");
			HashMap data = JSONObject.parseObject(dataJSON, HashMap.class);
			if (MapUtils.getInteger(data, "total", 0).equals(0) || "[]".equals(MapUtils.getString(data, "result"))) {
				break;
			}

			batchDisposeMemberStrs(MapUtils.getString(data, "result"), startTime);
		}

	}

	private static void batchDisposeMemberStrs(String memberStrs, String syncDate) {
		if (StringUtils.isEmpty(memberStrs)) {
			return;
		}

		LOGGER.debug("memberStrs -> {}", memberStrs);
		List<Member> members = JSONArray.parseArray(memberStrs, Member.class);
		for (Member member : members) {
			member.setSyncDate(syncDate);

			MemberService memberService = SpringBootUtils.getBean(MemberService.class);
			Member findMember = memberService.findByUserIdAndSyncDate(member.getUserId(), syncDate);
			if (findMember != null && findMember.getUserId() != null) {
				member.setId(findMember.getId());
				memberService.update(member);
			} else {
				memberService.add(member);
			}
		}
	}

	private static void crawFirstCharges(String startTime, String endTime) {
		startTime = StringUtils.defaultIfBlank(startTime, DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		endTime = StringUtils.defaultIfBlank(endTime, DateFormatUtils.format(new Date(), "yyyy-MM-dd"));

		String url = URL_CRAW_FIRST_CHARGE + "&startTime=" + startTime + "&endTime=" + endTime;

		// 模拟最多有200000页数据
		int i = 1;
		while (i < 200000) {
			String tmpUrl = url + "&pageNum=" + (i++);
			LOGGER.debug("tmpUrl -> {}", tmpUrl);

			String responseInfo = requestURL(tmpUrl);
			String bodyInfo = extractContentByTag(responseInfo, "body");
			HashMap<String, Object> parseObject = JSONObject.parseObject(bodyInfo, HashMap.class);

			String dataJSON = MapUtils.getString(parseObject, "data", "");
			HashMap data = JSONObject.parseObject(dataJSON, HashMap.class);
			if (MapUtils.getInteger(data, "total", 0).equals(0) || "[]".equals(MapUtils.getString(data, "result"))) {
				break;
			}

			batchDisposeFirstCharges(MapUtils.getString(data, "result"));
		}
	}

	private static void batchDisposeFirstCharges(String firstChargeStrs) {
		if (StringUtils.isEmpty(firstChargeStrs)) {
			return;
		}

		LOGGER.debug("firstChargeStrs -> {}", firstChargeStrs);

		List<FirstCharge> firstCharges = JSONArray.parseArray(firstChargeStrs, FirstCharge.class);
		for (FirstCharge firstCharge : firstCharges) {
			FirstChargeService firstChargeService = SpringBootUtils.getBean(FirstChargeService.class);
			FirstCharge findFirstCharge = firstChargeService.findByUserId(firstCharge.getUserId());
			if (findFirstCharge != null && findFirstCharge.getUserId() != null) {
				firstCharge.setId(findFirstCharge.getId());
				firstChargeService.update(firstCharge);
			} else {
				firstChargeService.add(firstCharge);
			}
		}

	}

	private static boolean login() {
		LOGGER.debug("开始登陆");
		Connection connect = Jsoup.connect(URL_LOGIN);
		// 伪造请求头
		connect.header("Accept", "application/json, text/javascript, */*; q=0.01").header("Accept-Encoding",
				"gzip, deflate");
		connect.header("Accept-Language", "zh-CN,zh;q=0.9").header("Connection", "keep-alive");
		connect.header("Content-Length", "72").header("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		connect.header("Host", "qiaoliqiang.cn").header("Referer", "http://qiaoliqiang.cn/Exam/");
		connect.header("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
				.header("X-Requested-With", "XMLHttpRequest");

		// 携带登陆账号密码
		connect.data("managerName", "test").data("password", "9b8c343a439678df948e526aea5d5ad6");

		try {
			Response res = connect.ignoreContentType(true).method(Method.POST).execute();
			// 获取返回的cookie
			cookies = res.cookies();

			String responseInfo = res.body();
			LOGGER.debug("登陆信息 -> {}", responseInfo);

			if (StringUtils.isNotBlank(responseInfo) && responseInfo.indexOf("登录成功") > 0) {
				return true;
			}
		} catch (IOException e) {
			LOGGER.error("登陆错误", e);
		}

		return false;
	}

	private static void logout() {
		LOGGER.debug("开始登出");

		try {
			Document document = Jsoup.connect(URL_LOGOUT).ignoreContentType(true).cookies(cookies).post();
			System.out.println(document.toString());
		} catch (IOException e) {
			LOGGER.error("登出error", e);
		}
	}

	private static String requestURL(String url) {
		try {
			Document document = Jsoup.connect(url).ignoreContentType(true).cookies(cookies).get();
			return document.toString();
		} catch (IOException e) {
			LOGGER.error("请求 url ->{} error", url, e);
			return "";
		}
	}

	/**
	 * 根据标签提取内容
	 * 
	 * @param content
	 * @param tag
	 * @return
	 */
	private static String extractContentByTag(String content, String tag) {
		if (StringUtils.isBlank(content)) {
			return "";
		}

		Document doc = Jsoup.parse(content);
		Elements links = doc.getElementsByTag(tag);
		if (links == null || links.isEmpty()) {
			return "";
		}

		List<String> result = new ArrayList<>();
		for (Element ele : links) {
			result.add(ele.text());
		}

		return StringUtils.join(result, "");
	}

	public static void doCrawLastData() {
		Date yesterday = DateUtils.addDays(new Date(), -1);
		String yesterdayStr = DateFormatUtils.format(yesterday, "yyyy-MM-dd");
		doCrawData(yesterdayStr, yesterdayStr);
	}

}
