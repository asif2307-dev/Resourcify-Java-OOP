package resourcify;

import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {
    private final String id;
    private final User user;
    private final Resource resource;
    private final LocalDate date;
    private final LocalTime start;
    private final LocalTime end;
    private final String purpose;
    private BookingStatus status;
    private String adminRemark;

    public Booking(String id, User user, Resource resource, LocalDate date,
                   LocalTime start, LocalTime end, String purpose) {
        this.id = id;
        this.user = user;
        this.resource = resource;
        this.date = date;
        this.start = start;
        this.end = end;
        this.purpose = purpose;
        this.status = BookingStatus.PENDING;
    }

    public String getId() { return id; }
    public User getUser() { return user; }
    public Resource getResource() { return resource; }
    public LocalDate getDate() { return date; }
    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }
    public String getPurpose() { return purpose; }
    public BookingStatus getStatus() { return status; }
    public String getAdminRemark() { return adminRemark; }

    public void approve(String remark) {
        status = BookingStatus.APPROVED;
        adminRemark = remark;
    }

    public void reject(String remark) {
        status = BookingStatus.REJECTED;
        adminRemark = remark;
    }

    public void cancel() {
        status = BookingStatus.CANCELLED;
    }

    @Override
    public String toString() {
        return id + " | " + resource.getName() + " | " + date + " " +
               start + "-" + end + " | " + status;
    }
}
