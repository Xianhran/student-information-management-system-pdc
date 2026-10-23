package sims.service;

import sims.model.Course;
import sims.repository.CsvCourseRepository;
import sims.repository.CsvEnrollmentRepository;

import java.util.List;

public class CourseService {
    private CsvCourseRepository courseRepo = new CsvCourseRepository();
    private CsvEnrollmentRepository enrollmentRepo = new CsvEnrollmentRepository();

    public boolean addCourse(String code, String title) {
        if (code == null || code.trim().isEmpty()) {
            System.out.println("Error: course code cannot be empty");
            return false;
        }
        if (courseRepo.findByCode(code) != null) {
            System.out.println("Error: course code already exists: " + code);
            return false;
        }
        try {
            courseRepo.save(new Course(code, title));
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCourse(String code, String title) {
        if (courseRepo.findByCode(code) == null) {
            System.out.println("Error: course code not found: " + code);
            return false;
        }
        try {
            courseRepo.update(new Course(code, title));
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCourse(String code) {
        try {
            enrollmentRepo.deleteByCourseCode(code);
            courseRepo.delete(code);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }
}
