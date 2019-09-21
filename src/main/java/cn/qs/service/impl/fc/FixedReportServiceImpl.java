package cn.qs.service.impl.fc;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qs.bean.fc.FixedReport;
import cn.qs.mapper.fc.FixedReportMapper;
import cn.qs.mapper.fc.custom.FixedReportCustomMapper;
import cn.qs.service.fc.FixedReportService;
import cn.qs.utils.BeanUtils;

@Service
public class FixedReportServiceImpl implements FixedReportService {

	@Autowired
	private FixedReportMapper fixedReportMapper;

	@Autowired
	private FixedReportCustomMapper fixedReportCustomMapper;

	@Override
	public void add(FixedReport t) {
		fixedReportMapper.save(t);
	}

	@Override
	public void delete(Integer id) {
		fixedReportMapper.delete(id);
	}

	@Override
	public FixedReport findById(Integer id) {
		return fixedReportMapper.findOne(id);
	}

	@Override
	public List<Map<String, Object>> listMapsByCondition(Map condition) {
		return fixedReportCustomMapper.listByCondition(condition);
	}

	@Override
	public void update(FixedReport t) {
		FixedReport systemBean = new FixedReport();
		if (t.getId() != null) {
			systemBean = fixedReportMapper.findOne(t.getId());
		}

		BeanUtils.copyProperties(systemBean, t);
		fixedReportMapper.save(systemBean);
	}

	@Override
	public FixedReport findBySyncDate(String syncDate) {
		return fixedReportMapper.findBySyncDate(syncDate);
	}

	@Override
	public List<FixedReport> listByCondition(Map condition) {
		// TODO Auto-generated method stub
		return null;
	}

}
