package cn.qs.service.fc;

import cn.qs.bean.fc.FirstCharge;
import cn.qs.service.BaseService;

public interface FirstChargeService extends BaseService<FirstCharge, Integer> {
	
	FirstCharge findByUserId(String userId);
}
