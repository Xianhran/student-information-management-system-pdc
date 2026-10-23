package sims.ui;

import java.util.Scanner;

public class MainMenu {
    static Scanner scanner = new Scanner(System.in);
    private StudentMenu studentMenu = new StudentMenu();
    private CourseMenu courseMenu = new CourseMenu();
    private EnrollmentMenu enrollmentMenu = new EnrollmentMenu();

    public void displayMainMenu() {
        while (true) {
            System.out.println("===== Student Information Management System =====");
            System.out.println("1. Student Management");
            System.out.println("2. Course Management");
            System.out.println("3. Enrollment Management");
            System.out.println("0. Exit");
            if (!handleUserInput()) {
                break;
            }
        }
    }

    public boolean handleUserInput() {
        System.out.print("Choose: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                studentMenu.displayMenu();
                return true;
            case "2":
                courseMenu.displayMenu();
                return true;
            case "3":
                enrollmentMenu.displayMenu();
                return true;
            case "0":
                System.out.println("Goodbye!");
                return false;
            default:
                System.out.println("Invalid option, please try again.");
                return true;
        }
    }
}
