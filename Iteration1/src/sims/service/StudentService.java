package sims.service;

import sims.model.Student;
import sims.repository.CsvEnrollmentRepository;
import sims.repository.CsvStudentRepository;

import java.util.List;

public class StudentService {
    private CsvStudentRepository studentRepo = new CsvStudentRepository();
    private CsvEnrollmentRepository enrollmentRepo = new CsvEnrollmentRepository();

    public boolean addStudent(String id, String name) {
        if (id == null || id.trim().isEmpty()) {
            System.out.println("Error: student ID cannot be empty");
            return false;
        }
        if (studentRepo.findById(id) != null) {
            System.out.println("Error: student ID already exists: " + id);
            return false;
        }
        try {
            studentRepo.save(new Student(id, name));
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStudent(String id, String name) {
        if (studentRepo.findById(id) == null) {
            System.out.println("Error: student ID not found: " + id);
            return false;
        }
        try {
            studentRepo.update(new Student(id, name));
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteStudent(String id) {
        try {
            enrollmentRepo.deleteByStudentId(id);
            studentRepo.delete(id);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public Student searchStudent(String id) {
        return studentRepo.findById(id);
    }
}
