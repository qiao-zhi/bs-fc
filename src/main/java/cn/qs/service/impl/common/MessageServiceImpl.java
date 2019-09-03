package cn.qs.service.impl.common;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qs.bean.common.Message;
import cn.qs.mapper.common.MessageMapper;
import cn.qs.service.common.MessageService;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageMapper messageMapper;

	@Override
	public void insert(Message message) {
		messageMapper.save(message);
	}

	@Override
	public Message getMessageDetail(Integer blogId) {
		return messageMapper.findOne(blogId);
	}

	@Override
	public void deleteMessage(int id) {
		messageMapper.delete(id);
	}

	@Override
	public void updateMessage(Message message) {
		messageMapper.save(message);
	}

	@Override
	public Page<Message> getMessageServicePage(Map condition) {
		// 构造请求参数，页号从0开始。
		int pageNum = MapUtils.getInteger(condition, "pageNum", 0);
		int pageSize = MapUtils.getInteger(condition, "pageSize", 0);
		Pageable pageRequest = new QPageRequest(pageNum, pageSize);

		Page<Message> page = messageMapper.findAll(pageRequest);
		return page;
	}

}
