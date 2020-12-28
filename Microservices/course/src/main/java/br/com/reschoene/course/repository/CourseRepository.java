package br.com.reschoene.course.repository;

import br.com.reschoene.course.model.Course;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {
}
