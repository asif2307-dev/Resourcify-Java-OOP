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
        setTitle("Resourcify — Administrator Dashboard");
        setSize(1080, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Top Navigation Header
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(0, 0, 10, 0)
        ));

        JPanel adminInfo = new JPanel(new GridLayout(2, 1));
        JLabel titleLabel = new JLabel("Resourcify — Administrator Control Center");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel adminLabel = new JLabel("Admin: " + admin.getName() + " (" + admin.getEmail() + ")");
        adminLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        adminLabel.setForeground(Color.DARK_GRAY);
        adminInfo.add(titleLabel);
        adminInfo.add(adminLabel);
        topHeader.add(adminInfo, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> handleLogout());
        topHeader.add(logoutBtn, BorderLayout.EAST);

        root.add(topHeader, BorderLayout.NORTH);

        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // =========================================================================
        // TAB 1: BOOKING REQUESTS & APPROVALS
        // =========================================================================
        JPanel bookingPanel = new JPanel(new BorderLayout(10, 10));
        bookingPanel.setBorder(new EmptyBorder(10, 5, 5, 5));

        JPanel bookingFilterBar = new JPanel(new BorderLayout());
        JPanel leftFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        leftFilter.add(new JLabel("Filter Status:"));
        leftFilter.add(statusFilterCombo);
        JButton refreshBookingsBtn = new JButton("Refresh");
        leftFilter.add(refreshBookingsBtn);
        bookingFilterBar.add(leftFilter, BorderLayout.WEST);

        pendingCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pendingCountLabel.setForeground(new Color(180, 70, 0));
        JPanel rightFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
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
        bookingTable.setRowHeight(24);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingPanel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);

        JPanel bookingActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton approveBtn = new JButton("Approve Request");
        approveBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JButton rejectBtn = new JButton("Reject Request");
        bookingActions.add(approveBtn);
        bookingActions.add(rejectBtn);
        bookingPanel.add(bookingActions, BorderLayout.SOUTH);

        tabbedPane.addTab("Booking Requests", bookingPanel);

        // =========================================================================
        // TAB 2: RESOURCE MANAGEMENT
        // =========================================================================
        JPanel resourcePanel = new JPanel(new BorderLayout(10, 10));
        resourcePanel.setBorder(new EmptyBorder(10, 5, 5, 5));

        JPanel resourceTopBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton addResourceBtn = new JButton("+ Add New Resource");
        addResourceBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
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
        resourceTable.setRowHeight(24);
        resourceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resourcePanel.add(new JScrollPane(resourceTable), BorderLayout.CENTER);

        JPanel resourceActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton toggleAvailBtn = new JButton("Toggle Availability");
        JButton deleteResBtn = new JButton("Delete Resource");
        JButton refreshResBtn = new JButton("Refresh");
        resourceActions.add(refreshResBtn);
        resourceActions.add(toggleAvailBtn);
        resourceActions.add(deleteResBtn);
        resourcePanel.add(resourceActions, BorderLayout.SOUTH);

        tabbedPane.addTab("Manage Resources", resourcePanel);

        // =========================================================================
        // TAB 3: UTILIZATION & REPORTS
        // =========================================================================
        JPanel reportPanel = new JPanel(new BorderLayout(10, 10));
        reportPanel.setBorder(new EmptyBorder(10, 5, 5, 5));

        // Stats Cards Panel
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setBorder(new EmptyBorder(5, 5, 10, 5));
        cardsPanel.add(createStatCard("Total Resources", statTotalResources));
        cardsPanel.add(createStatCard("Total Bookings", statTotalBookings));
        cardsPanel.add(createStatCard("Pending Requests", statPendingBookings));
        cardsPanel.add(createStatCard("Approved Bookings", statApprovedBookings));
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
        utilizationTable.setRowHeight(24);
        reportPanel.add(new JScrollPane(utilizationTable), BorderLayout.CENTER);

        JPanel reportActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton refreshReportBtn = new JButton("Refresh Analytics");
        reportActions.add(refreshReportBtn);
        reportPanel.add(reportActions, BorderLayout.SOUTH);

        tabbedPane.addTab("Utilization & Reports", reportPanel);

        root.add(tabbedPane, BorderLayout.CENTER);
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

    private JPanel createStatCard(String label, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 225), 1),
                new EmptyBorder(10, 15, 10, 15)
        ));
        card.setBackground(new Color(248, 250, 252));

        JLabel titleLbl = new JLabel(label);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLbl.setForeground(Color.DARK_GRAY);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(new Color(24, 76, 140));

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
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
        pendingCountLabel.setText("Pending Requests: " + pendingCount);
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
        dialog.setSize(420, 360);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(15, 20, 10, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        String nextId = String.format("R%03d", DataStore.RESOURCES.size() + 1);
        JTextField idField = new JTextField(nextId, 15);
        JTextField nameField = new JTextField(15);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{
                "Computer Lab", "Seminar Hall", "Laboratory", "Meeting Room", "Classroom", "Auditorium"
        });
        JTextField locationField = new JTextField(15);
        JTextField capacityField = new JTextField("30", 15);

        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Resource ID:"), g);
        g.gridx = 1; form.add(idField, g);

        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Resource Name:"), g);
        g.gridx = 1; form.add(nameField, g);

        g.gridx = 0; g.gridy = 2; form.add(new JLabel("Category / Type:"), g);
        g.gridx = 1; form.add(typeCombo, g);

        g.gridx = 0; g.gridy = 3; form.add(new JLabel("Location:"), g);
        g.gridx = 1; form.add(locationField, g);

        g.gridx = 0; g.gridy = 4; form.add(new JLabel("Capacity (People):"), g);
        g.gridx = 1; form.add(capacityField, g);

        dialog.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton cancelBtn = new JButton("Cancel");
        JButton saveBtn = new JButton("Add Resource");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));

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
