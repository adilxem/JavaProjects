package Projects;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.sql.SQLException;

public class StudentManagementSystem {

    private static String url = "jdbc:mysql://localhost:3306/sms";
    private static String username = "root";
    private static String password = "root";

    public static void main(String[] args) {

//      load the Driver Class...
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

//      creating connection to database and preparing for statements...
        try {
            Connection con = DriverManager.getConnection(url, username, password);

            Scanner sc = new Scanner(System.in);
            Statement st = con.createStatement();

            while (true) {

                System.out.println();
                System.out.println("STUDENT MANAGEMENT SYSTEM");
                System.out.println();

                System.out.println("1. Add a student");
                System.out.println("2. View all students");
                System.out.println("3. View a student with a particular Student ID");
                System.out.println("4. Update student information");
                System.out.println("5. Delete a student");
                System.out.println("0. Exit");

                System.out.println();

                System.out.print("Choose an option: ");
                int choice = sc.nextInt();

                if (choice == 1) {

                    addStudent(st, sc);
                }
                else if (choice == 2) {

                    displayAllStudents(st);
                }
                else if (choice == 3) {

                    displayAStudent(st, sc);
                }
                else if (choice == 4) {

                    updateStudent(st, sc);
                }
                else if (choice == 5) {

                    deleteStudent(st, sc);
                }
                else if (choice == 0) {

                    exit();
                    sc.close();
                    st.close();
                    con.close();
                    return;
                }
                else System.out.println("Wrong input");
            }

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        catch (InterruptedException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void addStudent(Statement st, Scanner sc) {

        try {

            System.out.print("Enter student name: ");
            String studentName = sc.next();

            System.out.print("Enter student contact: ");
            Long studentContact = sc.nextLong();

            String sql = "INSERT INTO students (studentName, studentContact) VALUES ('" + studentName + "', " + studentContact + ");";

            int rowsAffected = st.executeUpdate(sql);

            if (rowsAffected > 0) {

                System.out.println("Student added to database successfully!");
            }
            else {

                System.out.println("Error! Try Again...");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void displayAllStudents (Statement st) {

        try {

            String sql = "SELECT * FROM students;";

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                int id = rs.getInt("studentID");
                String name = rs.getString("studentName");
                Long contact = rs.getLong("studentContact");

                System.out.println();
                System.out.println("====================================================================");
                System.out.println();

                System.out.println("Student ID: " + id);
                System.out.println("Student Name: " + name);
                System.out.println("Student Contact: " + contact);

                System.out.println();
                System.out.println("====================================================================");
                System.out.println();
            }

            rs.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void displayAStudent (Statement st, Scanner sc) {

        try {

            System.out.print("Enter student ID: ");
            Integer studentID = sc.nextInt();

            String sql = "SELECT * FROM students WHERE studentID = " + studentID + ";";

            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {

                int id = rs.getInt("studentID");
                String name = rs.getString("studentName");
                Long contact = rs.getLong("studentContact");

                System.out.println();
                System.out.println("====================================================================");
                System.out.println();

                System.out.println("Student ID: " + id);
                System.out.println("Student Name: " + name);
                System.out.println("Student Contact: " + contact);

                System.out.println();
                System.out.println("====================================================================");
                System.out.println();
            }
            else System.out.println("Student not found for the given ID!");

            rs.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void updateStudent(Statement st, Scanner sc) {

        try {

            System.out.print("Enter the student ID to which you want to update the contact: ");
            int id = sc.nextInt();

            if (!studentExists(st, id)) {
                System.out.println("Student not found for ID " + id);
                return;
            }

            System.out.print("Enter the new contact for student with ID " + id + ": ");
            Long newContact = sc.nextLong();

            String sql = "UPDATE students SET studentContact = " + newContact + " WHERE studentID = " + id + ";";

            int rowsAffected = st.executeUpdate(sql);

            if (rowsAffected > 0 ) {
                System.out.println("Update successful for student with ID " + id);
            }
            else System.out.println("Update failed!");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteStudent(Statement st, Scanner sc) {

        try {

            System.out.print("Enter the student ID of the student to be removed: ");
            int removalID = sc.nextInt();

            if (!studentExists(st, removalID)) {

                System.out.println("Student not found for ID: " + removalID);
                return;
            }

            String sql = "DELETE FROM students WHERE studentID = " + removalID + ";";

            int rowsAffected = st.executeUpdate(sql);
            if (rowsAffected > 0) {

                System.out.println("Student with ID " + removalID + " has been removed successfully!");
            }
            else System.out.println("Deletion of student with ID: " + removalID + " failed!");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean studentExists(Statement st, int id) {

        try {

            String sql = "SELECT studentID FROM students WHERE studentID = " + id + ";";

            ResultSet rs = st.executeQuery(sql);

            return rs.next();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static void exit() throws InterruptedException {

        System.out.print("Exiting Student Management Portal");

        int i = 5;

        while (i > 0) {

            System.out.print(".");
            Thread.sleep(400);
            i--;
        }
    }
}