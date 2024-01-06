package CollegeManegementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FacultyManagementSystem extends JFrame implements ActionListener {
    JTextField tfName, tfDepartment;
    JButton btnSave, btnView, btnBack;
    JList<String> facultyList;
    DefaultListModel<String> facultyListModel;
    Connection connection;

    public FacultyManagementSystem() {
        setTitle("Faculty Management System");
        setSize(400, 400);
        setLayout(null);

        JLabel lbName = new JLabel("Name:");
        lbName.setBounds(50, 50, 80, 20);
        add(lbName);

        tfName = new JTextField();
        tfName.setBounds(130, 50, 150, 20);
        add(tfName);

        JLabel lbDepartment = new JLabel("Department:");
        lbDepartment.setBounds(50, 80, 80, 20);
        add(lbDepartment);

        tfDepartment = new JTextField();
        tfDepartment.setBounds(130, 80, 150, 20);
        add(tfDepartment);

        btnSave = new JButton("Save");
        btnSave.setBounds(50, 120, 100, 30);
        btnSave.addActionListener(e -> {
            try {
                saveFaculty();
            } catch (SQLException ex) {
                if (ex.getErrorCode() == 1062) { // Duplicate entry error code
                    showMessageDialog("Duplicate name! Faculty already exists.");
                } else {
                    ex.printStackTrace();
                }
            }
        });
        add(btnSave);

        btnView = new JButton("View");
        btnView.setBounds(160, 120, 100, 30);
        btnView.addActionListener(e -> viewFaculty());
        add(btnView);

        facultyListModel = new DefaultListModel<>();
        facultyList = new JList<>(facultyListModel);
        facultyList.setBounds(50, 160, 250, 180);
        facultyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        facultyList.addListSelectionListener(e -> displaySelectedFaculty());
        JScrollPane scrollPane = new JScrollPane(facultyList);
        scrollPane.setBounds(50, 160, 250, 180);
        add(scrollPane);

        btnBack = new JButton("Back to Menu");
        btnBack.setBounds(270, 350, 120, 30);
        btnBack.addActionListener(e -> backToMenu());
        add(btnBack);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/college_db?autoReconnect=true", "root", "lalith1.");
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS Faculty (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(50) UNIQUE," +
                    "department VARCHAR(50))");
        } catch (Exception e) {
            e.printStackTrace();
        }

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void saveFaculty() throws SQLException {
        String name = tfName.getText();
        String department = tfDepartment.getText();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Faculty (name, department) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, name);
        statement.setString(2, department);
        try {
            statement.executeUpdate();
            viewFaculty(); // Refresh the view after saving
            clearFields();
        } catch (SQLException e) {
            throw e; // Re-throw the exception for specific handling
        }
    }

    private void viewFaculty() {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Faculty");

            facultyListModel.clear(); // Clear previous entries
            while (rs.next()) {
                String record = "ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("name") +
                        ", Department: " + rs.getString("department");
                facultyListModel.addElement(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displaySelectedFaculty() {
        int selectedIndex = facultyList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedRecord = facultyList.getSelectedValue();
            String[] parts = selectedRecord.split(", ");
            String[] idParts = parts[0].split(": ");
            int facultyId = Integer.parseInt(idParts[1]);

            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM Faculty WHERE id = ?");
                statement.setInt(1, facultyId);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    tfName.setText(rs.getString("name"));
                    tfDepartment.setText(rs.getString("department"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        tfName.setText("");
        tfDepartment.setText("");
    }

    private void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void backToMenu() {
        ManagementMenu menu = new ManagementMenu();
        menu.setVisible(true);
        this.dispose(); // Close the Faculty Management System screen
    }

    public static void main(String[] args) {
        FacultyManagementSystem system = new FacultyManagementSystem();
        system.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
