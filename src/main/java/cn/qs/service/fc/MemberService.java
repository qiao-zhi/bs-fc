package cn.qs.service.fc;

import cn.qs.bean.fc.Member;
import cn.qs.service.BaseService;

public interface MemberService extends BaseService<Member, Integer> {

	Member findByUserIdAndSyncDate(String userId, String syncDate);
}
