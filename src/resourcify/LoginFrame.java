package resourcify;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {
    private final UITheme.ModernTextField emailField = new UITheme.ModernTextField(20);
    private final UITheme.ModernPasswordField passwordField = new UITheme.ModernPasswordField(20);

    public LoginFrame() {
        setTitle("Resourcify — Enterprise Campus Resource Booking");
        setSize(780, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_APP);

        // =====================================================================
        // LEFT PANEL: BRAND HERO BANNER (DEEP NAVY GRADIENT)
        // =====================================================================
        JPanel brandHero = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Rich Deep Navy to Royal Indigo Gradient
                GradientPaint gp = new GradientPaint(0, 0, new Color(15, 23, 42), getWidth(), getHeight(), new Color(30, 58, 138));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Glowing geometric background accents
                g2.setColor(new Color(59, 130, 246, 35));
                g2.fillOval(-30, -30, 180, 180);
                g2.setColor(new Color(99, 102, 241, 30));
                g2.fillOval(getWidth() - 110, getHeight() - 130, 200, 200);

                g2.dispose();
            }
        };
        brandHero.setOpaque(false);
        brandHero.setPreferredSize(new Dimension(320, 520));
        brandHero.setLayout(new BoxLayout(brandHero, BoxLayout.Y_AXIS));
        brandHero.setBorder(new EmptyBorder(42, 32, 36, 32));

        // Modern Logo Monogram Badge [ R ]
        JPanel logoBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.PRIMARY);
                g2.fill(new RoundRectangle2D.Float(0, 0, 48, 48, 14, 14));
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 26));
                FontMetrics fm = g2.getFontMetrics();
                int x = (48 - fm.stringWidth("R")) / 2;
                int y = ((48 - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString("R", x, y);
                g2.dispose();
            }
        };
        logoBadge.setPreferredSize(new Dimension(48, 48));
        logoBadge.setMaximumSize(new Dimension(48, 48));
        logoBadge.setOpaque(false);
        logoBadge.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel appTitle = new JLabel("RESOURCIFY");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        appTitle.setForeground(Color.WHITE);
        appTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel appTagline = new JLabel("Campus Facility & Resource System");
        appTagline.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        appTagline.setForeground(new Color(186, 215, 255));
        appTagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel feature1 = createHeroBullet("Automated slot conflict detection");
        JLabel feature2 = createHeroBullet("Role-governed booking approvals");
        JLabel feature3 = createHeroBullet("Audited facility utilization reports");
        JLabel feature4 = createHeroBullet("Real-time campus resource status");

        JLabel versionLbl = new JLabel("Resourcify Enterprise Platform");
        versionLbl.setFont(UITheme.FONT_SMALL);
        versionLbl.setForeground(new Color(148, 163, 184));
        versionLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        brandHero.add(logoBadge);
        brandHero.add(Box.createRigidArea(new Dimension(0, 14)));
        brandHero.add(appTitle);
        brandHero.add(Box.createRigidArea(new Dimension(0, 4)));
        brandHero.add(appTagline);
        brandHero.add(Box.createRigidArea(new Dimension(0, 36)));
        brandHero.add(feature1);
        brandHero.add(Box.createRigidArea(new Dimension(0, 12)));
        brandHero.add(feature2);
        brandHero.add(Box.createRigidArea(new Dimension(0, 12)));
        brandHero.add(feature3);
        brandHero.add(Box.createRigidArea(new Dimension(0, 12)));
        brandHero.add(feature4);
        brandHero.add(Box.createVerticalGlue());
        brandHero.add(versionLbl);

        root.add(brandHero, BorderLayout.WEST);

        // =====================================================================
        // RIGHT PANEL: MODERN LOGIN CARD & DEMO CHIPS
        // =====================================================================
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(UITheme.BG_APP);
        formContainer.setBorder(new EmptyBorder(32, 36, 32, 36));

        JPanel formCard = UITheme.createCard();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));

        JLabel welcomeHeader = new JLabel("Welcome Back");
        welcomeHeader.setFont(UITheme.FONT_HERO);
        welcomeHeader.setForeground(UITheme.TEXT_MAIN);
        welcomeHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeSub = new JLabel("Sign in with your campus credentials to continue");
        welcomeSub.setFont(UITheme.FONT_SUBTITLE);
        welcomeSub.setForeground(UITheme.TEXT_MUTED);
        welcomeSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Fields
        JLabel emailLbl = new JLabel("EMAIL ADDRESS");
        emailLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        emailLbl.setForeground(UITheme.TEXT_MUTED);
        emailLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel passLbl = new JLabel("PASSWORD");
        passLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        passLbl.setForeground(UITheme.TEXT_MUTED);
        passLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Action Buttons
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionRow.setOpaque(false);
        actionRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JButton clearBtn = UITheme.createSecondaryButton("Clear");
        JButton loginBtn = UITheme.createPrimaryButton("Sign In  →");
        loginBtn.setPreferredSize(new Dimension(130, 38));
        clearBtn.setPreferredSize(new Dimension(85, 38));

        actionRow.add(clearBtn);
        actionRow.add(loginBtn);

        // Quick Demo Fill Bar
        JPanel demoBox = new JPanel();
        demoBox.setLayout(new BoxLayout(demoBox, BoxLayout.Y_AXIS));
        demoBox.setOpaque(false);
        demoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        demoBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER),
                new EmptyBorder(14, 0, 0, 0)
        ));

        JLabel demoHint = new JLabel("QUICK DEMO ONE-CLICK FILL");
        demoHint.setFont(new Font("Segoe UI", Font.BOLD, 10));
        demoHint.setForeground(UITheme.TEXT_MUTED);

        JPanel chipRow = new JPanel(new GridLayout(1, 3, 8, 0));
        chipRow.setOpaque(false);
        chipRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        JButton studentDemoBtn = UITheme.createChipButton("Student", UITheme.PRIMARY, UITheme.PRIMARY_LIGHT);
        JButton facultyDemoBtn = UITheme.createChipButton("Faculty", UITheme.SUCCESS, UITheme.SUCCESS_BG);
        JButton adminDemoBtn = UITheme.createChipButton("Admin", UITheme.PURPLE, UITheme.PURPLE_BG);

        chipRow.add(studentDemoBtn);
        chipRow.add(facultyDemoBtn);
        chipRow.add(adminDemoBtn);

        demoBox.add(demoHint);
        demoBox.add(Box.createRigidArea(new Dimension(0, 8)));
        demoBox.add(chipRow);

        // Assemble Form
        formCard.add(welcomeHeader);
        formCard.add(Box.createRigidArea(new Dimension(0, 4)));
        formCard.add(welcomeSub);
        formCard.add(Box.createRigidArea(new Dimension(0, 20)));

        formCard.add(emailLbl);
        formCard.add(Box.createRigidArea(new Dimension(0, 6)));
        formCard.add(emailField);
        formCard.add(Box.createRigidArea(new Dimension(0, 14)));

        formCard.add(passLbl);
        formCard.add(Box.createRigidArea(new Dimension(0, 6)));
        formCard.add(passwordField);
        formCard.add(Box.createRigidArea(new Dimension(0, 20)));

        formCard.add(actionRow);
        formCard.add(Box.createRigidArea(new Dimension(0, 16)));
        formCard.add(demoBox);

        formContainer.add(formCard, BorderLayout.CENTER);
        root.add(formContainer, BorderLayout.CENTER);

        // Listeners
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

        loginBtn.addActionListener(e -> authenticate());
        clearBtn.addActionListener(e -> {
            emailField.setText("");
            passwordField.setText("");
            emailField.requestFocus();
        });

        getRootPane().setDefaultButton(loginBtn);
        setContentPane(root);

        // Default fill
        emailField.setText("student@resourcify.demo");
        passwordField.setText("student123");
    }

    private static JLabel createHeroBullet(String text) {
        JLabel label = new JLabel("•  " + text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(241, 245, 249)); // High contrast Slate 100
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
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
