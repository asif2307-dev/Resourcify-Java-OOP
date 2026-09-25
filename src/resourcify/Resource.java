package resourcify;

public class Resource {
    private final String id;
    private String name;
    private String type;
    private String location;
    private int capacity;
    private boolean available;

    public Resource(String id, String name, String type, String location,
                    int capacity, boolean available) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.capacity = capacity;
        this.available = available;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public boolean isAvailable() { return available; }

    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setLocation(String location) { this.location = location; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return name + " | " + type + " | " + location + " | Capacity: " + capacity;
    }
}
