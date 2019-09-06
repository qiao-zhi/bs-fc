package cn.qs.service.impl.fc;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qs.mapper.fc.FirstChargeReportMapper;
import cn.qs.service.fc.FirstChargeReportService;

@Service
public class FirstChargeReportServiceImpl implements FirstChargeReportService {

	@Autowired
	private FirstChargeReportMapper firstChargeReportMapper;

	@Override
	public List<Map<String, Object>> listFirstChargeReport(Map<String, Object> condition) {
		return firstChargeReportMapper.listFirstChargeReport(condition);
	}

}
