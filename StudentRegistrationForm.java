import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                JOptionPane.showMessageDialog(this,
                        "Registration Successful!\n\nName: " + name +
                        "\nRoll No: " + roll +
                        "\nEmail: " + email +
                        "\nPhone: " + phone +
                        "\nGender: " + gender +
                        "\nCourse: " + course,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRegistrationForm().setVisible(true));
    }
}
