package br.com.reschoene.course.endpoint.service;

import br.com.reschoene.course.model.Course;
import br.com.reschoene.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {
    private final CourseRepository courseRepository;

    public Iterable<Course> list(Pageable pageable){
        log.info("Listing all courses");
        return courseRepository.findAll(pageable);
    }
}
