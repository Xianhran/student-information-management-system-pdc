package sims.repository;

import sims.model.Enrollment;

import java.util.List;

public interface EnrollmentRepository {
    List<Enrollment> findAll();

    List<Enrollment> findByStudentId(String studentId);

    List<Enrollment> findByCourseCode(String courseCode);

    boolean exists(String studentId, String courseCode);

    void save(Enrollment enrollment);

    void delete(String studentId, String courseCode);

    void deleteByStudentId(String studentId);

    void deleteByCourseCode(String courseCode);
}