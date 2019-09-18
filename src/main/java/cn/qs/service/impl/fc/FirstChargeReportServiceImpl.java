package cn.qs.service.impl.fc;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qs.bean.fc.FirstCharge;
import cn.qs.mapper.fc.FirstChargeMapper;
import cn.qs.mapper.fc.FirstChargeReportMapper;
import cn.qs.service.fc.FirstChargeReportService;
import cn.qs.utils.BeanUtils;

@Service
public class FirstChargeReportServiceImpl implements FirstChargeReportService {

	@Autowired
	private FirstChargeReportMapper firstChargeReportMapper;

	@Autowired
	private FirstChargeMapper firstChargeMapper;

	@Override
	public List<Map<String, Object>> listFirstChargeReport(Map<String, Object> condition) {
		return firstChargeReportMapper.listFirstChargeReport(condition);
	}

	@Override
	public List<Map<String, Object>> listTotalChargeReport(Map<String, Object> tmpCondition) {
		return firstChargeReportMapper.listTotalChargeReport(tmpCondition);
	}

	@Override
	public List<Map<String, Object>> listFirstChargeReport2(Map<String, Object> tmpCondition) {
		return firstChargeReportMapper.listFirstChargeReport2(tmpCondition);
	}

	@Override
	public List<String> listDistinctParentName() {
		return firstChargeReportMapper.listDistinctParentName();
	}

	@Override
	public void update(FirstCharge firstCharge) {
		FirstCharge systemFirstCharge = firstChargeMapper.findOne(firstCharge.getId());
		// 将修改的属性赋值到系统bean上
		BeanUtils.copyProperties(systemFirstCharge, firstCharge);

		firstChargeMapper.saveAndFlush(systemFirstCharge);
	}

}
