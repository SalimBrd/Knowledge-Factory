package com.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.api.model.BookOrder;
import com.api.repositories.BookOrderRepository;

@Service
public class BookOrderService implements IModelService<BookOrder> {

	@Autowired
	private BookOrderRepository bookOrderRepository;
	
	@Override
	public List<BookOrder> getList(Integer page, Integer limit) {
		PageRequest pageable = PageRequest.of(page, limit);
		return bookOrderRepository.GetListByPage(pageable);
	}

	@Override
	public BookOrder getOneById(Integer id) {
		Optional<BookOrder> bookOrder = bookOrderRepository.findById(id);
		
		if (bookOrder.isEmpty())
			return null;
		
		return bookOrder.get();
	}
	
	public List<BookOrder> getListForUser(Integer page, Integer limit, Integer userId) {
		PageRequest pageable = PageRequest.of(page, limit);
		return bookOrderRepository.GetListByPageForUser(pageable, userId);
	}

	@Override
	public BookOrder create(BookOrder entity) {
		bookOrderRepository.save(entity);
		return entity;
	}

	@Override
	public boolean delete(Integer id) {
		Optional<BookOrder> bookOrder = bookOrderRepository.findById(id);
		
		if (bookOrder.isEmpty())
			return false;
		
		try {
			bookOrderRepository.delete(bookOrder.get());
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}

	@Override
	public BookOrder update(Integer id, BookOrder entity) {
		return null;
	}
}
