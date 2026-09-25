package resourcify;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);

    public LoginFrame() {
        setTitle("Resourcify — Campus Resource Booking System");
        setSize(500, 440);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(25, 30, 20, 30));

        // Header Section
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("RESOURCIFY");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(24, 76, 140));

        JLabel subtitleLabel = new JLabel("Campus Resource Booking & Allocation System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setForeground(Color.DARK_GRAY);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form Section
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Sign In"),
                new EmptyBorder(10, 15, 10, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel emailLbl = new JLabel("Email Address:");
        emailLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(emailLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel passLbl = new JLabel("Password:");
        passLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(passLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(passwordField, gbc);

        // Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton clearBtn = new JButton("Clear");
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginBtn.setPreferredSize(new Dimension(90, 30));
        clearBtn.setPreferredSize(new Dimension(80, 30));

        actionPanel.add(clearBtn);
        actionPanel.add(loginBtn);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 8, 4, 8);
        formPanel.add(actionPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Demo Quick Fill Footer
        JPanel demoPanel = new JPanel(new BorderLayout(5, 5));
        demoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Quick Demo Login (Click to Fill)"),
                new EmptyBorder(5, 10, 5, 10)
        ));

        JPanel demoButtons = new JPanel(new GridLayout(1, 3, 10, 0));
        JButton studentDemoBtn = new JButton("Student Demo");
        JButton facultyDemoBtn = new JButton("Faculty Demo");
        JButton adminDemoBtn = new JButton("Admin Demo");

        studentDemoBtn.addActionListener(e -> {
            emailField.setText("student@resourcify.demo");
            passwordField.setText("student123");
        });

        facultyDemoBtn.addActionListener(e -> {
            emailField.setText("faculty@resourcify.demo");
            passwordField.setText("faculty123");
        });

        adminDemoBtn.addActionListener(e -> {
            emailField.setText("admin@resourcify.demo");
            passwordField.setText("admin123");
        });

        demoButtons.add(studentDemoBtn);
        demoButtons.add(facultyDemoBtn);
        demoButtons.add(adminDemoBtn);
        demoPanel.add(demoButtons, BorderLayout.CENTER);

        mainPanel.add(demoPanel, BorderLayout.SOUTH);

        // Listeners
        loginBtn.addActionListener(e -> authenticate());
        clearBtn.addActionListener(e -> {
            emailField.setText("");
            passwordField.setText("");
            emailField.requestFocus();
        });

        // Set Login button as default (Enter key triggers it)
        getRootPane().setDefaultButton(loginBtn);
        setContentPane(mainPanel);

        // Pre-fill with Student demo by default for convenience
        emailField.setText("student@resourcify.demo");
        passwordField.setText("student123");
    }

    private void authenticate() {
        String mail = emailField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (mail.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both email address and password.",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (User u : DataStore.USERS) {
            if (u.getEmail().equalsIgnoreCase(mail) && u.getPassword().equals(pass)) {
                dispose();
                if (u.getRole() == Role.ADMIN) {
                    new AdminFrame(u).setVisible(true);
                } else {
                    new UserFrame(u).setVisible(true);
                }
                return;
            }
        }

        JOptionPane.showMessageDialog(this,
                "Invalid credentials. Please verify your email and password.",
                "Authentication Failed", JOptionPane.ERROR_MESSAGE);
    }
}
