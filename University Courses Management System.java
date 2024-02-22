import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
 
 
/**
 * University Courses Management System
 *
 * Version 1.0.0
 *
 * This class manages the University Courses Management System including adding courses, students, professors,
 * enrolling students in courses, dropping students from courses
 * assigning professors to teach courses, exempt professors from teaching courses.
 * @author Ahmed Baha Eddine Alimi
 */
 
public class UniversityCoursesManagementSystem {
    private static boolean error = true;
 
    private static List<Course> courses = new ArrayList<>();
    private static List<Student> students = new ArrayList<>();
    private static List<Professor> professors = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
 
    public static void main(String[] args) {
        fillInitialData();
        processInput();
    }
 
    /**
     * Processes input commands from the user.
     * Uses a loop to continuously accept commands until an error occurs or the input is empty.
     * Available commands: "course", "student", "professor", "enroll", "drop", "exempt", "teach".
     */
    public static void processInput() {
        label:
        while (error) {
            String command;
            if (scanner.hasNextLine()) {
                command = scanner.nextLine();
                command = command.toLowerCase();
            } else {
                break label;
            }
            if (command.isEmpty()) {
                System.out.println("Wrong inputs");
                break;
            }
 
            switch (command) {
                case "course":
                    addCourse();
                    break;
                case "student":
                    addStudent();
                    break;
                case "professor":
                    addProfessor();
                    break;
                case "enroll":
                    enroll();
                    break;
                case "drop":
                    drop();
                    break;
                case "exempt":
                    exempt();
                    break;
                case "teach":
                    teach();
                    break;
                default:
                    System.out.println("Wrong inputs");
                    error = false;
                    break label;
            }
        }
    }
 
    /**
     * Checks if a string made only from letters.
     *
     * @param str string to check
     * @return True if the string contains only letters, if else false
     */
    public static boolean containsOnlyLetters(String str) {
        return str.matches("[a-zA-Z]+");
    }
 
