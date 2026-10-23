package sims.ui;

import sims.service.EnrollmentService;

public class EnrollmentMenu {
    private EnrollmentService enrollmentService = new EnrollmentService();

    public void displayMenu() {
        while (true) {
            System.out.println("----- Enrollment Management -----");
            System.out.println("1. Enroll");
            System.out.println("2. Drop");
            System.out.println("3. View Enrollments by Student");
            System.out.println("4. View Enrollments by Course");
            System.out.println("0. Back");
            if (!handleUserInput()) {
                break;
            }
        }
    }

    public boolean handleUserInput() {
        System.out.print("Choose: ");
        String choice = MainMenu.scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.print("Student ID: ");
                String sid = MainMenu.scanner.nextLine();
                System.out.print("Course Code: ");
                String cid = MainMenu.scanner.nextLine();
                if (enrollmentService.enroll(sid, cid)) {
                    System.out.println("Enrolled successfully.");
                }
                return true;
            case "2":
                System.out.print("Student ID: ");
                String dsid = MainMenu.scanner.nextLine();
                System.out.print("Course Code: ");
                String dcid = MainMenu.scanner.nextLine();
                if (enrollmentService.drop(dsid, dcid)) {
                    System.out.println("Dropped successfully.");
                }
                return true;
            case "3":
                System.out.print("Student ID: ");
                String qsid = MainMenu.scanner.nextLine();
                var byStudent = enrollmentService.getEnrollmentsByStudent(qsid);
                if (byStudent.isEmpty()) {
                    System.out.println("No enrollment records for this student.");
                } else {
                    byStudent.forEach(System.out::println);
                }
                return true;
            case "4":
                System.out.print("Course Code: ");
                String qcid = MainMenu.scanner.nextLine();
                var byCourse = enrollmentService.getStudentsByCourse(qcid);
                if (byCourse.isEmpty()) {
                    System.out.println("No enrollment records for this course.");
                } else {
                    byCourse.forEach(System.out::println);
                }
                return true;
            case "0":
                return false;
            default:
                System.out.println("Invalid option, please try again.");
                return true;
        }
    }
}
