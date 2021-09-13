package com.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.api.model.Book;
import com.api.repositories.BookRepository;

@Service
public class BookService implements IModelService<Book> {

	@Autowired
	private BookRepository bookRepository;
	
	@Override
	public List<Book> getList(Integer page, Integer limit) {
		PageRequest pageable = PageRequest.of(page, limit);
		return bookRepository.GetListByPage(pageable);
	}

	@Override
	public Book getOneById(Integer id) {
		Optional<Book> book = bookRepository.findById(id);
		
		if (book.isEmpty())
			return null;
		
		return book.get();
	}

	@Override
	public Book create(Book entity) {
		bookRepository.save(entity);
		return entity;
	}

	@Override
	public Book update(Integer id, Book entity) {
		Optional<Book> book = bookRepository.findById(id);
		
		if (book.isEmpty())
			return null;
		
		Book bookFound = book.get();
		if(entity.getAuthor() != null)
			bookFound.setAuthor(entity.getAuthor());
		if(entity.getDescription() != null)
			bookFound.setDescription(entity.getDescription());
		if(entity.getPrice() != null)
			bookFound.setPrice(entity.getPrice());
		if(entity.getTitle() != null)
			bookFound.setTitle(entity.getTitle());
		if(entity.isPremium() != null)
			bookFound.setPremium(entity.isPremium());
		
		bookRepository.save(bookFound);
		return bookFound;
	}

	@Override
	public boolean delete(Integer id) {
		Optional<Book> book = bookRepository.findById(id);
		
		if (book.isEmpty())
			return false;
		
		try {
			bookRepository.delete(book.get());
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}
}
