package cn.qs.service.impl.fc;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qs.bean.fc.LoginLog;
import cn.qs.mapper.fc.LoginLogMapper;
import cn.qs.service.fc.LoginLogService;

@Service
public class LoginLogServiceImpl implements LoginLogService {

	@Autowired
	private LoginLogMapper loginLogMapper;

	@Override
	public void add(LoginLog t) {
		loginLogMapper.save(t);
	}

	@Override
	public void delete(Integer id) {
		loginLogMapper.delete(id);
	}

	@Override
	public LoginLog findById(Integer id) {
		return loginLogMapper.findOne(id);
	}

	@Override
	public List<LoginLog> listByCondition(Map condition) {
		return null;
	}

	@Override
	public void update(LoginLog t) {

	}

}
