package sims.ui;

import sims.service.StudentService;

public class StudentMenu {
    private StudentService studentService = new StudentService();

    public void displayMenu() {
        while (true) {
            System.out.println("----- Student Management -----");
            System.out.println("1. Add Student");
            System.out.println("2. Update Student");
            System.out.println("3. Delete Student");
            System.out.println("4. View All Students");
            System.out.println("5. Search Student by ID");
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
                String id = MainMenu.scanner.nextLine();
                System.out.print("Name: ");
                String name = MainMenu.scanner.nextLine();
                if (studentService.addStudent(id, name)) {
                    System.out.println("Student added successfully.");
                }
                return true;
            case "2":
                System.out.print("Student ID: ");
                String uid = MainMenu.scanner.nextLine();
                System.out.print("New Name: ");
                String uname = MainMenu.scanner.nextLine();
                if (studentService.updateStudent(uid, uname)) {
                    System.out.println("Student updated successfully.");
                }
                return true;
            case "3":
                System.out.print("Student ID: ");
                String did = MainMenu.scanner.nextLine();
                if (studentService.deleteStudent(did)) {
                    System.out.println("Student deleted (enrollments cascade deleted).");
                }
                return true;
            case "4":
                var all = studentService.getAllStudents();
                if (all.isEmpty()) {
                    System.out.println("No student records.");
                } else {
                    all.forEach(System.out::println);
                }
                return true;
            case "5":
                System.out.print("Student ID: ");
                String qid = MainMenu.scanner.nextLine();
                var s = studentService.searchStudent(qid);
                if (s == null) {
                    System.out.println("Student not found: " + qid);
                } else {
                    System.out.println(s);
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