    /**
     * Checks if a string made only from letters or if it contains underscore
     * ensuring there is a letter before and after each underscore
     * @param str string to check
     * @return True if the string contains only letters, if else false
     */
    public static boolean containsOnlyLettersCourse(String str) {
        boolean hasUnderscore = false;
        boolean letterBeforeUnderscore = false;
        for (int i = 0; i < str.length(); i++) {
            char currentChar = str.charAt(i);
            if (currentChar == '_') {
                hasUnderscore = true;
                if (i > 0 && i < str.length() - 1) {
                    char prevChar = str.charAt(i - 1);
                    char nextChar = str.charAt(i + 1);
 
                    if (Character.isLetter(prevChar) && Character.isLetter(nextChar)) {
                        letterBeforeUnderscore = true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        if (hasUnderscore) {
            return hasUnderscore && letterBeforeUnderscore && str.matches("[a-zA-Z_]+");
 
        } else {
            return str.matches("[a-zA-Z_]+");
        }
    }
    /**
     * Adds a new student.
     * Prompts the user to enter the student's name
     * adds them to the list of students if the input is valid and satisfies all its conditions.
     * Prints messages for success or failure.
     */
    public static void addStudent() {
        String memberName;
        if (scanner.hasNextLine()) {
            memberName = scanner.nextLine();
            if (!containsOnlyLetters(memberName)) {
                System.out.println("Wrong inputs");
                error = false;
            }
            if (memberName.equalsIgnoreCase("student")
                    || memberName.equalsIgnoreCase("course")
                    || memberName.equalsIgnoreCase("enroll")
                    || memberName.equalsIgnoreCase("drop")
                    || memberName.equalsIgnoreCase("professor")
                    || memberName.equalsIgnoreCase("teach")
                    || memberName.equalsIgnoreCase("exempt")) {
                System.out.println("Wrong inputs");
                error = false;
            } else {
                Student newStudent = new Student(memberName);
                students.add(newStudent);
                System.out.println("Added successfully");
            }
 
        } else {
            System.out.println("Wrong inputs");
            error = false;
        }
    }
    /**
     * assigns a professor to teach a course.
     * Prompts the user for the professor and course IDs
     * then assigns the professor to teach the course if he is not teaching it already.
     * Prints messages for success or failure.
     *
     * @return True if the professor is successfully assigned to teach the course, if else false
     */
    public static  boolean teach() {
        try {
            String professorIDInput;
            if (scanner.hasNextLine()) {
                professorIDInput = scanner.nextLine();
            } else {
                System.out.println("Wrong inputs");
                error = false;
                return error;
            }
            int professorID = Integer.parseInt(professorIDInput);
            Professor professor = findProfessorByID(professorID);
            if (professor == null) {
                System.out.println("Wrong inputs");
                error = false;
                return error;
            }
            String courseIDInput;
            if (scanner.hasNextLine()) {
                courseIDInput = scanner.nextLine();
            } else {
                System.out.println("Wrong inputs");
                error = false;
                return error;
            }
            int courseID = Integer.parseInt(courseIDInput);
 
            Course course = findCourseByID(courseID);
 
            if (course == null) {
                System.out.println("Wrong inputs");
                error = false;
                return error;
            } else {
                boolean assigned = professor.teach(course);
                if (assigned) {
                    System.out.println("Professor is successfully assigned to teach this course");
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            error = false;
        }
        return true;
    }
 
    /**
     * exempts a professor from teaching a course.
     * Prompts the user for the professor and course IDs
     * then exempts the professor from teaching the course if he is teaching it.
     * Prints messages for success or failure.
     *
     * @return True if the professor is successfully exempted from teaching the course, if else false
     */
    public static  boolean exempt() {
        try {
            String professorIDInput;
            if (scanner.hasNextLine()) {
                professorIDInput = scanner.nextLine();
            } else {
                System.out.println("Wrong inputs");
                error = false;
                return error;
            }
            int professorID = Integer.parseInt(professorIDInput);
            Professor professor = findProfessorByID(professorID);
            if (professor == null) {
                System.out.println("Wrong inputs");
                error = false;
                return error;
            }
            String courseIDInput;
            if (scanner.hasNextLine()) {
                courseIDInput = scanner.nextLine();
            } else {
                System.out.println("Wrong inputs");
                error = false;
                return false;
            }
            int courseID = Integer.parseInt(courseIDInput);
 
            Course course = findCourseByID(courseID);
 
            if (course == null) {
                System.out.println("Wrong inputs");
                error = false;
                return error;
            } else {
                boolean exempted = professor.exempt(course);
                if (exempted) {
                    System.out.println("Professor is exempted");
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            error = false;
            return error;
        }
        return true;
    }
    /**
     * drops a student from a course.
     * Prompts the user for the student and course IDs
     * then drops the student from the course if he is enrolled in it already.
     * Prints messages for success or failure.
     *
     * @return True if the student is successfully dropped from the course, if else false
     */
    public static boolean drop() {
        try {
            String studentIDInput;
            if (scanner.hasNextLine()) {
                studentIDInput = scanner.nextLine();
            } else {
                System.out.println("Wrong inputs");
                error = false;
                return false;
            }
            int studentID = Integer.parseInt(studentIDInput);
            Student student = findStudentByID(studentID);
            if (student == null) {
                System.out.println("Wrong inputs");
                error = false;
                return error;
            }
            String courseIDInput;
            if (scanner.hasNextLine()) {
                courseIDInput = scanner.nextLine();
            } else {
                System.out.println("Wrong inputs");
                error = false;
                return false;
            }
            int courseID = Integer.parseInt(courseIDInput);
 
            Course course = findCourseByID(courseID);
 
            if (course == null) {
                System.out.println("Wrong inputs");
                error = false;
                return error;
            } else {
                boolean dropped = student.drop(course);
                if (dropped) {
                    System.out.println("Dropped successfully");
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            error = false;
        }
        return true;
    }
    /**
     * enrolls a student from a course.
     * Prompts the user for the student and course IDs
     * then enrolls the student in the course if he is not already enrolled in it.
     * Prints messages for success or failure.
     *
     * @return True if the student is successfully dropped from the course, if else false
     */
    public static boolean enroll() {
        try {
            String studentIDInput;
            if (scanner.hasNextLine()) {
                studentIDInput = scanner.nextLine();
            } else {
                System.out.println("Wrong inputs");
                error = false;
                return false;
            }
            int studentID = Integer.parseInt(studentIDInput);
            Student student = findStudentByID(studentID);
            if (student == null) {
                System.out.println("Wrong inputs");
                error = false;
                return error;
            }
            String courseIDInput;
            if (scanner.hasNextLine()) {
                courseIDInput = scanner.nextLine();
            } else {
                System.out.println("Wrong inputs");
                error = false;
                return false;
            }
            int courseID = Integer.parseInt(courseIDInput);
 
            Course course = findCourseByID(courseID);
 
            if (course == null) {
                System.out.println("Wrong inputs");
                error = false;
                return error;
            } else {
                boolean enrolled = student.enroll(course);
                if (enrolled) {
                    System.out.println("Enrolled successfully");
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Wrong inputs");
            error = false;
            return error;
 
        }
        return true;
    }
    /**
     * Finds a student in the list of students by their ID.
     *
     * @param studentID The ID of the student to search for.
     * @return The student object if found, if else null.
     */
    public static Student findStudentByID(int studentID) {
        for (Student student : students) {
            if (student.getMemberID() == studentID) {
                return student;
            }
        }
        return null;
    }
    /**
     * Finds a professor in the list of professors by their ID.
     *
     * @param professorID The ID of the professor to search for.
     * @return The professor object if found, if else null.
     */
    public static Professor findProfessorByID(int professorID) {
        for (Professor professor : professors) {
            if (professor.getMemberID() == professorID) {
                return professor;
            }
        }
        return null;
    }
    /**
     * Finds a course in the list of courses by its ID.
     *
     * @param courseID The ID of the course to search for.
     * @return The course object if found, if else null.
     */
    public static Course findCourseByID(int courseID) {
        for (Course course : courses) {
            if (course.getCourseId() == courseID) {
                return course;
            }
        }
        return null;
    }
    /**
     * Finds a course in the list of courses by its name.
     *
     * @param courseName The name of the course to search for.
     * @return The course object if found, if else null.
     */
    public static Course findCourseByName(String courseName) {
        for (Course course : courses) {
            if (course.getCourseName().equalsIgnoreCase(courseName)) {
                return course;
            }
        }
        return null;
    }
    /**
     * Adds a new professor.
     * Prompts the user to enter the professor's name
     * adds them to the list of professors if the input is valid and satisfies all its conditions.
     * Prints messages for success or failure.
     */
    public static void addProfessor() {
        String memberName;
        if (scanner.hasNextLine()) {
            memberName = scanner.nextLine();
            if (!containsOnlyLetters(memberName)) {
                System.out.println("Wrong inputs");
                error = false;
            } else {
                Professor newProfessor = new Professor(memberName);
                professors.add(newProfessor);
                System.out.println("Added successfully");
            }
        } else {
            System.out.println("Wrong inputs");
            error = false;
        }
    }
    /**
     * Adds a new course.
     * Prompts the user to enter the course's name and level
     * adds them to the list of courses if the input is valid and satisfies all its conditions.
     * Prints messages for success or failure.
     */
    public static boolean addCourse() {
        String courseName;
        if (scanner.hasNextLine()) {
            courseName = scanner.nextLine();
            if (!containsOnlyLettersCourse(courseName)) {
                System.out.println("Wrong inputs");
                error = false;
                return error;
            }
            boolean courseExists = false;
            for (Course course : courses) {
                if (course.getCourseName().equalsIgnoreCase(courseName)) {
                    courseExists = true;
                    break;
                }
            }
            if (courseExists) {
                System.out.println("Course exists");
                error = false;
            }
            if (courseName.equalsIgnoreCase("student") || courseName.equalsIgnoreCase("course")
                    || courseName.equalsIgnoreCase("enroll") || courseName.equalsIgnoreCase("drop")
                    || courseName.equalsIgnoreCase("professor") || courseName.equalsIgnoreCase("teach")
                    || courseName.equalsIgnoreCase("exempt")) {
                    System.out.println("Wrong inputs");
                    error = false;
            } else {
                String level;
                if (scanner.hasNextLine()) {
                    level = scanner.nextLine().toUpperCase();
                    if (!level.equals("BACHELOR") && !level.equals("MASTER")) {
                        error = false;
                        System.out.println("Wrong inputs");
                        return error;
                    }
                CourseLevel courseLevel = CourseLevel.valueOf(level);
                Course newCourse = new Course(courseName, courseLevel);
                courses.add(newCourse);
                System.out.println("Added successfully");
                }
            }
        } else {
            System.out.println("Wrong inputs");
            error = false;
        }
        return true;
    }
 
    /**
     * fill the system with initial data including courses, students, and professors.
     */
    public static void fillInitialData() {
        courses.add(new Course("java_beginner", CourseLevel.BACHELOR));
        courses.add(new Course("java_intermediate", CourseLevel.BACHELOR));
        courses.add(new Course("python_basics", CourseLevel.BACHELOR));
        courses.add(new Course("algorithms", CourseLevel.MASTER));
        courses.add(new Course("advanced_programming", CourseLevel.MASTER));
        courses.add(new Course("mathematical_analysis", CourseLevel.MASTER));
        courses.add(new Course("computer_vision", CourseLevel.MASTER));
 
        students.add(new Student("Alice"));
        students.add(new Student("Bob"));
        students.add(new Student("Alex"));
 
        professors.add(new Professor("Ali"));
        professors.add(new Professor("Ahmed"));
        professors.add(new Professor("Andrey"));
        professors.get(0).teach(findCourseByName("java_beginner"));
        professors.get(0).teach(findCourseByName("java_intermediate"));
        professors.get(1).teach(findCourseByName("python_basics"));
        professors.get(1).teach(findCourseByName("advanced_programming"));
        professors.get(2).teach(findCourseByName("mathematical_analysis"));
        students.get(0).enroll(findCourseByName("java_beginner"));
        students.get(0).enroll(findCourseByName("java_intermediate"));
        students.get(0).enroll(findCourseByName("python_basics"));
        students.get(1).enroll(findCourseByName("java_beginner"));
        students.get(1).enroll(findCourseByName("algorithms"));
        students.get(2).enroll(findCourseByName("advanced_programming"));
    }
}
 
 
interface Enrollable {
    boolean enroll(Course course);
    boolean drop(Course course);
}
/**
 * Represents a member of the university.
 */
abstract class UniversityMember {
    private static int numberOfMembers = 0;
    private int memberID;
    private String memberName;
    /**
     * Constructs a UniversityMember with a given name.
     *
     * @param memberName The name of the university member.
     */
    public UniversityMember(String memberName) {
        this.memberID = ++numberOfMembers;
        this.memberName = memberName;
    }
    /**
     * Retrieves the ID of the university member.
     *
     * @return The member's ID.
     */
    public int getMemberID() {
        return memberID;
    }
    /**
     * Retrieves the name of the university member.
     *
     * @return The member's name.
     */
    public String getMemberName() {
        return memberName;
    }
}
/**
 * Represents a student in the university.
 */
class Student extends UniversityMember implements Enrollable {
    private static final int MAX_ENROLLMENT = 3;
    private List<Course> enrolledCourses;
    /**
     * Constructs a Student with a given name.
     *
     * @param memberName The name of the student.
     */
    public Student(String memberName) {
        super(memberName);
        enrolledCourses = new ArrayList<>();
    }
    /**
     * Enrolls the student in a course.
     *
     * @param course The course to be enrolled in.
     * @return True if enrollment is successful, if else false.
     */
    @Override
    public boolean enroll(Course course) {
        if (enrolledCourses.contains(course)) {
            System.out.println("Student is already enrolled in this course");
            System.exit(0);
            return false;
 
        } else if (enrolledCourses.size() >= MAX_ENROLLMENT) {
            System.out.println("Maximum enrollment is reached for the student");
            System.exit(0);
            return false;
        } else {
        enrolledCourses.add(course);
        course.enrollStudent(this);
        return true;
        }
    }
    /**
     * Drops a course that the student was enrolled in.
     *
     * @param course The course to be dropped.
     * @return True if dropping the course is successful, if else false.
     */
    @Override
    public boolean drop(Course course) {
        if (enrolledCourses.contains(course)) {
            enrolledCourses.remove(course);
            course.dropStudent(this);
            return true;
        } else {
            System.out.println("Student is not enrolled in this course");
            System.exit(0);
            return false;
        }
    }
}
/**
 * Represents a university professor who can teach courses.
 */
class Professor extends UniversityMember {
    static final int MAX_LOAD = 2;
    private List<Course> assignedCourses;
    /**
     * Constructs a Professor with a given name.
     *
     * @param memberName The name of the professor.
     */
    public Professor(String memberName) {
        super(memberName);
        assignedCourses = new ArrayList<>();
    }
    /**
     * Assigns a course to the professor for teaching.
     *
     * @param course The course to be taught.
     * @return True if the course assignment is successful, false otherwise.
     */
    public boolean teach(Course course) {
        if (assignedCourses.size() >= MAX_LOAD) {
            System.out.println("Professor's load is complete");
            System.exit(0);
            return false;
        }
        if (assignedCourses.contains(course)) {
            System.out.println("Professor is already teaching this course");
            System.exit(0);
            return false;
        }
            assignedCourses.add(course);
            return true;
        }
    /**
     * Removes a course from the professor's teaching assignment.
     *
     * @param course The course to be exempted.
     * @return True if the course removal is successful, false otherwise.
     */
    public boolean exempt(Course course) {
            if (!assignedCourses.contains(course)) {
                System.out.println("Professor is not teaching this course");
                System.exit(0);
                return false;
            } else {
                assignedCourses.remove(course);
                return true;
    }
     }
}
/**
 * Represents a course offered by the university.
 */
class Course {
    private String courseName;
    private CourseLevel courseLevel;
    private List<Student> enrolledStudents;
    private static final int CAPACITY = 3;
    private static int numberOfCourses = 0;
    private int courseId;
 
    /**
     * Constructs a Course with a given name and level.
     *
     * @param courseName  The name of the course.
     * @param courseLevel The level of the course (Bachelor or Master).
     */
    public Course(String courseName, CourseLevel courseLevel) {
        this.courseId = ++numberOfCourses;
        this.courseName = courseName;
        this.courseLevel = courseLevel;
        enrolledStudents = new ArrayList<>();
    }
 
 
    /**
     * Enrolls a student in the course.
     *
     * @param student The student to be enrolled.
     * @return True if the enrollment is successful, false otherwise.
     */
    public boolean enrollStudent(Student student) {
        if (enrolledStudents.size() < CAPACITY) {
            enrolledStudents.add(student);
            return true;
        }
        return false;
    }
    /**
     * Drops a student from the course.
     *
     * @param student The student to be dropped.
     */
    public void dropStudent(Student student) {
        enrolledStudents.remove(student);
    }
    /**
     * Retrieves the name of the course.
     *
     * @return The name of the course.
     */
    public String getCourseName() {
        return courseName;
    }
 
    /**
     * Retrieves the unique ID of the course.
     *
     * @return The course ID.
     */
    public int getCourseId() {
        return courseId;
    }
    /**
     * Checks if the course has reached maximum capacity.
     *
     * @return True if the course is full, false otherwise.
     */
    public boolean isFull() {
        return enrolledStudents.size() >= CAPACITY;
    }
}
/**
 * Enum representing the levels of courses.
 */
enum CourseLevel {
    BACHELOR,
    MASTER
}