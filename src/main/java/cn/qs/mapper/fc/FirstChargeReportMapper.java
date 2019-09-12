package cn.qs.mapper.fc;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FirstChargeReportMapper {

	List<Map<String, Object>> listFirstChargeReport(Map<String, Object> condition);

	List<Map<String, Object>> listTotalChargeReport(Map<String, Object> tmpCondition);

}
