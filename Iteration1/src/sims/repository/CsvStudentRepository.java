package sims.repository;

import sims.model.Student;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvStudentRepository implements StudentRepository {
    private static final String FILE_PATH = "data/students.csv";

    @Override
    public List<Student> findAll() {
        List<Student> list = new ArrayList<>();
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
                    list.add(new Student(parts[0].trim(), parts[1].trim()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Student findById(String id) {
        for (Student s : findAll()) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public void save(Student student) {
        List<Student> list = findAll();
        list.add(student);
        writeAll(list);
    }

    @Override
    public void update(Student student) {
        List<Student> list = findAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(student.getId())) {
                list.set(i, student);
                writeAll(list);
                return;
            }
        }
    }

    @Override
    public void delete(String id) {
        List<Student> list = findAll();
        list.removeIf(s -> s.getId().equals(id));
        writeAll(list);
    }

    private void writeAll(List<Student> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write("id,name");
            bw.newLine();
            for (Student s : list) {
                bw.write(s.getId() + "," + s.getName());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}