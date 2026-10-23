package sims.repository;

import sims.model.Student;

import java.util.List;

public interface StudentRepository {
    List<Student> findAll();

    Student findById(String id);

    void save(Student student);

    void update(Student student);

    void delete(String id);
}