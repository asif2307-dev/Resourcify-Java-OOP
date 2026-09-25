package resourcify;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public final class DataStore {
    public static final List<User> USERS = new ArrayList<>();
    public static final List<Resource> RESOURCES = new ArrayList<>();
    public static final List<Booking> BOOKINGS = new ArrayList<>();

    static {
        USERS.add(new User("U001", "Demo Student", "student@resourcify.demo",
                "student123", Role.STUDENT));
        USERS.add(new User("U002", "Demo Faculty", "faculty@resourcify.demo",
                "faculty123", Role.FACULTY));
        USERS.add(new User("A001", "System Administrator", "admin@resourcify.demo",
                "admin123", Role.ADMIN));

        RESOURCES.add(new Resource("R001", "Computer Lab 01", "Computer Lab",
                "Block C - Room 201", 60, true));
        RESOURCES.add(new Resource("R002", "Seminar Hall A", "Seminar Hall",
                "Academic Block - Ground Floor", 120, true));
        RESOURCES.add(new Resource("R003", "Innovation Lab", "Laboratory",
                "Innovation Centre", 40, true));
        RESOURCES.add(new Resource("R004", "Meeting Room 01", "Meeting Room",
                "Admin Block - Room 102", 12, true));
        RESOURCES.add(new Resource("R005", "Classroom C-202", "Classroom",
                "Block C - Room 202", 50, true));
        RESOURCES.add(new Resource("R006", "Physics Lab 02", "Laboratory",
                "Science Block - Room 105", 35, true));

        // Sample bookings for immediate demonstration
        Booking b1 = new Booking("RSF-1001", USERS.get(0), RESOURCES.get(0),
                LocalDate.now().plusDays(1), LocalTime.of(10, 0),
                LocalTime.of(12, 0), "Data Science Lab Practice");
        b1.approve("Approved - Lab assistant assigned");
        BOOKINGS.add(b1);

        Booking b2 = new Booking("RSF-1002", USERS.get(1), RESOURCES.get(1),
                LocalDate.now().plusDays(2), LocalTime.of(14, 0),
                LocalTime.of(16, 0), "Guest Lecture on Cloud Computing");
        BOOKINGS.add(b2);

        Booking b3 = new Booking("RSF-1003", USERS.get(0), RESOURCES.get(2),
                LocalDate.now().plusDays(3), LocalTime.of(11, 0),
                LocalTime.of(13, 0), "Robotics Club Project Demo");
        BOOKINGS.add(b3);
    }

    private DataStore() {}
}
