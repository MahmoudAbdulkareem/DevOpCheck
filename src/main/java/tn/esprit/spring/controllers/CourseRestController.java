package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.services.ICourseServices;

import java.util.List;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseRestController {

    private final ICourseServices courseServices;

    @Operation(description = "Add Course")
    @PostMapping("/add")
    public Course addCourse(@RequestBody Course course) {
        return courseServices.addCourse(course);
    }

    @Operation(description = "Retrieve all Courses")
    @GetMapping("/all")
    public List<Course> getAllCourses() {
        return courseServices.retrieveAllCourses();
    }

    @Operation(description = "Update Course")
    @PutMapping("/update")
    public Course updateCourse(@RequestBody Course course) {
        return courseServices.updateCourse(course);
    }

    @Operation(description = "Retrieve Course by Id")
    @GetMapping("/get/{id-course}")
    public Course getById(@PathVariable("id-course") Long numCourse) {
        return courseServices.retrieveCourse(numCourse);
    }

    @Operation(description = "Filter Courses by Level")
    @GetMapping("/filter/level/{level}")
    public List<Course> filterCoursesByLevel(@PathVariable("level") int level) {
        return courseServices.filterCoursesByLevel(level);
    }

    @Operation(description = "Search Courses by Type")
    @GetMapping("/search/type/{typeCourse}")
    public List<Course> searchCoursesByType(@PathVariable("typeCourse") TypeCourse typeCourse) {
        return courseServices.searchCoursesByType(typeCourse);
    }

    @Operation(description = "Calculate Total Price of Courses")
    @GetMapping("/calculate/totalPrice")
    public Float calculateTotalPrice() {
        List<Course> allCourses = courseServices.retrieveAllCourses();
        return courseServices.calculateTotalPrice(allCourses);
    }
}
