package CollegeManegementSystem;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class StudentManagementSystem extends JFrame implements ActionListener {
    JTextField tfID, tfName, tfAge;
    JButton btnSave, btnBack;
    Connection connection;

    public StudentManagementSystem() {
        setTitle("Student Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lbID = new JLabel("ID:");
        lbID.setBounds(50, 50, 80, 20);
        add(lbID);

        tfID = new JTextField();
        tfID.setBounds(130, 50, 150, 20);
        add(tfID);

        JLabel lbName = new JLabel("Name:");
        lbName.setBounds(50, 80, 80, 20);
        add(lbName);

        tfName = new JTextField();
        tfName.setBounds(130, 80, 150, 20);
        add(tfName);

        JLabel lbAge = new JLabel("Age:");
        lbAge.setBounds(50, 110, 80, 20);
        add(lbAge);

        tfAge = new JTextField();
        tfAge.setBounds(130, 110, 150, 20);
        add(tfAge);

        btnSave = new JButton("Save");
        btnSave.setBounds(50, 150, 100, 30);
        btnSave.addActionListener(this);
        add(btnSave);

        btnBack = new JButton("Back to Menu");
        btnBack.setBounds(160, 150, 150, 30);
        btnBack.addActionListener(this);
        add(btnBack);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/college_db", "root", "lalith1.");
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS students (id INT PRIMARY KEY, name VARCHAR(100), age INT)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            try {
                int id = Integer.parseInt(tfID.getText());
                String name = tfName.getText();
                int age = Integer.parseInt(tfAge.getText());
                PreparedStatement statement = connection.prepareStatement("INSERT INTO students(id, name, age) VALUES (?, ?, ?)");
                statement.setInt(1, id);
                statement.setString(2, name);
                statement.setInt(3, age);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Student added successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == btnBack) {
            ManagementMenu menu = new ManagementMenu();
            menu.setVisible(true);
            this.dispose(); // Close the Student Management System screen
        }
    }

    public static void main(String[] args) {
        StudentManagementSystem studentSystem = new StudentManagementSystem();
        studentSystem.setVisible(true);
    }
}
