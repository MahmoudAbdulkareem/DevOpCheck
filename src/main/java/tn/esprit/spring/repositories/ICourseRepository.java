package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.TypeCourse;

import java.util.List;

public interface ICourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByLevel(int level);

    List<Course> findByTypeCourse(TypeCourse typeCourse);
}
