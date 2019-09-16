package cn.qs.service.fc;

import java.util.List;
import java.util.Map;

public interface FirstChargeReportService {

	/**
	 * 组合条件查询会员留存
	 * 
	 * @param condition
	 *            gmtCreated:首充日期; syncDates:会员投注日期
	 * @return
	 */
	List<Map<String, Object>> listFirstChargeReport(Map<String, Object> condition);

	/**
	 * 组合条件查询累计充值报表
	 * 
	 * @param tmpCondition
	 * @return
	 */
	List<Map<String, Object>> listTotalChargeReport(Map<String, Object> tmpCondition);

	/**
	 * 高级版留存查询
	 * 
	 * @param tmpCondition
	 * @return
	 */
	List<Map<String, Object>> listFirstChargeReport2(Map<String, Object> tmpCondition);
}
