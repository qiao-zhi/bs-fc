package cn.qs.service.impl.fc;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qs.bean.fc.FirstCharge;
import cn.qs.mapper.fc.FirstChargeMapper;
import cn.qs.service.fc.FirstChargeService;
import cn.qs.utils.BeanUtils;

@Service
public class FirstChargeServiceImpl implements FirstChargeService {

	@Autowired
	private FirstChargeMapper firstChargeMapper;

	@Override
	public void add(FirstCharge t) {
		firstChargeMapper.save(t);
	}

	@Override
	public void delete(Integer id) {
		firstChargeMapper.delete(id);
	}

	@Override
	public FirstCharge findById(Integer id) {
		return firstChargeMapper.findOne(id);
	}

	@Override
	public List<FirstCharge> listByCondition(Map condition) {
		return null;
	}

	@Override
	public void update(FirstCharge t) {
		FirstCharge systemBean = new FirstCharge();
		if (t.getId() != null) {
			systemBean = firstChargeMapper.findOne(t.getId());
		}

		BeanUtils.copyProperties(systemBean, t);
		firstChargeMapper.save(systemBean);
	}

	@Override
	public FirstCharge findByUserId(String userId) {
		return firstChargeMapper.findByUserId(userId);
	}

}
