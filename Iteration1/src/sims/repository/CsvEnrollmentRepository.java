package sims.repository;

import sims.model.Enrollment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvEnrollmentRepository implements EnrollmentRepository {
    private static final String FILE_PATH = "data/enrollments.csv";

    @Override
    public List<Enrollment> findAll() {
        List<Enrollment> list = new ArrayList<>();
        if (!Files.exists(Paths.get(FILE_PATH))) {
            return list;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",", 2);
                if (parts.length >= 2) {
                    list.add(new Enrollment(parts[0].trim(), parts[1].trim()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Enrollment> findByStudentId(String studentId) {
        List<Enrollment> result = new ArrayList<>();
        for (Enrollment e : findAll()) {
            if (e.getStudentId().equals(studentId)) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public List<Enrollment> findByCourseCode(String courseCode) {
        List<Enrollment> result = new ArrayList<>();
        for (Enrollment e : findAll()) {
            if (e.getCourseCode().equals(courseCode)) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public boolean exists(String studentId, String courseCode) {
        for (Enrollment e : findAll()) {
            if (e.getStudentId().equals(studentId) && e.getCourseCode().equals(courseCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void save(Enrollment enrollment) {
        List<Enrollment> list = findAll();
        list.add(enrollment);
        writeAll(list);
    }

    @Override
    public void delete(String studentId, String courseCode) {
        List<Enrollment> list = findAll();
        list.removeIf(e -> e.getStudentId().equals(studentId) && e.getCourseCode().equals(courseCode));
        writeAll(list);
    }

    @Override
    public void deleteByStudentId(String studentId) {
        List<Enrollment> list = findAll();
        list.removeIf(e -> e.getStudentId().equals(studentId));
        writeAll(list);
    }

    @Override
    public void deleteByCourseCode(String courseCode) {
        List<Enrollment> list = findAll();
        list.removeIf(e -> e.getCourseCode().equals(courseCode));
        writeAll(list);
    }

    private void writeAll(List<Enrollment> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write("studentId,courseCode");
            bw.newLine();
            for (Enrollment e : list) {
                bw.write(e.getStudentId() + "," + e.getCourseCode());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}