import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class StudentRegistrationForm extends JFrame implements ActionListener {

    private JTextField nameField, rollField, emailField, phoneField;
    private JComboBox<String> genderBox, courseBox;
    private JButton submitButton, resetButton;

    public StudentRegistrationForm() {
        setTitle("Student Registration Form");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));

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

        submitButton = new JButton("Submit");
        resetButton = new JButton("Reset");

        panel.add(submitButton);
        panel.add(resetButton);

        add(panel);

        submitButton.addActionListener(this);
        resetButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String name = nameField.getText();
            String roll = rollField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String gender = (String) genderBox.getSelectedItem();
            String course = (String) courseBox.getSelectedItem();

            if (name.isEmpty() || roll.isEmpty() || email.isEmpty() || phone.isEmpty()
                    || gender.equals("Select") || course.equals("Select")) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean saved = saveToDatabase(name, roll, email, phone, gender, course);
                if (saved) {
                    JOptionPane.showMessageDialog(this, "Registration Successful and saved to database!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error saving to database!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == resetButton) {
            nameField.setText("");
            rollField.setText("");
            emailField.setText("");
            phoneField.setText("");
            genderBox.setSelectedIndex(0);
            courseBox.setSelectedIndex(0);
        }
    }

    private boolean saveToDatabase(String name, String roll, String email, String phone, String gender,
                                   String course) {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to MySQL
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/studentdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                    "root",
                    "Ram@2001"
            );

            String query = "INSERT INTO students(name, roll, email, phone, gender, course) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, roll);
            pst.setString(3, email);
            pst.setString(4, phone);
            pst.setString(5, gender);
            pst.setString(6, course);

            pst.executeUpdate();
            con.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRegistrationForm().setVisible(true));
    }
}
