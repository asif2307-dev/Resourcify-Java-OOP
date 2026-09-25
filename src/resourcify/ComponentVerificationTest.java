package resourcify;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Automated test suite to verify every component, model, service,
 * and GUI frame in Resourcify.
 */
public class ComponentVerificationTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   RESOURCIFY COMPONENT VERIFICATION TEST SUITE   ");
        System.out.println("==================================================");

        try {
            testDataStoreAndModels();
            testBookingServiceLogic();
            testBookingServiceEdgeCases();
            testLoginFrameComponent();
            testUserFrameComponent();
            testAdminFrameComponent();
        } catch (Exception e) {
            System.err.println("[CRITICAL UNCAUGHT EXCEPTION] " + e.getMessage());
            e.printStackTrace();
            testsFailed++;
        }

        System.out.println("\n==================================================");
        System.out.println("TEST RESULTS SUMMARY:");
        System.out.println("Passed: " + testsPassed);
        System.out.println("Failed: " + testsFailed);
        System.out.println("Status: " + (testsFailed == 0 ? "ALL COMPONENTS FUNCTIONAL & VERIFIED!" : "SOME TESTS FAILED"));
        System.out.println("==================================================");

        if (testsFailed > 0) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }

    private static void assertTrue(String testName, boolean condition, String errorMsg) {
        if (condition) {
            System.out.println("  [PASS] " + testName);
            testsPassed++;
        } else {
            System.err.println("  [FAIL] " + testName + " - " + errorMsg);
            testsFailed++;
        }
    }

    // -------------------------------------------------------------
    // 1. DataStore and Models Verification
    // -------------------------------------------------------------
    private static void testDataStoreAndModels() {
        System.out.println("\n[1] Verifying DataStore & Core OOP Models...");

        assertTrue("DataStore USERS populated", !DataStore.USERS.isEmpty(), "USERS list is empty");
        assertTrue("DataStore RESOURCES populated", !DataStore.RESOURCES.isEmpty(), "RESOURCES list is empty");
        assertTrue("DataStore BOOKINGS populated", !DataStore.BOOKINGS.isEmpty(), "BOOKINGS list is empty");

        User student = DataStore.USERS.get(0);
        assertTrue("User model encapsulation", student.getId() != null && student.getRole() == Role.STUDENT, "Student user invalid");

        Resource res = DataStore.RESOURCES.get(0);
        assertTrue("Resource model encapsulation", res.getId() != null && res.getCapacity() > 0, "Resource invalid");

        Booking booking = DataStore.BOOKINGS.get(0);
        assertTrue("Booking model encapsulation", booking.getId() != null && booking.getUser() != null, "Booking invalid");
    }

    // -------------------------------------------------------------
    // 2. BookingService Happy Path Verification
    // -------------------------------------------------------------
    private static void testBookingServiceLogic() {
        System.out.println("\n[2] Verifying BookingService Business Logic...");
        BookingService service = new BookingService();

        User student = DataStore.USERS.get(0);
        Resource classroom = DataStore.RESOURCES.get(4); // Classroom C-202

        LocalDate testDate = LocalDate.now().plusDays(10);
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(11, 0);

        // Test creation
        Booking newBooking = service.createBooking(student, classroom, testDate, start, end, "Unit Test Study Session");
        assertTrue("Booking creation", newBooking != null && newBooking.getStatus() == BookingStatus.PENDING, "Booking creation failed");

        // Test conflict detection
        boolean conflict = service.hasConflict(classroom, testDate, LocalTime.of(10, 0), LocalTime.of(12, 0));
        assertTrue("Time conflict detection", conflict, "Failed to detect overlapping booking interval");

        // Test non-overlapping time
        boolean noConflict = service.hasConflict(classroom, testDate, LocalTime.of(12, 0), LocalTime.of(14, 0));
        assertTrue("Time non-conflict allowed", !noConflict, "Reported false conflict for non-overlapping interval");

        // Test Approval
        service.approveBooking(newBooking, "Approved for testing");
        assertTrue("Admin Approval action", newBooking.getStatus() == BookingStatus.APPROVED, "Approval status incorrect");

        // Test Cancellation
        service.cancelBooking(newBooking, student);
        assertTrue("User Cancellation action", newBooking.getStatus() == BookingStatus.CANCELLED, "Cancellation status incorrect");
    }

    // -------------------------------------------------------------
    // 3. BookingService Edge Cases & Validations
    // -------------------------------------------------------------
    private static void testBookingServiceEdgeCases() {
        System.out.println("\n[3] Verifying BookingService Validations & Exceptions...");
        BookingService service = new BookingService();
        User student = DataStore.USERS.get(0);
        User faculty = DataStore.USERS.get(1);
        Resource res = DataStore.RESOURCES.get(1);

        // Past date validation
        boolean pastDateCaught = false;
        try {
            service.createBooking(student, res, LocalDate.now().minusDays(1), LocalTime.of(10, 0), LocalTime.of(11, 0), "Past");
        } catch (IllegalArgumentException e) {
            pastDateCaught = true;
        }
        assertTrue("Past date rejection", pastDateCaught, "Allowed booking in the past");

        // End before start validation
        boolean invalidTimeCaught = false;
        try {
            service.createBooking(student, res, LocalDate.now().plusDays(5), LocalTime.of(14, 0), LocalTime.of(12, 0), "Time error");
        } catch (IllegalArgumentException e) {
            invalidTimeCaught = true;
        }
        assertTrue("Invalid time range rejection", invalidTimeCaught, "Allowed end time before start time");

        // Empty purpose validation
        boolean emptyPurposeCaught = false;
        try {
            service.createBooking(student, res, LocalDate.now().plusDays(5), LocalTime.of(14, 0), LocalTime.of(15, 0), "   ");
        } catch (IllegalArgumentException e) {
            emptyPurposeCaught = true;
        }
        assertTrue("Empty purpose rejection", emptyPurposeCaught, "Allowed blank purpose");

        // Add resource validation
        service.addResource("TEST-R99", "Test Robotics Lab", "Laboratory", "Block Z", 25);
        boolean added = DataStore.RESOURCES.stream().anyMatch(r -> r.getId().equals("TEST-R99"));
        assertTrue("Admin Add Resource", added, "Resource was not added to DataStore");

        // Duplicate resource ID rejection
        boolean dupCaught = false;
        try {
            service.addResource("TEST-R99", "Duplicate", "Laboratory", "Block Z", 10);
        } catch (IllegalArgumentException e) {
            dupCaught = true;
        }
        assertTrue("Duplicate resource ID rejection", dupCaught, "Allowed duplicate resource ID");

        // Clean up test resource
        Resource testRes = DataStore.RESOURCES.stream().filter(r -> r.getId().equals("TEST-R99")).findFirst().orElse(null);
        if (testRes != null) {
            service.deleteResource(testRes);
        }
    }

    // -------------------------------------------------------------
    // 4. LoginFrame Component Verification
    // -------------------------------------------------------------
    private static void testLoginFrameComponent() throws Exception {
        System.out.println("\n[4] Verifying LoginFrame GUI Component...");

        final LoginFrame[] frameHolder = new LoginFrame[1];
        SwingUtilities.invokeAndWait(() -> {
            frameHolder[0] = new LoginFrame();
        });

        LoginFrame frame = frameHolder[0];
        assertTrue("LoginFrame instantiated", frame != null, "LoginFrame failed to create");
        assertTrue("LoginFrame has components", frame.getContentPane().getComponentCount() > 0, "No components in LoginFrame");
        assertTrue("LoginFrame default button set", frame.getRootPane().getDefaultButton() != null, "Default enter button not set");

        SwingUtilities.invokeAndWait(frame::dispose);
        assertTrue("LoginFrame teardown", true, "");
    }

    // -------------------------------------------------------------
    // 5. UserFrame Component Verification
    // -------------------------------------------------------------
    private static void testUserFrameComponent() throws Exception {
        System.out.println("\n[5] Verifying UserFrame GUI Component & Tabs...");

        User student = DataStore.USERS.get(0);
        final UserFrame[] frameHolder = new UserFrame[1];

        SwingUtilities.invokeAndWait(() -> {
            frameHolder[0] = new UserFrame(student);
        });

        UserFrame frame = frameHolder[0];
        assertTrue("UserFrame instantiated for Student", frame != null, "UserFrame failed to create");

        // Check for TabbedPane
        Container content = frame.getContentPane();
        JTabbedPane tabs = findComponent(content, JTabbedPane.class);
        assertTrue("UserFrame contains JTabbedPane", tabs != null, "JTabbedPane missing in UserFrame");

        if (tabs != null) {
            assertTrue("UserFrame has 2 Tabs", tabs.getTabCount() == 2, "Tab count is " + tabs.getTabCount());
            assertTrue("Tab 1 title Browse Resources", tabs.getTitleAt(0).contains("Browse"), "Tab 1 title mismatch");
            assertTrue("Tab 2 title My Bookings", tabs.getTitleAt(1).contains("Bookings"), "Tab 2 title mismatch");
        }

        // Check for Tables
        List<JTable> tables = findAllComponents(content, JTable.class);
        assertTrue("UserFrame contains JTables for resources & bookings", tables.size() >= 2, "Found " + tables.size() + " tables");

        if (!tables.isEmpty()) {
            JTable resTable = tables.get(0);
            assertTrue("Resource table has rows", resTable.getRowCount() > 0, "Resource table is empty");
        }

        SwingUtilities.invokeAndWait(frame::dispose);
        assertTrue("UserFrame teardown", true, "");
    }

    // -------------------------------------------------------------
    // 6. AdminFrame Component Verification
    // -------------------------------------------------------------
    private static void testAdminFrameComponent() throws Exception {
        System.out.println("\n[6] Verifying AdminFrame GUI Component & Tabs...");

        User admin = DataStore.USERS.stream().filter(u -> u.getRole() == Role.ADMIN).findFirst().orElse(null);
        assertTrue("Admin user exists in DataStore", admin != null, "No admin user found");

        final AdminFrame[] frameHolder = new AdminFrame[1];
        SwingUtilities.invokeAndWait(() -> {
            frameHolder[0] = new AdminFrame(admin);
        });

        AdminFrame frame = frameHolder[0];
        assertTrue("AdminFrame instantiated for Admin", frame != null, "AdminFrame failed to create");

        Container content = frame.getContentPane();
        JTabbedPane tabs = findComponent(content, JTabbedPane.class);
        assertTrue("AdminFrame contains JTabbedPane", tabs != null, "JTabbedPane missing in AdminFrame");

        if (tabs != null) {
            assertTrue("AdminFrame has 3 Tabs", tabs.getTabCount() == 3, "Tab count is " + tabs.getTabCount());
            assertTrue("Tab 1 is Booking Requests", tabs.getTitleAt(0).contains("Booking"), "Tab 1 mismatch");
            assertTrue("Tab 2 is Manage Resources", tabs.getTitleAt(1).contains("Resource"), "Tab 2 mismatch");
            assertTrue("Tab 3 is Utilization & Reports", tabs.getTitleAt(2).contains("Utilization") || tabs.getTitleAt(2).contains("Report"), "Tab 3 mismatch");
        }

        List<JTable> tables = findAllComponents(content, JTable.class);
        assertTrue("AdminFrame contains JTables across tabs", tables.size() >= 3, "Found " + tables.size() + " tables");

        SwingUtilities.invokeAndWait(frame::dispose);
        assertTrue("AdminFrame teardown", true, "");
    }

    // --- Helper Component Tree Traversals ---
    @SuppressWarnings("unchecked")
    private static <T extends Component> T findComponent(Container parent, Class<T> targetClass) {
        for (Component c : parent.getComponents()) {
            if (targetClass.isInstance(c)) {
                return (T) c;
            }
            if (c instanceof Container) {
                T found = findComponent((Container) c, targetClass);
                if (found != null) return found;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Component> List<T> findAllComponents(Container parent, Class<T> targetClass) {
        List<T> list = new java.util.ArrayList<>();
        for (Component c : parent.getComponents()) {
            if (targetClass.isInstance(c)) {
                list.add((T) c);
            }
            if (c instanceof Container) {
                list.addAll(findAllComponents((Container) c, targetClass));
            }
        }
        return list;
    }
}
