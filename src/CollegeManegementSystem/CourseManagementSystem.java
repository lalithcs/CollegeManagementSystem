package CollegeManegementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CourseManagementSystem extends JFrame implements ActionListener {
    JTextField tfId, tfCourseName, tfInstructor, tfSchedule;
    JButton btnSave, btnView, btnDelete, btnBack;
    List courseList;
    Connection connection;

    public CourseManagementSystem() {
        setTitle("Course Management System");
        setSize(400, 400);
        setLayout(null);

        JLabel lbId = new JLabel("ID:");
        lbId.setBounds(50, 30, 100, 20);
        add(lbId);

        tfId = new JTextField();
        tfId.setBounds(160, 30, 150, 20);
        add(tfId);

        JLabel lbCourseName = new JLabel("Course Name:");
        lbCourseName.setBounds(50, 50, 100, 20);
        add(lbCourseName);

        tfCourseName = new JTextField();
        tfCourseName.setBounds(160, 50, 150, 20);
        add(tfCourseName);

        JLabel lbInstructor = new JLabel("Instructor:");
        lbInstructor.setBounds(50, 80, 100, 20);
        add(lbInstructor);

        tfInstructor = new JTextField();
        tfInstructor.setBounds(160, 80, 150, 20);
        add(tfInstructor);

        JLabel lbSchedule = new JLabel("Schedule:");
        lbSchedule.setBounds(50, 110, 100, 20);
        add(lbSchedule);

        tfSchedule = new JTextField();
        tfSchedule.setBounds(160, 110, 150, 20);
        add(tfSchedule);

        btnSave = new JButton("Save");
        btnSave.setBounds(50, 150, 100, 30);
        btnSave.addActionListener(e -> {
            try {
                saveCourse();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        add(btnSave);

        btnView = new JButton("View");
        btnView.setBounds(160, 150, 100, 30);
        btnView.addActionListener(e -> viewCourses());
        add(btnView);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(270, 150, 100, 30);
        btnDelete.addActionListener(e -> {
            int selectedIndex = courseList.getSelectedIndex();
            if (selectedIndex != -1) {
                try {
                    deleteCourse(selectedIndex);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        add(btnDelete);

        courseList = new List(10, true);
        courseList.setBounds(50, 190, 320, 150);
        courseList.addActionListener(e -> displaySelectedCourse());
        add(courseList);

        btnBack = new JButton("Back to Menu");
        btnBack.setBounds(270, 350, 120, 30);
        btnBack.addActionListener(e -> backToMenu());
        add(btnBack);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/college_db", "root", "lalith1.");
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS Courses (" +
                    "id INT PRIMARY KEY," +
                    "course_name VARCHAR(100) UNIQUE," +
                    "instructor VARCHAR(100)," +
                    "schedule VARCHAR(100))");
        } catch (Exception e) {
            e.printStackTrace();
        }

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void saveCourse() throws SQLException {
        int id = Integer.parseInt(tfId.getText());
        String courseName = tfCourseName.getText();
        String instructor = tfInstructor.getText();
        String schedule = tfSchedule.getText();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Courses (id, course_name, instructor, schedule) VALUES (?, ?, ?, ?)");
        statement.setInt(1, id);
        statement.setString(2, courseName);
        statement.setString(3, instructor);
        statement.setString(4, schedule);
        try {
            statement.executeUpdate();
            viewCourses();
            clearFields();
        } catch (SQLException e) {
            throw e;
        }
    }

    private void viewCourses() {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Courses");

            courseList.removeAll();
            while (rs.next()) {
                String record = "ID: " + rs.getInt("id") +
                        ", Course: " + rs.getString("course_name") +
                        ", Instructor: " + rs.getString("instructor") +
                        ", Schedule: " + rs.getString("schedule");
                courseList.add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCourse(int selectedIndex) throws SQLException {
        String selectedRecord = courseList.getItem(selectedIndex);
        String[] parts = selectedRecord.split(", ");
        String[] idParts = parts[0].split(": ");
        int courseId = Integer.parseInt(idParts[1]);

        PreparedStatement statement = connection.prepareStatement("DELETE FROM Courses WHERE id = ?");
        statement.setInt(1, courseId);
        statement.executeUpdate();

        viewCourses();
    }

    private void displaySelectedCourse() {
        int selectedIndex = courseList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedRecord = courseList.getItem(selectedIndex);
            String[] parts = selectedRecord.split(", ");
            String[] idParts = parts[0].split(": ");
            int courseId = Integer.parseInt(idParts[1]);

            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM Courses WHERE id = ?");
                statement.setInt(1, courseId);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    tfId.setText(String.valueOf(rs.getInt("id")));
                    tfCourseName.setText(rs.getString("course_name"));
                    tfInstructor.setText(rs.getString("instructor"));
                    tfSchedule.setText(rs.getString("schedule"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        tfId.setText("");
        tfCourseName.setText("");
        tfInstructor.setText("");
        tfSchedule.setText("");
    }

    private void backToMenu() {
        ManagementMenu menu = new ManagementMenu();
        menu.setVisible(true);
        this.dispose(); // Close the Course Management System screen
    }

    public static void main(String[] args) {
        CourseManagementSystem system = new CourseManagementSystem();
        system.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
