package resourcify;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminFrame extends JFrame {
    private final User admin;
    private final BookingService service = new BookingService();

    // Tab 1: Bookings
    private final DefaultTableModel bookingModel;
    private final JTable bookingTable;
    private final JComboBox<String> statusFilterCombo = new JComboBox<>(new String[]{
            "All Requests", "PENDING", "APPROVED", "REJECTED", "CANCELLED"
    });
    private final JLabel pendingCountLabel = new JLabel("Pending Requests: 0");

    // Tab 2: Resources
    private final DefaultTableModel resourceModel;
    private final JTable resourceTable;

    // Tab 3: Utilization
    private final DefaultTableModel utilizationModel;
    private final JTable utilizationTable;
    private final JLabel statTotalResources = new JLabel("0");
    private final JLabel statTotalBookings = new JLabel("0");
    private final JLabel statApprovedBookings = new JLabel("0");
    private final JLabel statPendingBookings = new JLabel("0");

    public AdminFrame(User admin) {
        this.admin = admin;
        setTitle("Resourcify — Administrator Command Center");
        setSize(1120, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_APP);

        // Top Navigation Header
        JPanel topNavbar = UITheme.createNavbar("Administrator Control Center", admin, this::handleLogout);
        root.add(topNavbar, BorderLayout.NORTH);

        // Content Area
        JPanel bodyPanel = new JPanel(new BorderLayout(12, 12));
        bodyPanel.setBackground(UITheme.BG_APP);
        bodyPanel.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UITheme.FONT_BOLD);
        tabbedPane.setBackground(Color.WHITE);

        // =========================================================================
        // TAB 1: BOOKING REQUESTS & APPROVALS
        // =========================================================================
        JPanel bookingPanel = new JPanel(new BorderLayout(12, 12));
        bookingPanel.setBackground(UITheme.BG_CARD);
        bookingPanel.setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel bookingFilterBar = new JPanel(new BorderLayout());
        bookingFilterBar.setOpaque(false);

        JPanel leftFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        leftFilter.setOpaque(false);
        JLabel statFilterLbl = new JLabel("Filter Status:");
        statFilterLbl.setFont(UITheme.FONT_BOLD);
        statFilterLbl.setForeground(UITheme.TEXT_MAIN);
        leftFilter.add(statFilterLbl);

        statusFilterCombo.setFont(UITheme.FONT_REGULAR);
        statusFilterCombo.setBackground(Color.WHITE);
        statusFilterCombo.setPreferredSize(new Dimension(140, 34));
        leftFilter.add(statusFilterCombo);

        JButton refreshBookingsBtn = UITheme.createSecondaryButton("↻ Refresh");
        refreshBookingsBtn.setPreferredSize(new Dimension(100, 34));
        leftFilter.add(refreshBookingsBtn);
        bookingFilterBar.add(leftFilter, BorderLayout.WEST);

        pendingCountLabel.setFont(UITheme.FONT_BADGE);
        pendingCountLabel.setForeground(UITheme.WARNING_TEXT);
        JPanel rightFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        rightFilter.setOpaque(false);
        rightFilter.add(pendingCountLabel);
        bookingFilterBar.add(rightFilter, BorderLayout.EAST);

        bookingPanel.add(bookingFilterBar, BorderLayout.NORTH);

        bookingModel = new DefaultTableModel(new Object[]{
                "Booking ID", "Applicant", "Role", "Resource", "Date", "Time Slot", "Purpose", "Status", "Admin Remarks"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        bookingTable = new JTable(bookingModel);
        UITheme.styleTable(bookingTable);

        JScrollPane bookScroll = new JScrollPane(bookingTable);
        bookScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        bookScroll.getViewport().setBackground(Color.WHITE);
        bookingPanel.add(bookScroll, BorderLayout.CENTER);

        JPanel bookingActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        bookingActions.setOpaque(false);
        JButton rejectBtn = UITheme.createDangerButton("✕ Reject Request");
        JButton approveBtn = UITheme.createSuccessButton("✔ Approve Request");
        approveBtn.setPreferredSize(new Dimension(160, 36));
        rejectBtn.setPreferredSize(new Dimension(150, 36));

        bookingActions.add(rejectBtn);
        bookingActions.add(approveBtn);
        bookingPanel.add(bookingActions, BorderLayout.SOUTH);

        tabbedPane.addTab("Booking Requests", bookingPanel);

        // =========================================================================
        // TAB 2: RESOURCE MANAGEMENT
        // =========================================================================
        JPanel resourcePanel = new JPanel(new BorderLayout(12, 12));
        resourcePanel.setBackground(UITheme.BG_CARD);
        resourcePanel.setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel resourceTopBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        resourceTopBar.setOpaque(false);
        JButton addResourceBtn = UITheme.createPrimaryButton("+ Add New Resource");
        addResourceBtn.setPreferredSize(new Dimension(180, 34));
        resourceTopBar.add(addResourceBtn);
        resourcePanel.add(resourceTopBar, BorderLayout.NORTH);

        resourceModel = new DefaultTableModel(new Object[]{
                "ID", "Resource Name", "Category", "Location", "Capacity", "Availability"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        resourceTable = new JTable(resourceModel);
        UITheme.styleTable(resourceTable);

        JScrollPane resScroll = new JScrollPane(resourceTable);
        resScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        resScroll.getViewport().setBackground(Color.WHITE);
        resourcePanel.add(resScroll, BorderLayout.CENTER);

        JPanel resourceActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        resourceActions.setOpaque(false);
        JButton refreshResBtn = UITheme.createSecondaryButton("↻ Refresh");
        JButton toggleAvailBtn = UITheme.createPrimaryButton("⇄ Toggle Availability");
        JButton deleteResBtn = UITheme.createDangerButton("🗑 Delete Resource");

        refreshResBtn.setPreferredSize(new Dimension(100, 36));
        toggleAvailBtn.setPreferredSize(new Dimension(180, 36));
        deleteResBtn.setPreferredSize(new Dimension(150, 36));

        resourceActions.add(refreshResBtn);
        resourceActions.add(toggleAvailBtn);
        resourceActions.add(deleteResBtn);
        resourcePanel.add(resourceActions, BorderLayout.SOUTH);

        tabbedPane.addTab("Manage Resources", resourcePanel);

        // =========================================================================
        // TAB 3: UTILIZATION & REPORTS
        // =========================================================================
        JPanel reportPanel = new JPanel(new BorderLayout(12, 12));
        reportPanel.setBackground(UITheme.BG_CARD);
        reportPanel.setBorder(new EmptyBorder(14, 14, 14, 14));

        // Modern 4 Stat Cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 14, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(new EmptyBorder(0, 0, 8, 0));
        cardsPanel.add(UITheme.createStatCard("Total Resources", statTotalResources, UITheme.PRIMARY, "Active campus facilities"));
        cardsPanel.add(UITheme.createStatCard("Total Bookings", statTotalBookings, UITheme.PURPLE, "Lifetime slot requests"));
        cardsPanel.add(UITheme.createStatCard("Pending Requests", statPendingBookings, UITheme.WARNING, "Requiring admin review"));
        cardsPanel.add(UITheme.createStatCard("Approved Bookings", statApprovedBookings, UITheme.SUCCESS, "Confirmed reservations"));
        reportPanel.add(cardsPanel, BorderLayout.NORTH);

        utilizationModel = new DefaultTableModel(new Object[]{
                "Resource ID", "Resource Name", "Location", "Total Requests", "Approved Bookings", "Utilization Rate"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        utilizationTable = new JTable(utilizationModel);
        UITheme.styleTable(utilizationTable);

        JScrollPane utilScroll = new JScrollPane(utilizationTable);
        utilScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        utilScroll.getViewport().setBackground(Color.WHITE);
        reportPanel.add(utilScroll, BorderLayout.CENTER);

        JPanel reportActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        reportActions.setOpaque(false);
        JButton refreshReportBtn = UITheme.createSecondaryButton("↻ Refresh Analytics");
        refreshReportBtn.setPreferredSize(new Dimension(160, 36));
        reportActions.add(refreshReportBtn);
        reportPanel.add(reportActions, BorderLayout.SOUTH);

        tabbedPane.addTab("Utilization & Reports", reportPanel);

        bodyPanel.add(tabbedPane, BorderLayout.CENTER);
        root.add(bodyPanel, BorderLayout.CENTER);
        setContentPane(root);

        // Listeners for Tab 1
        statusFilterCombo.addActionListener(e -> refreshBookings());
        refreshBookingsBtn.addActionListener(e -> refreshBookings());
        approveBtn.addActionListener(e -> handleReviewBooking(true));
        rejectBtn.addActionListener(e -> handleReviewBooking(false));

        // Listeners for Tab 2
        addResourceBtn.addActionListener(e -> openAddResourceDialog());
        toggleAvailBtn.addActionListener(e -> handleToggleAvailability());
        deleteResBtn.addActionListener(e -> handleDeleteResource());
        refreshResBtn.addActionListener(e -> refreshResources());

        // Listeners for Tab 3
        refreshReportBtn.addActionListener(e -> refreshReports());

        // Tab change listener to auto-refresh active view
        tabbedPane.addChangeListener(e -> {
            int selected = tabbedPane.getSelectedIndex();
            if (selected == 0) refreshBookings();
            else if (selected == 1) refreshResources();
            else if (selected == 2) refreshReports();
        });

        // Initial Data Load
        refreshBookings();
        refreshResources();
        refreshReports();
    }

    private void refreshBookings() {
        bookingModel.setRowCount(0);
        String filter = (String) statusFilterCombo.getSelectedItem();
        int pendingCount = 0;

        for (Booking b : DataStore.BOOKINGS) {
            if (b.getStatus() == BookingStatus.PENDING) {
                pendingCount++;
            }

            boolean matches = filter == null
                    || filter.equals("All Requests")
                    || b.getStatus().name().equalsIgnoreCase(filter);

            if (matches) {
                bookingModel.addRow(new Object[]{
                        b.getId(),
                        b.getUser().getName(),
                        b.getUser().getRole(),
                        b.getResource().getName(),
                        b.getDate().toString(),
                        b.getStart() + " - " + b.getEnd(),
                        b.getPurpose(),
                        b.getStatus(),
                        b.getAdminRemark() == null ? "—" : b.getAdminRemark()
                });
            }
        }
        pendingCountLabel.setText("Pending Action Required: " + pendingCount);
    }

    private void handleReviewBooking(boolean approve) {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a booking from the table to review.",
                    "No Booking Selected", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String bookingId = (String) bookingModel.getValueAt(selectedRow, 0);
        Booking booking = null;
        for (Booking b : DataStore.BOOKINGS) {
            if (b.getId().equals(bookingId)) {
                booking = b;
                break;
            }
        }

        if (booking == null) return;

        if (booking.getStatus() != BookingStatus.PENDING) {
            JOptionPane.showMessageDialog(this,
                    "Only PENDING booking requests can be reviewed.\nThis booking is already " + booking.getStatus() + ".",
                    "Invalid Action", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String prompt = approve ? "Approval remark (Optional):" : "Reason for rejection:";
        String defaultText = approve ? "Approved by admin" : "Schedule conflict or maintenance";
        String remark = JOptionPane.showInputDialog(this, prompt, defaultText);

        // If user cancelled the input dialog
        if (remark == null) {
            return;
        }

        try {
            if (approve) {
                service.approveBooking(booking, remark);
                JOptionPane.showMessageDialog(this,
                        "Booking " + booking.getId() + " has been approved successfully.",
                        "Approved", JOptionPane.INFORMATION_MESSAGE);
            } else {
                service.rejectBooking(booking, remark);
                JOptionPane.showMessageDialog(this,
                        "Booking " + booking.getId() + " has been rejected.",
                        "Rejected", JOptionPane.INFORMATION_MESSAGE);
            }
            refreshBookings();
            refreshReports();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshResources() {
        resourceModel.setRowCount(0);
        for (Resource r : DataStore.RESOURCES) {
            resourceModel.addRow(new Object[]{
                    r.getId(),
                    r.getName(),
                    r.getType(),
                    r.getLocation(),
                    r.getCapacity(),
                    r.isAvailable() ? "Available" : "Maintenance"
            });
        }
    }

    private void openAddResourceDialog() {
        JDialog dialog = new JDialog(this, "Add New Campus Resource", true);
        dialog.setSize(460, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel card = UITheme.createCard();
        card.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        String nextId = String.format("R%03d", DataStore.RESOURCES.size() + 1);
        UITheme.ModernTextField idField = new UITheme.ModernTextField(nextId, 15);
        UITheme.ModernTextField nameField = new UITheme.ModernTextField(15);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{
                "Computer Lab", "Seminar Hall", "Laboratory", "Meeting Room", "Classroom", "Auditorium"
        });
        typeCombo.setFont(UITheme.FONT_REGULAR);
        typeCombo.setBackground(Color.WHITE);
        UITheme.ModernTextField locationField = new UITheme.ModernTextField(15);
        UITheme.ModernTextField capacityField = new UITheme.ModernTextField("30", 15);

        g.gridx = 0; g.gridy = 0;
        JLabel idLbl = new JLabel("RESOURCE ID:"); idLbl.setFont(UITheme.FONT_BOLD); idLbl.setForeground(UITheme.TEXT_MUTED);
        card.add(idLbl, g);
        g.gridx = 1; card.add(idField, g);

        g.gridx = 0; g.gridy = 1;
        JLabel nameLbl = new JLabel("RESOURCE NAME:"); nameLbl.setFont(UITheme.FONT_BOLD); nameLbl.setForeground(UITheme.TEXT_MUTED);
        card.add(nameLbl, g);
        g.gridx = 1; card.add(nameField, g);

        g.gridx = 0; g.gridy = 2;
        JLabel catLbl = new JLabel("CATEGORY / TYPE:"); catLbl.setFont(UITheme.FONT_BOLD); catLbl.setForeground(UITheme.TEXT_MUTED);
        card.add(catLbl, g);
        g.gridx = 1; card.add(typeCombo, g);

        g.gridx = 0; g.gridy = 3;
        JLabel locLbl = new JLabel("LOCATION:"); locLbl.setFont(UITheme.FONT_BOLD); locLbl.setForeground(UITheme.TEXT_MUTED);
        card.add(locLbl, g);
        g.gridx = 1; card.add(locationField, g);

        g.gridx = 0; g.gridy = 4;
        JLabel capLbl = new JLabel("CAPACITY (SEATS):"); capLbl.setFont(UITheme.FONT_BOLD); capLbl.setForeground(UITheme.TEXT_MUTED);
        card.add(capLbl, g);
        g.gridx = 1; card.add(capacityField, g);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(UITheme.BG_APP);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 8, 16));
        contentWrapper.add(card, BorderLayout.CENTER);
        dialog.add(contentWrapper, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 14));
        btnPanel.setBackground(UITheme.BG_APP);
        JButton cancelBtn = UITheme.createSecondaryButton("Cancel");
        JButton saveBtn = UITheme.createPrimaryButton("+ Add Resource");
        saveBtn.setPreferredSize(new Dimension(150, 36));
        cancelBtn.setPreferredSize(new Dimension(90, 36));

        cancelBtn.addActionListener(e -> dialog.dispose());
        saveBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String type = (String) typeCombo.getSelectedItem();
            String location = locationField.getText().trim();
            String capStr = capacityField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || location.isEmpty() || capStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.");
                return;
            }

            try {
                int capacity = Integer.parseInt(capStr);
                service.addResource(id, name, type, location, capacity);
                JOptionPane.showMessageDialog(dialog, "Resource '" + name + "' added successfully!");
                dialog.dispose();
                refreshResources();
                refreshReports();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(dialog, "Capacity must be a valid integer number.");
            } catch (IllegalArgumentException iae) {
                JOptionPane.showMessageDialog(dialog, iae.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void handleToggleAvailability() {
        int selectedRow = resourceTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a resource to toggle its availability.");
            return;
        }

        String resId = (String) resourceModel.getValueAt(selectedRow, 0);
        for (Resource r : DataStore.RESOURCES) {
            if (r.getId().equals(resId)) {
                r.setAvailable(!r.isAvailable());
                JOptionPane.showMessageDialog(this,
                        "Resource '" + r.getName() + "' is now " + (r.isAvailable() ? "AVAILABLE" : "UNDER MAINTENANCE") + ".");
                break;
            }
        }
        refreshResources();
    }

    private void handleDeleteResource() {
        int selectedRow = resourceTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a resource to delete.");
            return;
        }

        String resId = (String) resourceModel.getValueAt(selectedRow, 0);
        Resource target = null;
        for (Resource r : DataStore.RESOURCES) {
            if (r.getId().equals(resId)) {
                target = r;
                break;
            }
        }

        if (target == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete resource '" + target.getName() + "' (" + target.getId() + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.deleteResource(target);
                JOptionPane.showMessageDialog(this, "Resource deleted successfully.");
                refreshResources();
                refreshReports();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Delete Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void refreshReports() {
        int totalRes = DataStore.RESOURCES.size();
        int totalBookings = DataStore.BOOKINGS.size();
        long pending = DataStore.BOOKINGS.stream().filter(b -> b.getStatus() == BookingStatus.PENDING).count();
        long approved = DataStore.BOOKINGS.stream().filter(b -> b.getStatus() == BookingStatus.APPROVED).count();

        statTotalResources.setText(String.valueOf(totalRes));
        statTotalBookings.setText(String.valueOf(totalBookings));
        statPendingBookings.setText(String.valueOf(pending));
        statApprovedBookings.setText(String.valueOf(approved));

        utilizationModel.setRowCount(0);
        for (Resource r : DataStore.RESOURCES) {
            long reqCount = DataStore.BOOKINGS.stream()
                    .filter(b -> b.getResource().getId().equals(r.getId()))
                    .count();

            long appCount = DataStore.BOOKINGS.stream()
                    .filter(b -> b.getResource().getId().equals(r.getId()) && b.getStatus() == BookingStatus.APPROVED)
                    .count();

            String rate = totalBookings > 0 ? String.format("%.1f%%", (appCount * 100.0) / Math.max(1, totalBookings)) : "0%";

            utilizationModel.addRow(new Object[]{
                    r.getId(),
                    r.getName(),
                    r.getLocation(),
                    reqCount,
                    appCount,
                    rate
            });
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to log out of the Admin panel?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
