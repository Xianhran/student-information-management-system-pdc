package sims.repository;

import sims.model.Course;

import java.util.List;

public interface CourseRepository {
    List<Course> findAll();

    Course findByCode(String code);

    void save(Course course);

    void update(Course course);

    void delete(String code);
}