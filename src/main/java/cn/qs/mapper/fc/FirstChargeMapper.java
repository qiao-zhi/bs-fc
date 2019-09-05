package cn.qs.mapper.fc;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qs.bean.fc.FirstCharge;

public interface FirstChargeMapper extends JpaRepository<FirstCharge, Integer> {

	FirstCharge findByUserId(String userId);
}
