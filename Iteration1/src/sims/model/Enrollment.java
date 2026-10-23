package sims.model;

public class Enrollment {
    private String studentId;
    private String courseCode;

    public Enrollment() {
    }

    public Enrollment(String studentId, String courseCode) {
        this.studentId = studentId;
        this.courseCode = courseCode;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    @Override
    public String toString() {
        return "Enrollment{studentId=" + studentId + ", courseCode=" + courseCode + "}";
    }
}