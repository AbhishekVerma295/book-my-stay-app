```java
import java.io.*;
import java.util.*;

/*
=========================================================
Use Case 12: Data Persistence & System Recovery
Book My Stay App
=========================================================
Demonstrates saving system state (inventory + bookings)
to a file and restoring it after application restart.
*/

public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Persistence Demo\n");

        String fileName = "booking_system_state.dat";

        PersistenceService persistence = new PersistenceService();

        // Attempt recovery
        SystemState state = persistence.loadState(fileName);

        if(state == null) {

            System.out.println("No previous state found. Starting fresh.\n");

            state = new SystemState();

            state.inventory.put("SingleRoom",5);
            state.inventory.put("DoubleRoom",3);
            state.inventory.put("SuiteRoom",2);

        } else {

            System.out.println("System state restored successfully!\n");
        }

        // Simulate booking
        Reservation r1 = new Reservation("SingleRoom-1","Alice","SingleRoom");
        state.bookings.add(r1);
        state.inventory.put("SingleRoom",
                state.inventory.get("SingleRoom") - 1);

        // Display current state
        System.out.println("Current Inventory:");
        for(Map.Entry<String,Integer> entry : state.inventory.entrySet()) {

            System.out.println(entry.getKey() +
                    " Available: " + entry.getValue());
        }

        System.out.println("\nBookings:");
        for(Reservation r : state.bookings) {
            System.out.println(
                    r.getReservationId() +
                    " | Guest: " + r.getGuestName() +
                    " | Room: " + r.getRoomType()
            );
        }

        // Save system state before shutdown
        persistence.saveState(state,fileName);

        System.out.println("\nSystem state saved successfully.");
    }
}


/* =========================================================
Reservation Model
========================================================= */

class Reservation implements Serializable {

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId,
                       String guestName,
                       String roomType) {

        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}


/* =========================================================
System State (Inventory + Bookings)
========================================================= */

class SystemState implements Serializable {

    Map<String,Integer> inventory;
    List<Reservation> bookings;

    public SystemState() {

        inventory = new HashMap<>();
        bookings = new ArrayList<>();
    }
}


/* =========================================================
Persistence Service
========================================================= */

class PersistenceService {

    public void saveState(SystemState state,String fileName) {

        try {

            ObjectOutputStream out =
                    new ObjectOutputStream(
                            new FileOutputStream(fileName));

            out.writeObject(state);
            out.close();

        } catch(Exception e) {

            System.out.println("Error saving state: "
                    + e.getMessage());
        }
    }

    public SystemState loadState(String fileName) {

        try {

            ObjectInputStream in =
                    new ObjectInputStream(
                            new FileInputStream(fileName));

            SystemState state =
                    (SystemState) in.readObject();

            in.close();

            return state;

        } catch(Exception e) {

            return null;
        }
    }
}
```
