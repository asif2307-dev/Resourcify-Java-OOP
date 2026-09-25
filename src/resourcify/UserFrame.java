package resourcify;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class UserFrame extends JFrame {
    private final User user;
    private final BookingService service = new BookingService();

    // Resource browsing tab components
    private final DefaultTableModel resourceModel;
    private final JTable resourceTable;
    private final JTextField searchField = new JTextField(15);
    private final JComboBox<String> typeFilterCombo = new JComboBox<>(new String[]{
            "All Types", "Computer Lab", "Seminar Hall", "Laboratory", "Meeting Room", "Classroom"
    });

    // My Bookings tab components
    private final DefaultTableModel myBookingsModel;
    private final JTable myBookingsTable;
    private final JComboBox<String> statusFilterCombo = new JComboBox<>(new String[]{
            "All Bookings", "PENDING", "APPROVED", "REJECTED", "CANCELLED"
    });

    public UserFrame(User user) {
        this.user = user;
        setTitle("Resourcify — Campus Resource Portal (" + user.getRole() + ")");
        setSize(1000, 650);
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

        JPanel userInfo = new JPanel(new GridLayout(2, 1));
        JLabel titleLabel = new JLabel("Campus Resource Booking Portal");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel userLabel = new JLabel("Logged in as: " + user.getName() + " | Role: " + user.getRole() + " (" + user.getEmail() + ")");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLabel.setForeground(Color.DARK_GRAY);
        userInfo.add(titleLabel);
        userInfo.add(userLabel);
        topHeader.add(userInfo, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> handleLogout());
        topHeader.add(logoutBtn, BorderLayout.EAST);

        root.add(topHeader, BorderLayout.NORTH);

        // Tabbed Pane for Navigation
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // --- TAB 1: BROWSE RESOURCES ---
        JPanel resourcePanel = new JPanel(new BorderLayout(10, 10));
        resourcePanel.setBorder(new EmptyBorder(10, 5, 5, 5));

        // Filter Bar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterBar.add(new JLabel("Search Name/Location:"));
        filterBar.add(searchField);
        filterBar.add(new JLabel("Category:"));
        filterBar.add(typeFilterCombo);
        JButton filterBtn = new JButton("Filter");
        JButton resetFilterBtn = new JButton("Reset");
        filterBar.add(filterBtn);
        filterBar.add(resetFilterBtn);
        resourcePanel.add(filterBar, BorderLayout.NORTH);

        resourceModel = new DefaultTableModel(new Object[]{
                "Resource ID", "Resource Name", "Category", "Location", "Capacity", "Availability"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resourceTable = new JTable(resourceModel);
        resourceTable.setRowHeight(24);
        resourceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resourcePanel.add(new JScrollPane(resourceTable), BorderLayout.CENTER);

        // Resource Actions
        JPanel resourceActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton bookBtn = new JButton("Book Selected Resource...");
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JButton refreshResBtn = new JButton("Refresh");
        resourceActions.add(refreshResBtn);
        resourceActions.add(bookBtn);
        resourcePanel.add(resourceActions, BorderLayout.SOUTH);

        tabbedPane.addTab("Browse Resources", resourcePanel);

        // --- TAB 2: MY BOOKINGS ---
        JPanel myBookingsPanel = new JPanel(new BorderLayout(10, 10));
        myBookingsPanel.setBorder(new EmptyBorder(10, 5, 5, 5));

        JPanel bookingsFilterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        bookingsFilterBar.add(new JLabel("Filter by Status:"));
        bookingsFilterBar.add(statusFilterCombo);
        JButton refreshBookingsBtn = new JButton("Refresh");
        bookingsFilterBar.add(refreshBookingsBtn);
        myBookingsPanel.add(bookingsFilterBar, BorderLayout.NORTH);

        myBookingsModel = new DefaultTableModel(new Object[]{
                "Booking ID", "Resource", "Location", "Date", "Time Slot", "Purpose", "Status", "Admin Remarks"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        myBookingsTable = new JTable(myBookingsModel);
        myBookingsTable.setRowHeight(24);
        myBookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myBookingsPanel.add(new JScrollPane(myBookingsTable), BorderLayout.CENTER);

        JPanel myBookingsActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton cancelBookingBtn = new JButton("Cancel Selected Booking");
        myBookingsActions.add(cancelBookingBtn);
        myBookingsPanel.add(myBookingsActions, BorderLayout.SOUTH);

        tabbedPane.addTab("My Bookings", myBookingsPanel);

        root.add(tabbedPane, BorderLayout.CENTER);
        setContentPane(root);

        // Action Listeners
        filterBtn.addActionListener(e -> refreshResources());
        resetFilterBtn.addActionListener(e -> {
            searchField.setText("");
            typeFilterCombo.setSelectedIndex(0);
            refreshResources();
        });
        refreshResBtn.addActionListener(e -> refreshResources());
        bookBtn.addActionListener(e -> openBookingDialog());

        statusFilterCombo.addActionListener(e -> refreshMyBookings());
        refreshBookingsBtn.addActionListener(e -> refreshMyBookings());
        cancelBookingBtn.addActionListener(e -> handleCancelBooking());

        // Initial Data Load
        refreshResources();
        refreshMyBookings();
    }

    private void refreshResources() {
        resourceModel.setRowCount(0);
        String search = searchField.getText().trim().toLowerCase();
        String selectedType = (String) typeFilterCombo.getSelectedItem();

        for (Resource r : DataStore.RESOURCES) {
            boolean matchesSearch = search.isEmpty()
                    || r.getName().toLowerCase().contains(search)
                    || r.getLocation().toLowerCase().contains(search)
                    || r.getId().toLowerCase().contains(search);

            boolean matchesType = selectedType == null
                    || selectedType.equals("All Types")
                    || r.getType().equalsIgnoreCase(selectedType);

            if (matchesSearch && matchesType) {
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
    }

    private void refreshMyBookings() {
        myBookingsModel.setRowCount(0);
        String selectedStatus = (String) statusFilterCombo.getSelectedItem();

        for (Booking b : DataStore.BOOKINGS) {
            if (b.getUser().getId().equals(user.getId())) {
                boolean matchesStatus = selectedStatus == null
                        || selectedStatus.equals("All Bookings")
                        || b.getStatus().name().equalsIgnoreCase(selectedStatus);

                if (matchesStatus) {
                    myBookingsModel.addRow(new Object[]{
                            b.getId(),
                            b.getResource().getName(),
                            b.getResource().getLocation(),
                            b.getDate().toString(),
                            b.getStart() + " - " + b.getEnd(),
                            b.getPurpose(),
                            b.getStatus(),
                            b.getAdminRemark() == null ? "—" : b.getAdminRemark()
                    });
                }
            }
        }
    }

    private void openBookingDialog() {
        int selectedRow = resourceTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a resource from the table to book.",
                    "No Resource Selected", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String resourceId = (String) resourceModel.getValueAt(selectedRow, 0);
        Resource resource = null;
        for (Resource r : DataStore.RESOURCES) {
            if (r.getId().equals(resourceId)) {
                resource = r;
                break;
            }
        }

        if (resource == null) {
            JOptionPane.showMessageDialog(this, "Selected resource not found.");
            return;
        }

        if (!resource.isAvailable()) {
            JOptionPane.showMessageDialog(this,
                    "This resource is currently marked as unavailable or under maintenance.",
                    "Resource Unavailable", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Build Booking Dialog
        JDialog dialog = new JDialog(this, "Book Resource: " + resource.getName(), true);
        dialog.setSize(440, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(15, 20, 10, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        // Resource details
        g.gridx = 0; g.gridy = 0;
        form.add(new JLabel("Selected Resource:"), g);
        g.gridx = 1;
        JLabel resNameLabel = new JLabel(resource.getName() + " (" + resource.getLocation() + ")");
        resNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        form.add(resNameLabel, g);

        // Date field
        g.gridx = 0; g.gridy = 1;
        form.add(new JLabel("Date (YYYY-MM-DD):"), g);
        g.gridx = 1;
        JTextField dateField = new JTextField(LocalDate.now().plusDays(1).toString(), 12);
        form.add(dateField, g);

        // Start Time
        g.gridx = 0; g.gridy = 2;
        form.add(new JLabel("Start Time:"), g);
        g.gridx = 1;
        JComboBox<String> startCombo = new JComboBox<>(new String[]{
                "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"
        });
        startCombo.setSelectedItem("10:00");
        form.add(startCombo, g);

        // End Time
        g.gridx = 0; g.gridy = 3;
        form.add(new JLabel("End Time:"), g);
        g.gridx = 1;
        JComboBox<String> endCombo = new JComboBox<>(new String[]{
                "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"
        });
        endCombo.setSelectedItem("12:00");
        form.add(endCombo, g);

        // Purpose
        g.gridx = 0; g.gridy = 4;
        form.add(new JLabel("Purpose:"), g);
        g.gridx = 1;
        JTextField purposeField = new JTextField(15);
        form.add(purposeField, g);

        dialog.add(form, BorderLayout.CENTER);

        // Dialog buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton cancelBtn = new JButton("Cancel");
        JButton confirmBtn = new JButton("Submit Request");
        confirmBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));

        cancelBtn.addActionListener(e -> dialog.dispose());

        final Resource finalResource = resource;
        confirmBtn.addActionListener(e -> {
            String dateStr = dateField.getText().trim();
            String purpose = purposeField.getText().trim();

            if (dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a booking date.");
                return;
            }
            if (purpose.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter the purpose of the booking.");
                return;
            }

            try {
                LocalDate date = LocalDate.parse(dateStr);
                LocalTime start = LocalTime.parse((String) startCombo.getSelectedItem());
                LocalTime end = LocalTime.parse((String) endCombo.getSelectedItem());

                Booking booking = service.createBooking(user, finalResource, date, start, end, purpose);

                JOptionPane.showMessageDialog(dialog,
                        "Booking request submitted successfully!\n\n"
                                + "Booking ID: " + booking.getId() + "\n"
                                + "Resource: " + finalResource.getName() + "\n"
                                + "Date: " + date + " (" + start + " - " + end + ")\n"
                                + "Status: " + booking.getStatus(),
                        "Booking Created", JOptionPane.INFORMATION_MESSAGE);

                dialog.dispose();
                refreshMyBookings();
            } catch (DateTimeParseException dtEx) {
                JOptionPane.showMessageDialog(dialog,
                        "Invalid date format. Please use YYYY-MM-DD (e.g. 2026-10-15).",
                        "Date Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException argEx) {
                JOptionPane.showMessageDialog(dialog, argEx.getMessage(),
                        "Booking Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(confirmBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void handleCancelBooking() {
        int selectedRow = myBookingsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a booking from the table to cancel.",
                    "No Booking Selected", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String bookingId = (String) myBookingsModel.getValueAt(selectedRow, 0);
        Booking target = null;
        for (Booking b : DataStore.BOOKINGS) {
            if (b.getId().equals(bookingId)) {
                target = b;
                break;
            }
        }

        if (target == null) return;

        if (target.getStatus() == BookingStatus.CANCELLED) {
            JOptionPane.showMessageDialog(this, "This booking is already cancelled.");
            return;
        }

        if (target.getStatus() == BookingStatus.REJECTED) {
            JOptionPane.showMessageDialog(this, "Cannot cancel a booking that was already rejected.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel booking " + target.getId() + "?",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.cancelBooking(target, user);
                JOptionPane.showMessageDialog(this, "Booking cancelled successfully.");
                refreshMyBookings();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to log out?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
