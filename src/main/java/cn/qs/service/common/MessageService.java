package cn.qs.service.common;

import java.util.Map;

import org.springframework.data.domain.Page;

import cn.qs.bean.common.Message;

public interface MessageService {
	void insert(Message message);

	Page<Message> getMessageServicePage(Map condition);

	Message getMessageDetail(Integer blogId);

	void deleteMessage(int id);

	void updateMessage(Message message);
}
