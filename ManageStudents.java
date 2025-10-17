import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ManageStudents extends JFrame implements ActionListener {

    private JTextField idField, nameField, rollField, emailField, phoneField;
    private JComboBox<String> genderBox, courseBox;
    private JButton updateButton, deleteButton, searchButton;

    public ManageStudents() {
        setTitle("Update / Delete Students");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));

        panel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        panel.add(idField);

        searchButton = new JButton("Search by ID");
        panel.add(searchButton);
        panel.add(new JLabel()); // empty space

        panel.add(new JLabel("Full Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Roll Number:"));
        rollField = new JTextField();
        panel.add(rollField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Gender:"));
        genderBox = new JComboBox<>(new String[]{"Select", "Male", "Female", "Other"});
        panel.add(genderBox);

        panel.add(new JLabel("Course:"));
        courseBox = new JComboBox<>(new String[]{"Select", "B.Tech", "B.Sc", "BCA", "MCA"});
        panel.add(courseBox);

        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        panel.add(updateButton);
        panel.add(deleteButton);

        add(panel);

        searchButton.addActionListener(this);
        updateButton.addActionListener(this);
        deleteButton.addActionListener(this);
    }

    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/studentdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "root",
                "YOUR_PASSWORD_HERE"
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int id = 0;
            if (!idField.getText().isEmpty()) {
                id = Integer.parseInt(idField.getText());
            }

            if (e.getSource() == searchButton) {
                if (id == 0) {
                    JOptionPane.showMessageDialog(this, "Enter Student ID to search!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Connection con = getConnection();
                PreparedStatement pst = con.prepareStatement("SELECT * FROM students WHERE id=?");
                pst.setInt(1, id);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    nameField.setText(rs.getString("name"));
                    rollField.setText(rs.getString("roll"));
                    emailField.setText(rs.getString("email"));
                    phoneField.setText(rs.getString("phone"));
                    genderBox.setSelectedItem(rs.getString("gender"));
                    courseBox.setSelectedItem(rs.getString("course"));
                } else {
                    JOptionPane.showMessageDialog(this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                con.close();

            } else if (e.getSource() == updateButton) {
                if (id == 0) {
                    JOptionPane.showMessageDialog(this, "Enter Student ID to update!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Connection con = getConnection();
                PreparedStatement pst = con.prepareStatement(
                        "UPDATE students SET name=?, roll=?, email=?, phone=?, gender=?, course=? WHERE id=?"
                );
                pst.setString(1, nameField.getText());
                pst.setString(2, rollField.getText());
                pst.setString(3, emailField.getText());
                pst.setString(4, phoneField.getText());
                pst.setString(5, (String) genderBox.getSelectedItem());
                pst.setString(6, (String) courseBox.getSelectedItem());
                pst.setInt(7, id);

                int updated = pst.executeUpdate();
                con.close();
                if (updated > 0) {
                    JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } else if (e.getSource() == deleteButton) {
                if (id == 0) {
                    JOptionPane.showMessageDialog(this, "Enter Student ID to delete!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Connection con = getConnection();
                    PreparedStatement pst = con.prepareStatement("DELETE FROM students WHERE id=?");
                    pst.setInt(1, id);
                    int deleted = pst.executeUpdate();
                    con.close();
                    if (deleted > 0) {
                        JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        // Clear fields
                        idField.setText("");
                        nameField.setText("");
                        rollField.setText("");
                        emailField.setText("");
                        phoneField.setText("");
                        genderBox.setSelectedIndex(0);
                        courseBox.setSelectedIndex(0);
                    } else {
                        JOptionPane.showMessageDialog(this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Student ID must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageStudents().setVisible(true));
    }
}
