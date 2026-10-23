package sims.ui;

import sims.service.CourseService;

public class CourseMenu {
    private CourseService courseService = new CourseService();

    public void displayMenu() {
        while (true) {
            System.out.println("----- Course Management -----");
            System.out.println("1. Add Course");
            System.out.println("2. Update Course");
            System.out.println("3. Delete Course");
            System.out.println("4. View All Courses");
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
                System.out.print("Course Code: ");
                String code = MainMenu.scanner.nextLine();
                System.out.print("Title: ");
                String title = MainMenu.scanner.nextLine();
                if (courseService.addCourse(code, title)) {
                    System.out.println("Course added successfully.");
                }
                return true;
            case "2":
                System.out.print("Course Code: ");
                String ucode = MainMenu.scanner.nextLine();
                System.out.print("New Title: ");
                String utitle = MainMenu.scanner.nextLine();
                if (courseService.updateCourse(ucode, utitle)) {
                    System.out.println("Course updated successfully.");
                }
                return true;
            case "3":
                System.out.print("Course Code: ");
                String dcode = MainMenu.scanner.nextLine();
                if (courseService.deleteCourse(dcode)) {
                    System.out.println("Course deleted (enrollments cascade deleted).");
                }
                return true;
            case "4":
                var all = courseService.getAllCourses();
                if (all.isEmpty()) {
                    System.out.println("No course records.");
                } else {
                    all.forEach(System.out::println);
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
