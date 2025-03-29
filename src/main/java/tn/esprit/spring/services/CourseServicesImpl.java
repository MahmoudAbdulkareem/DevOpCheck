package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;

import java.util.List;

@AllArgsConstructor
@Service
@Primary
public class CourseServicesImpl implements ICourseServices {

    private final ICourseRepository courseRepository;

    @Override
    public List<Course> retrieveAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course retrieveCourse(Long numCourse) {
        return courseRepository.findById(numCourse)
                .orElseThrow(() -> new EmptyResultDataAccessException("Course not found", 1)); // This should throw the exception
    }

    public List<Course> filterCoursesByLevel(int level) {
        return courseRepository.findByLevel(level);
    }

    public List<Course> searchCoursesByType(TypeCourse typeCourse) {
        return courseRepository.findByTypeCourse(typeCourse);
    }

    public Float calculateTotalPrice(List<Course> courses) {
        return courses.stream()
                .map(Course::getPrice)
                .reduce(0.0f, Float::sum);
    }
}
