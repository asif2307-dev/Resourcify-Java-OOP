package resourcify;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Modern Design System & Theme Engine for Resourcify.
 * Provides a contemporary, polished aesthetic with smooth typography,
 * pill badges, elevated cards, and responsive button feedback.
 */
public final class UITheme {

    // --- Color Palette (Tailwind & Modern SaaS Inspired) ---
    public static final Color BG_APP = new Color(248, 250, 252);        // Slate 50
    public static final Color BG_CARD = new Color(255, 255, 255);       // White
    public static final Color BG_SIDEBAR = new Color(15, 23, 42);       // Slate 900
    public static final Color BG_NAVBAR = new Color(24, 32, 54);        // Deep Navy Slate

    public static final Color PRIMARY = new Color(37, 99, 235);         // Blue 600
    public static final Color PRIMARY_HOVER = new Color(29, 78, 216);   // Blue 700
    public static final Color PRIMARY_LIGHT = new Color(239, 246, 255); // Blue 50

    public static final Color SUCCESS = new Color(16, 185, 129);        // Emerald 500
    public static final Color SUCCESS_HOVER = new Color(5, 150, 105);   // Emerald 600
    public static final Color SUCCESS_BG = new Color(236, 253, 245);    // Emerald 50
    public static final Color SUCCESS_TEXT = new Color(6, 95, 70);      // Emerald 800

    public static final Color WARNING = new Color(245, 158, 11);        // Amber 500
    public static final Color WARNING_HOVER = new Color(217, 119, 6);   // Amber 600
    public static final Color WARNING_BG = new Color(254, 243, 199);    // Amber 100
    public static final Color WARNING_TEXT = new Color(146, 64, 14);    // Amber 800

    public static final Color DANGER = new Color(239, 68, 68);          // Rose 500
    public static final Color DANGER_HOVER = new Color(220, 38, 38);    // Rose 600
    public static final Color DANGER_BG = new Color(254, 242, 242);     // Rose 50
    public static final Color DANGER_TEXT = new Color(153, 27, 27);     // Rose 800

    public static final Color PURPLE = new Color(139, 92, 246);         // Violet 500
    public static final Color PURPLE_BG = new Color(245, 243, 255);     // Violet 50
    public static final Color PURPLE_TEXT = new Color(91, 33, 182);     // Violet 800

    public static final Color CYAN = new Color(14, 165, 233);           // Sky 500
    public static final Color CYAN_BG = new Color(240, 249, 255);       // Sky 50
    public static final Color CYAN_TEXT = new Color(3, 105, 161);       // Sky 700

    public static final Color BORDER = new Color(226, 232, 240);        // Slate 200
    public static final Color BORDER_DARK = new Color(203, 213, 225);   // Slate 300
    public static final Color TEXT_MAIN = new Color(15, 23, 42);        // Slate 900
    public static final Color TEXT_MUTED = new Color(100, 116, 139);    // Slate 500
    public static final Color TABLE_HEADER_BG = new Color(241, 245, 249);// Slate 100
    public static final Color TABLE_ALT_ROW = new Color(248, 250, 252); // Slate 50
    public static final Color TABLE_SELECTION = new Color(219, 234, 254);// Blue 100

    // --- Modern Typography ---
    public static final Font FONT_HERO = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BADGE = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font FONT_STAT = new Font("Segoe UI", Font.BOLD, 26);

    private UITheme() {}

