import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

class Student {
    public String student_id;
    public String name;
    public String email;
    public List<Course> courses_enrolled;

    public Student(String student_id, String name, String email) {
        this.student_id = student_id;
        this.name = name;
        this.email = email;
        this.courses_enrolled = new ArrayList<>();
    }

    public void enrollCourse(Course course) {
        courses_enrolled.add(course);
    }

    public void dropCourse(Course course) {
        courses_enrolled.remove(course);
    }

    public List<Course> viewCourses() {
        return courses_enrolled;
    }

    public String getName() {
        return name;
    }
}

class Teacher {
    public String teacher_id;
    public String name;
    public String email;
    public List<Course> courses_taught;

    public Teacher(String teacher_id, String name, String email) {
        this.teacher_id = teacher_id;
        this.name = name;
        this.email = email;
        this.courses_taught = new ArrayList<>();
    }

    public void assignCourse(Course course) {
        courses_taught.add(course);
    }

    public void removeCourse(Course course) {
        courses_taught.remove(course);
    }

    public List<Course> viewCourses() {
        return courses_taught;
    }

    public String getName() {
        return name;
    }
}

class Course {
    public String course_code;
    public String course_name;
    public Teacher teacher;
    public List<Student> students_enrolled;

    public Course(String course_code, String course_name) {
        this.course_code = course_code;
        this.course_name = course_name;
        this.students_enrolled = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students_enrolled.add(student);
    }

    public void removeStudent(Student student) {
        students_enrolled.remove(student);
    }

    public List<Student> viewStudents() {
        return students_enrolled;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getCourseName() {
        return course_name;
    }
}

class Room {
    public String room_number;
    public int capacity;

    public Room(String room_number, int capacity) {
        this.room_number = room_number;
        this.capacity = capacity;
    }

    public boolean checkAvailability() {
        // Logic to check room availability
        return true; // Placeholder return
    }

    public void bookRoom() {
        // Logic to book the room
    }
}

class UniversityManagementSystem {
    private Map<String, Student> students;
    private Map<String, Teacher> teachers;
    private Map<String, Course> courses;
    private Map<String, Room> rooms;

    public UniversityManagementSystem() {
        this.students = new HashMap<>();
        this.teachers = new HashMap<>();
        this.courses = new HashMap<>();
        this.rooms = new HashMap<>();
    }

    public void addStudent(Student student) {
        students.put(student.student_id, student);
    }

    public void addTeacher(Teacher teacher) {
        teachers.put(teacher.teacher_id, teacher);
    }

    public void addCourse(Course course) {
        courses.put(course.course_code, course);
    }

    public void addRoom(Room room) {
        rooms.put(room.room_number, room);
    }

    public void saveData() {
        try (FileWriter file = new FileWriter("students.json")) {
            JSONArray studentList = new JSONArray();
            for (Student student : students.values()) {
                JSONObject studentObj = new JSONObject();
                studentObj.put("student_id", student.student_id);
                studentObj.put("name", student.name);
                studentObj.put("email", student.email);
                studentList.add(studentObj);
            }
            file.write(studentList.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Similar logic for teachers, courses, and rooms
    }

    public void loadData() {
        JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader("students.json");
            JSONArray studentList = (JSONArray) parser.parse(reader);
            for (Object obj : studentList) {
                JSONObject studentObj = (JSONObject) obj;
                String student_id = (String) studentObj.get("student_id");
                String name = (String) studentObj.get("name");
                String email = (String) studentObj.get("email");
                Student student = new Student(student_id, name, email);
                students.put(student_id, student);
            }
            // Similar logic for teachers, courses, and rooms
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public Student findStudentById(String student_id) {
        return students.get(student_id);
    }

    public Course findCourseByCode(String course_code) {
        return courses.get(course_code);
    }

    public void enrollStudentInCourse(Student student, Course course) {
        course.addStudent(student);
        student.enrollCourse(course);
        System.out.println("Student " + student.getName() + " enrolled in course " + course.getCourseName());
    }

    public void assignTeacherToCourse(Teacher teacher, Course course) {
        course.setTeacher(teacher);
        teacher.assignCourse(course);
        System.out.println("Teacher " + teacher.getName() + " assigned to course " + course.getCourseName());
    }

    public Teacher findTeacherById(String teacher_id) {
        return teachers.get(teacher_id);
    }
}

public class Main {
    public static void main(String[] args) {
        UniversityManagementSystem ums = new UniversityManagementSystem();
        ums.loadData(); // Load existing data

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("\nUniversity Management System");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Assign Teacher to Course");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    enrollStudentInCourse(ums, scanner);
                    break;
                case 2:
                    assignTeacherToCourse(ums, scanner);
                    break;
                case 3:
                    ums.saveData(); // Save data before exiting
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    public static void assignTeacherToCourse(UniversityManagementSystem ums, Scanner scanner) {
        System.out.println("\nAssign Teacher to Course");
        System.out.print("Enter teacher ID: ");
        String teacherId = scanner.nextLine();
        Teacher teacher = ums.findTeacherById(teacherId);
        if (teacher == null) {
            System.out.println("Teacher not found with ID: " + teacherId);
            return;
        }

        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine();
        Course course = ums.findCourseByCode(courseCode);
        if (course == null) {
            System.out.println("Course not found with code: " + courseCode);
            return;
        }

        ums.assignTeacherToCourse(teacher, course);
    }
    public static void enrollStudentInCourse(UniversityManagementSystem ums, Scanner scanner) {
        System.out.println("\nEnroll Student in Course");
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();
        Student student = ums.findStudentById(studentId);
        if (student == null) {
            System.out.println("Student not found with ID: " + studentId);
            return;
        }

        System.out.print("Enter course code: ");



    }
}

       
