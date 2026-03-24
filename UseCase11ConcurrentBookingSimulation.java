```java
import java.util.*;

/*
=========================================================
Use Case 11: Concurrent Booking Simulation (Thread Safety)
Book My Stay App
=========================================================
Simulates multiple guests booking rooms at the same time.
Synchronization ensures safe inventory updates and
prevents double booking.
*/

public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation\n");

        RoomInventory inventory = new RoomInventory();

        BookingQueue queue = new BookingQueue();

        // add booking requests
        queue.addRequest(new Reservation("Alice","SingleRoom"));
        queue.addRequest(new Reservation("Bob","SingleRoom"));
        queue.addRequest(new Reservation("Charlie","DoubleRoom"));
        queue.addRequest(new Reservation("David","SuiteRoom"));
        queue.addRequest(new Reservation("Eva","SingleRoom"));

        // processor
        ConcurrentBookingProcessor processor =
                new ConcurrentBookingProcessor(queue,inventory);

        // simulate multiple threads
        Thread t1 = new Thread(processor);
        Thread t2 = new Thread(processor);
        Thread t3 = new Thread(processor);

        t1.start();
        t2.start();
        t3.start();
    }
}


/* =========================================================
Reservation Model
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
Shared Booking Queue
========================================================= */

class BookingQueue {

    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getRequest() {

        if(queue.isEmpty())
            return null;

        return queue.poll();
    }
}


/* =========================================================
Room Inventory (Shared Resource)
========================================================= */

class RoomInventory {

    private Map<String,Integer> inventory;

    public RoomInventory() {

        inventory = new HashMap<>();

        inventory.put("SingleRoom",2);
        inventory.put("DoubleRoom",1);
        inventory.put("SuiteRoom",1);
    }

    public synchronized boolean allocateRoom(String roomType,String guest) {

        int available = inventory.getOrDefault(roomType,0);

        if(available <= 0) {
            System.out.println("No room available for "+guest);
            return false;
        }

        inventory.put(roomType,available-1);

        System.out.println(
                Thread.currentThread().getName() +
                " allocated "+roomType+
                " to "+guest+
                " | Remaining: "+(available-1)
        );

        return true;
    }
}


/* =========================================================
Concurrent Booking Processor
========================================================= */

class ConcurrentBookingProcessor implements Runnable {

    private BookingQueue queue;
    private RoomInventory inventory;

    public ConcurrentBookingProcessor(BookingQueue queue,
                                      RoomInventory inventory) {

        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while(true) {

            Reservation r = queue.getRequest();

            if(r == null)
                break;

            inventory.allocateRoom(
                    r.getRoomType(),
                    r.getGuestName()
            );

            try {
                Thread.sleep(200);
            } catch(Exception e) {}
        }
    }
}
```
