package cn.qs.service.impl.fc;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qs.bean.fc.Member;
import cn.qs.mapper.fc.MemberMapper;
import cn.qs.service.fc.MemberService;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberMapper memberMapper;

	@Override
	public void add(Member t) {
		memberMapper.save(t);
	}

	@Override
	public void delete(Integer id) {
		memberMapper.delete(id);
	}

	@Override
	public Member findById(Integer id) {
		return memberMapper.findOne(id);
	}

	@Override
	public List<Member> listByCondition(Map condition) {
		return null;
	}

	@Override
	public void update(Member t) {
		memberMapper.saveAndFlush(t);
	}

	@Override
	public Member findByUserIdAndSyncDate(String userId, String syncDate) {
		return memberMapper.findByUserIdAndSyncDate(userId, syncDate);
	}
}
