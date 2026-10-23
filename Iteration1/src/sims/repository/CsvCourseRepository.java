package sims.repository;

import sims.model.Course;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvCourseRepository implements CourseRepository {
    private static final String FILE_PATH = "data/courses.csv";

    @Override
    public List<Course> findAll() {
        List<Course> list = new ArrayList<>();
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
                    list.add(new Course(parts[0].trim(), parts[1].trim()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Course findByCode(String code) {
        for (Course c : findAll()) {
            if (c.getCode().equals(code)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public void save(Course course) {
        List<Course> list = findAll();
        list.add(course);
        writeAll(list);
    }

    @Override
    public void update(Course course) {
        List<Course> list = findAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCode().equals(course.getCode())) {
                list.set(i, course);
                writeAll(list);
                return;
            }
        }
    }

    @Override
    public void delete(String code) {
        List<Course> list = findAll();
        list.removeIf(c -> c.getCode().equals(code));
        writeAll(list);
    }

    private void writeAll(List<Course> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write("code,title");
            bw.newLine();
            for (Course c : list) {
                bw.write(c.getCode() + "," + c.getTitle());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}