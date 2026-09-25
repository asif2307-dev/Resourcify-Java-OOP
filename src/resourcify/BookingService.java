package resourcify;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingService {

    public boolean hasConflict(Resource resource, LocalDate date,
                                LocalTime start, LocalTime end) {
        for (Booking b : DataStore.BOOKINGS) {
            if (b.getResource().getId().equals(resource.getId())
                    && b.getDate().equals(date)
                    && b.getStatus() != BookingStatus.REJECTED
                    && b.getStatus() != BookingStatus.CANCELLED
                    && start.isBefore(b.getEnd())
                    && end.isAfter(b.getStart())) {
                return true;
            }
        }
        return false;
    }

    public Booking createBooking(User user, Resource resource, LocalDate date,
                                 LocalTime start, LocalTime end, String purpose) {
        if (!resource.isAvailable()) {
            throw new IllegalArgumentException("Resource is currently unavailable or under maintenance.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Booking date is required.");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot create booking for past dates.");
        }
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start time and end time are required.");
        }
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("End time must be after start time.");
        }
        if (purpose == null || purpose.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking purpose is required.");
        }
        if (hasConflict(resource, date, start, end)) {
            throw new IllegalArgumentException("Time conflict: Resource is already booked for this slot.");
        }

        String id = String.format("RSF-%04d", 1000 + DataStore.BOOKINGS.size() + 1);
        Booking booking = new Booking(id, user, resource, date, start, end, purpose.trim());
        DataStore.BOOKINGS.add(booking);
        return booking;
    }

    public void cancelBooking(Booking booking, User requester) {
        if (booking == null) {
            throw new IllegalArgumentException("Please select a booking to cancel.");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("This booking is already cancelled.");
        }
        if (booking.getStatus() == BookingStatus.REJECTED) {
            throw new IllegalArgumentException("Cannot cancel a rejected booking.");
        }
        if (requester.getRole() != Role.ADMIN && !booking.getUser().getId().equals(requester.getId())) {
            throw new IllegalArgumentException("You can only cancel your own bookings.");
        }
        booking.cancel();
    }

    public void approveBooking(Booking booking, String remark) {
        if (booking == null) {
            throw new IllegalArgumentException("Please select a booking to approve.");
        }
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalArgumentException("Only pending bookings can be approved.");
        }
        booking.approve(remark == null || remark.trim().isEmpty() ? "Approved by Administrator" : remark.trim());
    }

    public void rejectBooking(Booking booking, String remark) {
        if (booking == null) {
            throw new IllegalArgumentException("Please select a booking to reject.");
        }
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalArgumentException("Only pending bookings can be rejected.");
        }
        booking.reject(remark == null || remark.trim().isEmpty() ? "Rejected by Administrator" : remark.trim());
    }

    public void addResource(String id, String name, String type, String location, int capacity) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Resource ID is required.");
        }
        for (Resource r : DataStore.RESOURCES) {
            if (r.getId().equalsIgnoreCase(id.trim())) {
                throw new IllegalArgumentException("A resource with ID '" + id.trim() + "' already exists.");
            }
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Resource name is required.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Resource location is required.");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be a positive number.");
        }
        DataStore.RESOURCES.add(new Resource(id.trim(), name.trim(), type.trim(), location.trim(), capacity, true));
    }

    public void deleteResource(Resource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("Please select a resource to delete.");
        }
        boolean hasActive = DataStore.BOOKINGS.stream()
                .anyMatch(b -> b.getResource().getId().equals(resource.getId())
                        && (b.getStatus() == BookingStatus.PENDING || b.getStatus() == BookingStatus.APPROVED));
        if (hasActive) {
            throw new IllegalArgumentException("Cannot delete resource with active or pending bookings.");
        }
        DataStore.RESOURCES.remove(resource);
    }
}
