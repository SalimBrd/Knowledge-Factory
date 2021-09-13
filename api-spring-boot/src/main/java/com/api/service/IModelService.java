package com.api.service;

import java.util.List;

public interface IModelService<T> {
	public List<T> getList(Integer page, Integer limit);
	public T getOneById(Integer id);
	public T create(T entity);
	public T update(Integer id, T entity);
	public boolean delete(Integer id);
}
