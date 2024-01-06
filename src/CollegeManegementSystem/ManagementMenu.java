package CollegeManegementSystem;

import javax.swing.*;
import java.awt.event.*;

public class ManagementMenu extends JFrame implements ActionListener {
    JButton studentBtn, facultyBtn, courseBtn;

    public ManagementMenu() {
        setTitle("Management System Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        studentBtn = new JButton("Student Management");
        studentBtn.setBounds(100, 50, 200, 40);
        studentBtn.addActionListener(this);
        add(studentBtn);

        facultyBtn = new JButton("Faculty Management");
        facultyBtn.setBounds(100, 110, 200, 40);
        facultyBtn.addActionListener(this);
        add(facultyBtn);

        courseBtn = new JButton("Course Management");
        courseBtn.setBounds(100, 170, 200, 40);
        courseBtn.addActionListener(this);
        add(courseBtn);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == studentBtn) {
            // Open Student Management System
            StudentManagementSystem studentSystem = new StudentManagementSystem();
            studentSystem.setVisible(true);
            this.dispose(); // Close the current menu screen
        } else if (e.getSource() == facultyBtn) {
            // Open Faculty Management System
            FacultyManagementSystem facultySystem = new FacultyManagementSystem();
            facultySystem.setVisible(true);
            this.dispose(); // Close the current menu screen
        } else if (e.getSource() == courseBtn) {
            // Open Course Management System
            CourseManagementSystem courseSystem = new CourseManagementSystem();
            courseSystem.setVisible(true);
            this.dispose(); // Close the current menu screen
        }
    }

    public static void main(String[] args) {
        ManagementMenu menu = new ManagementMenu();
        menu.setVisible(true);
    }
}
