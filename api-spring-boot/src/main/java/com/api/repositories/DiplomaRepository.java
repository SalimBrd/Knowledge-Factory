package com.api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.model.Diploma;

public interface DiplomaRepository extends PagingAndSortingRepository<Diploma, Integer>{
	@Query("SELECT d FROM Diploma d ORDER BY d.id DESC")
	List<Diploma> GetListByPage(Pageable pageable);
	
	@Query("SELECT d FROM Diploma d WHERE d.user.id LIKE :userId ORDER BY d.id DESC")
	List<Diploma> GetListByPageForUser(Pageable pageable, Integer userId);

	@Query("SELECT d FROM Diploma d Where d.user.id LIKE :userId AND d.course.id LIKE :courseId")
	Optional<Diploma> getOneByUserAndCourseId(Integer userId, Integer courseId);
	
	@Query("SELECT d FROM Diploma d WHERE d.user.id LIKE :userId AND d.id LIKE :id")
	Optional<Diploma> GetByIdForUser(Integer id, Integer userId);
}
