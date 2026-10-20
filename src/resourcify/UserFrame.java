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
    private final UITheme.ModernTextField searchField = new UITheme.ModernTextField(18);
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
        setSize(1040, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_APP);

        // Modern Top Navbar
        JPanel topNavbar = UITheme.createNavbar("Campus Resource Booking Portal", user, this::handleLogout);
        root.add(topNavbar, BorderLayout.NORTH);

        // Body with Padding
        JPanel bodyPanel = new JPanel(new BorderLayout(12, 12));
        bodyPanel.setBackground(UITheme.BG_APP);
        bodyPanel.setBorder(new EmptyBorder(16, 20, 16, 20));

        // Tabbed Navigation
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UITheme.FONT_BOLD);
        tabbedPane.setBackground(Color.WHITE);

        // =====================================================================
        // TAB 1: BROWSE RESOURCES
        // =====================================================================
        JPanel resourcePanel = new JPanel(new BorderLayout(12, 12));
        resourcePanel.setBackground(UITheme.BG_CARD);
        resourcePanel.setBorder(new EmptyBorder(14, 14, 14, 14));

        // Modern Filter Bar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        filterBar.setOpaque(false);

        JLabel searchLbl = new JLabel("Search Location/Name:");
        searchLbl.setFont(UITheme.FONT_BOLD);
        searchLbl.setForeground(UITheme.TEXT_MAIN);
        filterBar.add(searchLbl);
        filterBar.add(searchField);

        JLabel typeLbl = new JLabel("Category:");
        typeLbl.setFont(UITheme.FONT_BOLD);
        typeLbl.setForeground(UITheme.TEXT_MAIN);
        filterBar.add(typeLbl);

        typeFilterCombo.setFont(UITheme.FONT_REGULAR);
        typeFilterCombo.setBackground(Color.WHITE);
        typeFilterCombo.setPreferredSize(new Dimension(140, 34));
        filterBar.add(typeFilterCombo);

        JButton filterBtn = UITheme.createPrimaryButton("Filter");
        JButton resetFilterBtn = UITheme.createSecondaryButton("Reset");
        filterBtn.setPreferredSize(new Dimension(85, 34));
        resetFilterBtn.setPreferredSize(new Dimension(85, 34));
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
        UITheme.styleTable(resourceTable);

        JScrollPane resScrollPane = new JScrollPane(resourceTable);
        resScrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        resScrollPane.getViewport().setBackground(Color.WHITE);
        resourcePanel.add(resScrollPane, BorderLayout.CENTER);

        // Resource Actions
        JPanel resourceActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        resourceActions.setOpaque(false);
        JButton refreshResBtn = UITheme.createSecondaryButton("↻ Refresh");
        JButton bookBtn = UITheme.createPrimaryButton("+ Book Selected Resource...");
        bookBtn.setPreferredSize(new Dimension(220, 36));
        refreshResBtn.setPreferredSize(new Dimension(100, 36));

        resourceActions.add(refreshResBtn);
        resourceActions.add(bookBtn);
        resourcePanel.add(resourceActions, BorderLayout.SOUTH);

        tabbedPane.addTab("Browse Resources", resourcePanel);

        // =====================================================================
        // TAB 2: MY BOOKINGS
        // =====================================================================
        JPanel myBookingsPanel = new JPanel(new BorderLayout(12, 12));
        myBookingsPanel.setBackground(UITheme.BG_CARD);
        myBookingsPanel.setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel bookingsFilterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        bookingsFilterBar.setOpaque(false);
        JLabel statusLbl = new JLabel("Filter by Status:");
        statusLbl.setFont(UITheme.FONT_BOLD);
        statusLbl.setForeground(UITheme.TEXT_MAIN);
        bookingsFilterBar.add(statusLbl);

        statusFilterCombo.setFont(UITheme.FONT_REGULAR);
        statusFilterCombo.setBackground(Color.WHITE);
        statusFilterCombo.setPreferredSize(new Dimension(140, 34));
        bookingsFilterBar.add(statusFilterCombo);

        JButton refreshBookingsBtn = UITheme.createSecondaryButton("↻ Refresh");
        refreshBookingsBtn.setPreferredSize(new Dimension(100, 34));
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
        UITheme.styleTable(myBookingsTable);

        JScrollPane bookScrollPane = new JScrollPane(myBookingsTable);
        bookScrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        bookScrollPane.getViewport().setBackground(Color.WHITE);
        myBookingsPanel.add(bookScrollPane, BorderLayout.CENTER);

        JPanel myBookingsActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        myBookingsActions.setOpaque(false);
        JButton cancelBookingBtn = UITheme.createDangerButton("✕ Cancel Selected Booking");
        cancelBookingBtn.setPreferredSize(new Dimension(200, 36));
        myBookingsActions.add(cancelBookingBtn);
        myBookingsPanel.add(myBookingsActions, BorderLayout.SOUTH);

        tabbedPane.addTab("My Bookings", myBookingsPanel);

        bodyPanel.add(tabbedPane, BorderLayout.CENTER);
        root.add(bodyPanel, BorderLayout.CENTER);
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

        // Build Modern Booking Dialog
        JDialog dialog = new JDialog(this, "Book Facility — " + resource.getName(), true);
        dialog.setSize(480, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel card = UITheme.createCard();
        card.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        // Resource details
        g.gridx = 0; g.gridy = 0;
        JLabel selLbl = new JLabel("FACILITY:");
        selLbl.setFont(UITheme.FONT_BOLD);
        selLbl.setForeground(UITheme.TEXT_MUTED);
        card.add(selLbl, g);

        g.gridx = 1;
        JLabel resNameLabel = new JLabel(resource.getName() + " (" + resource.getLocation() + ")");
        resNameLabel.setFont(UITheme.FONT_TITLE);
        resNameLabel.setForeground(UITheme.PRIMARY);
        card.add(resNameLabel, g);

        // Date field
        g.gridx = 0; g.gridy = 1;
        JLabel dateLbl = new JLabel("DATE (YYYY-MM-DD):");
        dateLbl.setFont(UITheme.FONT_BOLD);
        dateLbl.setForeground(UITheme.TEXT_MUTED);
        card.add(dateLbl, g);

        g.gridx = 1;
        UITheme.ModernTextField dateField = new UITheme.ModernTextField(LocalDate.now().plusDays(1).toString(), 12);
        card.add(dateField, g);

        // Start Time
        g.gridx = 0; g.gridy = 2;
        JLabel startLbl = new JLabel("START TIME:");
        startLbl.setFont(UITheme.FONT_BOLD);
        startLbl.setForeground(UITheme.TEXT_MUTED);
        card.add(startLbl, g);

        g.gridx = 1;
        JComboBox<String> startCombo = new JComboBox<>(new String[]{
                "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"
        });
        startCombo.setFont(UITheme.FONT_REGULAR);
        startCombo.setBackground(Color.WHITE);
        startCombo.setSelectedItem("10:00");
        card.add(startCombo, g);

        // End Time
        g.gridx = 0; g.gridy = 3;
        JLabel endLbl = new JLabel("END TIME:");
        endLbl.setFont(UITheme.FONT_BOLD);
        endLbl.setForeground(UITheme.TEXT_MUTED);
        card.add(endLbl, g);

        g.gridx = 1;
        JComboBox<String> endCombo = new JComboBox<>(new String[]{
                "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"
        });
        endCombo.setFont(UITheme.FONT_REGULAR);
        endCombo.setBackground(Color.WHITE);
        endCombo.setSelectedItem("12:00");
        card.add(endCombo, g);

        // Purpose
        g.gridx = 0; g.gridy = 4;
        JLabel purpLbl = new JLabel("PURPOSE:");
        purpLbl.setFont(UITheme.FONT_BOLD);
        purpLbl.setForeground(UITheme.TEXT_MUTED);
        card.add(purpLbl, g);

        g.gridx = 1;
        UITheme.ModernTextField purposeField = new UITheme.ModernTextField(15);
        card.add(purposeField, g);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(UITheme.BG_APP);
        contentWrapper.setBorder(new EmptyBorder(16, 16, 8, 16));
        contentWrapper.add(card, BorderLayout.CENTER);
        dialog.add(contentWrapper, BorderLayout.CENTER);

        // Dialog buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 14));
        btnPanel.setBackground(UITheme.BG_APP);
        JButton cancelBtn = UITheme.createSecondaryButton("Cancel");
        JButton confirmBtn = UITheme.createPrimaryButton("Submit Booking Request");
        confirmBtn.setPreferredSize(new Dimension(190, 36));
        cancelBtn.setPreferredSize(new Dimension(90, 36));

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