    /**
     * Initializes global anti-aliasing and modern defaults for Swing.
     */
    public static void applySystemTweaks() {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        UIManager.put("TabbedPane.selected", Color.WHITE);
        UIManager.put("TabbedPane.focus", new Color(0, 0, 0, 0));
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(8, 0, 0, 0));
    }

    // =========================================================================
    // CUSTOM BUTTON COMPONENT WITH ROUNDED CORNERS & HOVER EFFECTS
    // =========================================================================
    public static class ModernButton extends JButton {
        private final Color baseBg;
        private final Color hoverBg;
        private final Color textColor;
        private final boolean isOutlined;
        private boolean isHovered = false;
        private final int cornerRadius;

        public ModernButton(String text, Color baseBg, Color hoverBg, Color textColor, int cornerRadius, boolean isOutlined) {
            super(text);
            this.baseBg = baseBg;
            this.hoverBg = hoverBg;
            this.textColor = textColor;
            this.cornerRadius = cornerRadius;
            this.isOutlined = isOutlined;

            setFont(FONT_BOLD);
            setForeground(textColor);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(8, 16, 8, 16));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (isEnabled()) {
                        isHovered = true;
                        repaint();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            if (!isOutlined) {
                g2.setColor(isHovered ? hoverBg : baseBg);
                g2.fill(new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius));
            } else {
                g2.setColor(isHovered ? hoverBg : baseBg);
                g2.fill(new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius));
                g2.setColor(isHovered ? textColor : BORDER_DARK);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Float(0.6f, 0.6f, width - 1.2f, height - 1.2f, cornerRadius, cornerRadius));
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static JButton createPrimaryButton(String text) {
        return new ModernButton(text, PRIMARY, PRIMARY_HOVER, Color.WHITE, 8, false);
    }

    public static JButton createSuccessButton(String text) {
        return new ModernButton(text, SUCCESS, SUCCESS_HOVER, Color.WHITE, 8, false);
    }

    public static JButton createDangerButton(String text) {
        return new ModernButton(text, DANGER, DANGER_HOVER, Color.WHITE, 8, false);
    }

    public static JButton createSecondaryButton(String text) {
        return new ModernButton(text, Color.WHITE, new Color(241, 245, 249), TEXT_MAIN, 8, true);
    }

    public static JButton createChipButton(String text, Color accentColor, Color bg) {
        ModernButton btn = new ModernButton(text, bg, accentColor, accentColor, 16, false);
        btn.setFont(FONT_BADGE);
        btn.setBorder(new EmptyBorder(6, 12, 6, 12));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setForeground(accentColor);
            }
        });
        return btn;
    }

    // =========================================================================
    // MODERN TEXT FIELD & PASSWORD FIELD
    // =========================================================================
    public static class ModernTextField extends JTextField {
        private boolean isFocused = false;

        public ModernTextField(int columns) {
            super(columns);
            init();
        }

        public ModernTextField(String text, int columns) {
            super(text, columns);
            init();
        }

        private void init() {
            setFont(FONT_REGULAR);
            setForeground(TEXT_MAIN);
            setCaretColor(PRIMARY);
            setBackground(Color.WHITE);
            setOpaque(false);
            setBorder(new EmptyBorder(8, 12, 8, 12));

            addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    isFocused = true;
                    repaint();
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    isFocused = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.WHITE);
            g2.fill(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 8, 8));

            g2.setColor(isFocused ? PRIMARY : BORDER);
            g2.setStroke(new BasicStroke(isFocused ? 1.8f : 1.0f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 8, 8));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class ModernPasswordField extends JPasswordField {
        private boolean isFocused = false;

        public ModernPasswordField(int columns) {
            super(columns);
            setFont(FONT_REGULAR);
            setForeground(TEXT_MAIN);
            setCaretColor(PRIMARY);
            setBackground(Color.WHITE);
            setOpaque(false);
            setBorder(new EmptyBorder(8, 12, 8, 12));

            addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    isFocused = true;
                    repaint();
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    isFocused = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.WHITE);
            g2.fill(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 8, 8));

            g2.setColor(isFocused ? PRIMARY : BORDER);
            g2.setStroke(new BasicStroke(isFocused ? 1.8f : 1.0f));
            g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 8, 8));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =========================================================================
    // ELEVATED CARD CONTAINER
    // =========================================================================
    public static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1.0f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 12, 12));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(16, 16, 16, 16));
        return card;
    }

    // =========================================================================
    // PILL BADGE TABLE CELL RENDERER
    // =========================================================================
    public static class StatusBadgeRenderer extends DefaultTableCellRenderer {
        private String badgeText = "";
        private Color bgColor = Color.LIGHT_GRAY;
        private Color fgColor = Color.BLACK;
        private Color strokeColor = Color.GRAY;

        public StatusBadgeRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(FONT_BADGE);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            String text = value == null ? "" : value.toString().trim();
            this.badgeText = text;

            if ("APPROVED".equalsIgnoreCase(text) || "Available".equalsIgnoreCase(text)) {
                bgColor = SUCCESS_BG;
                fgColor = SUCCESS_TEXT;
                strokeColor = SUCCESS;
            } else if ("PENDING".equalsIgnoreCase(text)) {
                bgColor = WARNING_BG;
                fgColor = WARNING_TEXT;
                strokeColor = WARNING;
            } else if ("REJECTED".equalsIgnoreCase(text) || "CANCELLED".equalsIgnoreCase(text) || "Maintenance".equalsIgnoreCase(text)) {
                bgColor = DANGER_BG;
                fgColor = DANGER_TEXT;
                strokeColor = DANGER;
            } else {
                bgColor = new Color(241, 245, 249);
                fgColor = TEXT_MUTED;
                strokeColor = BORDER;
            }

            setBackground(isSelected ? TABLE_SELECTION : (row % 2 == 0 ? Color.WHITE : TABLE_ALT_ROW));
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());

            if (badgeText.isEmpty()) return;

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setFont(FONT_BADGE);

            FontMetrics fm = g2.getFontMetrics();
            int strWidth = fm.stringWidth(badgeText);
            int badgeW = Math.max(strWidth + 18, 70);
            int badgeH = 22;
            int x = (getWidth() - badgeW) / 2;
            int y = (getHeight() - badgeH) / 2;

            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(x, y, badgeW, badgeH, 12, 12));

            g2.setColor(strokeColor);
            g2.setStroke(new BasicStroke(1.0f));
            g2.draw(new RoundRectangle2D.Float(x + 0.5f, y + 0.5f, badgeW - 1, badgeH - 1, 12, 12));

            g2.setColor(fgColor);
            int textY = y + ((badgeH - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(badgeText, x + (badgeW - strWidth) / 2, textY);

            g2.dispose();
        }
    }

    // =========================================================================
    // TABLE STYLING HELPER
    // =========================================================================
    public static void styleTable(JTable table) {
        table.setRowHeight(36);
        table.setFont(FONT_REGULAR);
        table.setForeground(TEXT_MAIN);
        table.setGridColor(BORDER);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setSelectionBackground(TABLE_SELECTION);
        table.setSelectionForeground(TEXT_MAIN);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TEXT_MAIN);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(TABLE_HEADER_BG);
                setForeground(TEXT_MAIN);
                setFont(FONT_BOLD);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_DARK),
                        new EmptyBorder(8, 12, 8, 12)
                ));
                return this;
            }
        };
        header.setDefaultRenderer(headerRenderer);

        DefaultTableCellRenderer defaultCellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                                                           boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBorder(new EmptyBorder(6, 12, 6, 12));
                if (!sel) {
                    setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALT_ROW);
                } else {
                    setBackground(TABLE_SELECTION);
                }
                setForeground(TEXT_MAIN);
                return this;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            String colName = table.getColumnName(i).toLowerCase();
            if (colName.contains("status") || colName.contains("availability")) {
                table.getColumnModel().getColumn(i).setCellRenderer(new StatusBadgeRenderer());
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(defaultCellRenderer);
            }
        }
    }

    // =========================================================================
    // MODERN APP TOP NAVBAR / HEADER
    // =========================================================================
    public static JPanel createNavbar(String title, User user, Runnable onLogout) {
        JPanel navbar = new JPanel(new BorderLayout(15, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, BG_SIDEBAR, getWidth(), 0, BG_NAVBAR);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        navbar.setBorder(new EmptyBorder(12, 24, 12, 24));
        navbar.setPreferredSize(new Dimension(1000, 64));

        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        brandPanel.setOpaque(false);

        JPanel iconBadge = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PRIMARY);
                g2.fillOval(0, 0, 36, 36);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
                FontMetrics fm = g2.getFontMetrics();
                int x = (36 - fm.stringWidth("R")) / 2;
                int y = ((36 - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString("R", x, y);
                g2.dispose();
            }
        };
        iconBadge.setPreferredSize(new Dimension(36, 36));
        iconBadge.setOpaque(false);
        brandPanel.add(iconBadge);

        JPanel titles = new JPanel(new GridLayout(2, 1, 0, 2));
        titles.setOpaque(false);
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLbl.setForeground(Color.WHITE);

        JLabel subLbl = new JLabel("Resourcify Enterprise — Campus Facility Management");
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subLbl.setForeground(new Color(148, 163, 184));
        titles.add(titleLbl);
        titles.add(subLbl);
        brandPanel.add(titles);

        navbar.add(brandPanel, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        userPanel.setOpaque(false);

        String initials = user.getName().length() >= 2 ? user.getName().substring(0, 2).toUpperCase() : "U";
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(user.getRole() == Role.ADMIN ? PURPLE : (user.getRole() == Role.FACULTY ? SUCCESS : CYAN));
                g2.fillOval(0, 0, 34, 34);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                int x = (34 - fm.stringWidth(initials)) / 2;
                int y = ((34 - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(initials, x, y);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(34, 34));
        avatar.setOpaque(false);
        userPanel.add(avatar);

        JPanel metaPanel = new JPanel(new GridLayout(2, 1, 0, 1));
        metaPanel.setOpaque(false);
        JLabel nameLbl = new JLabel(user.getName());
        nameLbl.setFont(FONT_BOLD);
        nameLbl.setForeground(Color.WHITE);

        JLabel roleLbl = new JLabel(user.getRole() + " • " + user.getEmail());
        roleLbl.setFont(FONT_SMALL);
        roleLbl.setForeground(new Color(148, 163, 184));
        metaPanel.add(nameLbl);
        metaPanel.add(roleLbl);
        userPanel.add(metaPanel);

        ModernButton logoutBtn = new ModernButton("Sign Out", new Color(255, 255, 255, 25), new Color(239, 68, 68, 180), Color.WHITE, 6, true);
        logoutBtn.setBorder(new EmptyBorder(6, 12, 6, 12));
        logoutBtn.setFont(FONT_SMALL);
        logoutBtn.addActionListener(e -> onLogout.run());
        userPanel.add(logoutBtn);

        navbar.add(userPanel, BorderLayout.EAST);
        return navbar;
    }

    // =========================================================================
    // METRIC STAT CARD (FOR ADMIN & ANALYTICS)
    // =========================================================================
    public static JPanel createStatCard(String label, JLabel valueLabel, Color accentColor, String subtitle) {
        JPanel card = new JPanel(new BorderLayout(8, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

                g2.setColor(accentColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, 5, getHeight(), 12, 12));
                g2.fillRect(3, 0, 3, getHeight());

                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1.0f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 12, 12));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(16, 20, 16, 16));

        JLabel titleLbl = new JLabel(label.toUpperCase());
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLbl.setForeground(TEXT_MUTED);

        valueLabel.setFont(FONT_STAT);
        valueLabel.setForeground(TEXT_MAIN);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(FONT_SMALL);
        subLbl.setForeground(TEXT_MUTED);

        JPanel textStack = new JPanel(new GridLayout(3, 1, 0, 4));
        textStack.setOpaque(false);
        textStack.add(titleLbl);
        textStack.add(valueLabel);
        textStack.add(subLbl);

        card.add(textStack, BorderLayout.CENTER);
        return card;
    }
}
