package cn.qs.mapper.fc.custom;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FixedReportCustomMapper {

	List<Map<String, Object>> listByCondition(Map condition);

}
