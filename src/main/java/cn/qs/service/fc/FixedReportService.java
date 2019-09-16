package cn.qs.service.fc;

import java.util.List;
import java.util.Map;

import cn.qs.bean.fc.FixedReport;
import cn.qs.service.BaseService;

public interface FixedReportService extends BaseService<FixedReport, Integer> {

	FixedReport findBySyncDate(String syncDate);

	List<Map<String, Object>> listMapsByCondition(Map condition);
}
