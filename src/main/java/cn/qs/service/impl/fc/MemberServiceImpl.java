package cn.qs.service.impl.fc;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qs.bean.fc.Member;
import cn.qs.mapper.fc.MemberMapper;
import cn.qs.service.fc.MemberService;
import cn.qs.utils.BeanUtils;

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
		Member systemBean = new Member();
		if (t.getId() != null) {
			systemBean = memberMapper.findOne(t.getId());
		}

		BeanUtils.copyProperties(systemBean, t);
		memberMapper.saveAndFlush(systemBean);
	}

	@Override
	public Member findByUserIdAndSyncDate(String userId, String syncDate) {
		return memberMapper.findByUserIdAndSyncDate(userId, syncDate);
	}
}
