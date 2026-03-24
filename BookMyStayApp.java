```java
import java.util.*;

/*
=========================================================
BookMyStayApp
Hotel Booking Management System (Use Case 1–7)
=========================================================
*/

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking Management System\n");

        // Room initialization
        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Display inventory
        inventory.displayInventory();
        System.out.println();

        // Search available rooms
        System.out.println("Available Rooms:\n");
        displayRoom(single, inventory.getAvailability("SingleRoom"));
        displayRoom(dbl, inventory.getAvailability("DoubleRoom"));
        displayRoom(suite, inventory.getAvailability("SuiteRoom"));

        // Booking request queue
        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Alice", "SingleRoom"));
        queue.addRequest(new Reservation("Bob", "DoubleRoom"));
        queue.addRequest(new Reservation("Charlie", "SuiteRoom"));
        queue.addRequest(new Reservation("David", "SingleRoom"));

        // Allocation service
        RoomAllocationService allocator =
                new RoomAllocationService(inventory, queue);

        allocator.processBookings();

        // Add-on services
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        String reservationId = "SingleRoom-1";

        serviceManager.addService(reservationId,
                new Service("Breakfast", 500));

        serviceManager.addService(reservationId,
                new Service("Airport Pickup", 1200));

        serviceManager.displayServices(reservationId);

        double cost = serviceManager.calculateServiceCost(reservationId);

        System.out.println("\nTotal Add-on Cost: ₹" + cost);
    }

    static void displayRoom(Room room, int available) {
        if (available > 0) {
            room.displayRoomDetails();
            System.out.println("Available: " + available + "\n");
        }
    }
}


/* =========================================================
ROOM DOMAIN MODEL
========================================================= */

abstract class Room {

    protected int beds;
    protected int size;
    protected double price;

    public Room(int beds, int size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public void displayRoomDetails() {
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqft");
        System.out.println("Price per night: " + price);
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super(1,250,1500);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2,400,2500);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3,750,5000);
    }
}


/* =========================================================
ROOM INVENTORY (HashMap)
========================================================= */

class RoomInventory {

    private HashMap<String,Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("SingleRoom",5);
        inventory.put("DoubleRoom",3);
        inventory.put("SuiteRoom",2);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type,0);
    }

    public void updateAvailability(String type,int count) {
        inventory.put(type,count);
    }

    public void displayInventory() {

        System.out.println("Room Inventory:");

        for(Map.Entry<String,Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " Available: " + e.getValue());
        }
    }
}


/* =========================================================
RESERVATION
========================================================= */

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName,String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}


/* =========================================================
BOOKING QUEUE (FIFO)
========================================================= */

class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation r) {
        queue.offer(r);
        System.out.println("Booking request added for " + r.getGuestName());
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}


/* =========================================================
ROOM ALLOCATION SERVICE
========================================================= */

class RoomAllocationService {

    private RoomInventory inventory;
    private BookingRequestQueue queue;

    private Map<String,Set<String>> allocatedRooms;

    public RoomAllocationService(RoomInventory inventory,
                                 BookingRequestQueue queue) {

        this.inventory = inventory;
        this.queue = queue;

        allocatedRooms = new HashMap<>();
    }

    public void processBookings() {

        System.out.println("\nProcessing Bookings...\n");

        while(!queue.isEmpty()) {

            Reservation r = queue.getNextRequest();
            String type = r.getRoomType();

            int available = inventory.getAvailability(type);

            if(available > 0) {

                String roomId = generateRoomId(type);

                allocatedRooms
                        .computeIfAbsent(type,k->new HashSet<>())
                        .add(roomId);

                inventory.updateAvailability(type,available-1);

                System.out.println("Reservation Confirmed");
                System.out.println("Guest: " + r.getGuestName());
                System.out.println("Room Type: " + type);
                System.out.println("Room ID: " + roomId + "\n");

            } else {

                System.out.println("No room available for "
                        + r.getGuestName());
            }
        }
    }

    private String generateRoomId(String type) {

        int count = allocatedRooms
                .getOrDefault(type,new HashSet<>())
                .size() + 1;

        return type + "-" + count;
    }
}


/* =========================================================
ADD-ON SERVICES
========================================================= */

class Service {

    private String name;
    private double price;

    public Service(String name,double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void display() {
        System.out.println(name + " - ₹" + price);
    }
}


class AddOnServiceManager {

    private Map<String,List<Service>> services;

    public AddOnServiceManager() {
        services = new HashMap<>();
    }

    public void addService(String reservationId, Service s) {

        services
                .computeIfAbsent(reservationId,
                        k->new ArrayList<>())
                .add(s);

        System.out.println(s.getName() +
                " added to reservation " + reservationId);
    }

    public void displayServices(String reservationId) {

        List<Service> list = services.get(reservationId);

        if(list == null) return;

        System.out.println("\nServices for Reservation "
                + reservationId);

        for(Service s : list)
            s.display();
    }

    public double calculateServiceCost(String reservationId) {

        List<Service> list = services.get(reservationId);

        if(list == null) return 0;

        double total = 0;

        for(Service s : list)
            total += s.getPrice();

        return total;
    }
}
```
