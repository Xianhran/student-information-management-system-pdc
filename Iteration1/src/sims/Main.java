package sims;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import sims.model.Course;
import sims.model.Enrollment;
import sims.model.Student;
import sims.service.CourseService;
import sims.service.EnrollmentService;
import sims.service.StudentService;
import sims.ui.MainMenu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static StudentService studentService = new StudentService();
    private static CourseService courseService = new CourseService();
    private static EnrollmentService enrollmentService = new EnrollmentService();

    public static void main(String[] args) throws IOException {
        if (args.length > 0 && args[0].equals("--cli")) {
            new MainMenu().displayMainMenu();
            return;
        }
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/students", new StudentHandler());
        server.createContext("/api/courses", new CourseHandler());
        server.createContext("/api/enrollments", new EnrollmentHandler());
        server.createContext("/", new StaticHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started: http://localhost:8080");
    }

    private static String toJson(Student s) {
        return "{\"id\":\"" + esc(s.getId()) + "\",\"name\":\"" + esc(s.getName()) + "\"}";
    }

    private static String toJson(Course c) {
        return "{\"code\":\"" + esc(c.getCode()) + "\",\"title\":\"" + esc(c.getTitle()) + "\"}";
    }

    private static String toJson(Enrollment e) {
        return "{\"studentId\":\"" + esc(e.getStudentId()) + "\",\"courseCode\":\"" + esc(e.getCourseCode()) + "\"}";
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String extract(String body, String key) {
        Matcher m = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\"").matcher(body);
        return m.find() ? m.group(1) : null;
    }

    private static String param(String query, String key) {
        if (query == null) return null;
        for (String p : query.split("&")) {
            String[] kv = p.split("=", 2);
            if (kv[0].equals(key)) return kv.length > 1 ? kv[1] : null;
        }
        return null;
    }

    private static String readBody(HttpExchange ex) throws IOException {
        try (InputStream is = ex.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static void send(HttpExchange ex, int code, String resp, String contentType) throws IOException {
        byte[] bytes = resp.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", contentType);
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void sendJson(HttpExchange ex, int code, String json) throws IOException {
        send(ex, code, json, "application/json; charset=UTF-8");
    }

    private static void sendResult(HttpExchange ex, boolean ok) throws IOException {
        sendJson(ex, ok ? 200 : 400, "{\"success\":" + ok + "}");
    }

    static class StudentHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            String method = ex.getRequestMethod();
            String rest = ex.getRequestURI().getPath().substring("/api/students".length());
            if (method.equals("GET") && (rest.isEmpty() || rest.equals("/"))) {
                List<Student> list = studentService.getAllStudents();
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < list.size(); i++) {
                    if (i > 0) sb.append(",");
                    sb.append(toJson(list.get(i)));
                }
                sb.append("]");
                sendJson(ex, 200, sb.toString());
            } else if (method.equals("POST")) {
                String body = readBody(ex);
                sendResult(ex, studentService.addStudent(extract(body, "id"), extract(body, "name")));
            } else if (method.equals("GET")) {
                Student s = studentService.searchStudent(rest.substring(1));
                if (s != null) sendJson(ex, 200, toJson(s));
                else sendJson(ex, 404, "{\"error\":\"not found\"}");
            } else if (method.equals("PUT")) {
                String body = readBody(ex);
                sendResult(ex, studentService.updateStudent(rest.substring(1), extract(body, "name")));
            } else if (method.equals("DELETE")) {
                sendResult(ex, studentService.deleteStudent(rest.substring(1)));
            } else {
                sendJson(ex, 405, "{\"error\":\"method not allowed\"}");
            }
        }
    }

    static class CourseHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            String method = ex.getRequestMethod();
            String rest = ex.getRequestURI().getPath().substring("/api/courses".length());
            if (method.equals("GET") && (rest.isEmpty() || rest.equals("/"))) {
                List<Course> list = courseService.getAllCourses();
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < list.size(); i++) {
                    if (i > 0) sb.append(",");
                    sb.append(toJson(list.get(i)));
                }
                sb.append("]");
                sendJson(ex, 200, sb.toString());
            } else if (method.equals("POST")) {
                String body = readBody(ex);
                sendResult(ex, courseService.addCourse(extract(body, "code"), extract(body, "title")));
            } else if (method.equals("PUT")) {
                String body = readBody(ex);
                sendResult(ex, courseService.updateCourse(rest.substring(1), extract(body, "title")));
            } else if (method.equals("DELETE")) {
                sendResult(ex, courseService.deleteCourse(rest.substring(1)));
            } else {
                sendJson(ex, 405, "{\"error\":\"method not allowed\"}");
            }
        }
    }

    static class EnrollmentHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            String method = ex.getRequestMethod();
            String query = ex.getRequestURI().getQuery();
            if (method.equals("GET")) {
                String studentId = param(query, "studentId");
                String courseCode = param(query, "courseCode");
                List<Enrollment> list;
                if (studentId != null) list = enrollmentService.getEnrollmentsByStudent(studentId);
                else if (courseCode != null) list = enrollmentService.getStudentsByCourse(courseCode);
                else list = enrollmentService.getEnrollmentsByStudent("");
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < list.size(); i++) {
                    if (i > 0) sb.append(",");
                    sb.append(toJson(list.get(i)));
                }
                sb.append("]");
                sendJson(ex, 200, sb.toString());
            } else if (method.equals("POST")) {
                String body = readBody(ex);
                sendResult(ex, enrollmentService.enroll(extract(body, "studentId"), extract(body, "courseCode")));
            } else if (method.equals("DELETE")) {
                sendResult(ex, enrollmentService.drop(param(query, "studentId"), param(query, "courseCode")));
            } else {
                sendJson(ex, 405, "{\"error\":\"method not allowed\"}");
            }
        }
    }

    static class StaticHandler implements HttpHandler {
        public void handle(HttpExchange ex) throws IOException {
            String path = ex.getRequestURI().getPath();
            String file = path.equals("/") ? "web/index.html" : "web" + path;
            if (!Files.exists(Paths.get(file))) {
                byte[] msg = "404 Not Found".getBytes(StandardCharsets.UTF_8);
                ex.sendResponseHeaders(404, msg.length);
                try (OutputStream os = ex.getResponseBody()) { os.write(msg); }
                return;
            }
            byte[] content = Files.readAllBytes(Paths.get(file));
            String ct = path.endsWith(".css") ? "text/css; charset=UTF-8"
                    : path.endsWith(".js") ? "application/javascript; charset=UTF-8"
                    : "text/html; charset=UTF-8";
            ex.getResponseHeaders().set("Content-Type", ct);
            ex.sendResponseHeaders(200, content.length);
            try (OutputStream os = ex.getResponseBody()) { os.write(content); }
        }
    }
}
