package sims.service;

import sims.model.Enrollment;
import sims.repository.CsvCourseRepository;
import sims.repository.CsvEnrollmentRepository;
import sims.repository.CsvStudentRepository;

import java.util.List;

public class EnrollmentService {
    private CsvEnrollmentRepository enrollmentRepo = new CsvEnrollmentRepository();
    private CsvStudentRepository studentRepo = new CsvStudentRepository();
    private CsvCourseRepository courseRepo = new CsvCourseRepository();

    public boolean enroll(String studentId, String courseCode) {
        if (studentRepo.findById(studentId) == null) {
            System.out.println("Error: student not found: " + studentId);
            return false;
        }
        if (courseRepo.findByCode(courseCode) == null) {
            System.out.println("Error: course not found: " + courseCode);
            return false;
        }
        if (enrollmentRepo.exists(studentId, courseCode)) {
            System.out.println("Error: student already enrolled in this course");
            return false;
        }
        try {
            enrollmentRepo.save(new Enrollment(studentId, courseCode));
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean drop(String studentId, String courseCode) {
        if (!enrollmentRepo.exists(studentId, courseCode)) {
            System.out.println("Error: enrollment record not found");
            return false;
        }
        try {
            enrollmentRepo.delete(studentId, courseCode);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        return enrollmentRepo.findByStudentId(studentId);
    }

    public List<Enrollment> getStudentsByCourse(String courseCode) {
        return enrollmentRepo.findByCourseCode(courseCode);
    }
}
