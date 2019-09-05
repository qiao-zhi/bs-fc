package cn.qs.mapper.fc;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qs.bean.fc.Member;

public interface MemberMapper extends JpaRepository<Member, Integer> {

}
