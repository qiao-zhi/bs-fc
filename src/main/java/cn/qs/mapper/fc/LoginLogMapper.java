package cn.qs.mapper.fc;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qs.bean.fc.LoginLog;

public interface LoginLogMapper extends JpaRepository<LoginLog, Integer> {

}
