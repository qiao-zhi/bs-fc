package cn.qs.mapper.common;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qs.bean.common.Message;

public interface MessageMapper extends JpaRepository<Message, Integer> {

}