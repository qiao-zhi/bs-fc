package cn.qs.service;

import java.util.List;
import java.util.Map;

/**
 * 公共的service接口
 * 
 * @author Administrator
 *
 * @param <T>
 *            bean实体
 * @param <E>
 *            ID类型
 */
public interface BaseService<T, E> {
	void add(T t);

	void delete(E id);

	T findById(E id);

	List<T> listByCondition(Map condition);

	void update(T t);
}
