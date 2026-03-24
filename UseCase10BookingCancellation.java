```java
import java.util.*;

/*
=========================================================
Use Case 10: Booking Cancellation & Inventory Rollback
Book My Stay App
=========================================================
Demonstrates safe cancellation with inventory rollback
using Stack (LIFO) for released room IDs.
*/

public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Booking Cancellation\n");

        // inventory
        RoomInventory inventory = new RoomInventory();

        // booking history
        BookingHistory history = new BookingHistory();

        // cancellation service
        CancellationService cancellationService =
                new CancellationService(inventory, history);

        // simulate confirmed bookings
        Reservation r1 = new Reservation("SingleRoom-1", "Alice", "SingleRoom");
        Reservation r2 = new Reservation("DoubleRoom-1", "Bob", "DoubleRoom");

        history.addReservation(r1);
        history.addReservation(r2);

        inventory.decreaseAvailability("SingleRoom");
        inventory.decreaseAvailability("DoubleRoom");

        System.out.println("Initial Inventory:");
        inventory.displayInventory();

        System.out.println("\nCancelling reservation: SingleRoom-1\n");

        try {
            cancellationService.cancelReservation("SingleRoom-1");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\nUpdated Inventory:");
        inventory.displayInventory();
    }
}


/* =========================================================
Reservation Model
========================================================= */

class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;
    private boolean active;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.active = true;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isActive() {
        return active;
    }

    public void cancel() {
        active = false;
    }
}


/* =========================================================
Room Inventory
========================================================= */

class RoomInventory {

    private Map<String,Integer> inventory;

    public RoomInventory() {

        inventory = new HashMap<>();

        inventory.put("SingleRoom",5);
        inventory.put("DoubleRoom",3);
        inventory.put("SuiteRoom",2);
    }

    public void decreaseAvailability(String roomType) {
        inventory.put(roomType, inventory.get(roomType)-1);
    }

    public void increaseAvailability(String roomType) {
        inventory.put(roomType, inventory.get(roomType)+1);
    }

    public void displayInventory() {

        for(Map.Entry<String,Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey()+" Available: "+entry.getValue());
        }
    }
}


/* =========================================================
Booking History
========================================================= */

class BookingHistory {

    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation r) {
        reservations.add(r);
    }

    public Reservation findReservation(String reservationId) {

        for(Reservation r : reservations) {
            if(r.getReservationId().equals(reservationId))
                return r;
        }
        return null;
    }
}


/* =========================================================
Cancellation Service
========================================================= */

class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;

    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory,
                               BookingHistory history) {

        this.inventory = inventory;
        this.history = history;

        rollbackStack = new Stack<>();
    }

    public void cancelReservation(String reservationId) throws Exception {

        Reservation reservation = history.findReservation(reservationId);

        if(reservation == null)
            throw new Exception("Reservation does not exist.");

        if(!reservation.isActive())
            throw new Exception("Reservation already cancelled.");

        // record rollback
        rollbackStack.push(reservationId);

        // restore inventory
        inventory.increaseAvailability(reservation.getRoomType());

        // mark cancelled
        reservation.cancel();

        System.out.println("Reservation cancelled successfully.");
        System.out.println("Released Room ID: " + rollbackStack.peek());
    }
}
```
