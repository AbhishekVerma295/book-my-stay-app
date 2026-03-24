```java
import java.util.*;

/*
=========================================================
Use Case 9: Error Handling & Validation
Book My Stay App
=========================================================
Demonstrates input validation, custom exceptions,
fail-fast design, and graceful error handling.
*/

public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        System.out.println("Book My Stay - Validation Demo\n");

        // Inventory with valid room types
        RoomInventory inventory = new RoomInventory();

        // Validator
        InvalidBookingValidator validator = new InvalidBookingValidator(inventory);

        // Example booking inputs
        BookingInput validBooking = new BookingInput("Alice", "SingleRoom");
        BookingInput invalidRoomType = new BookingInput("Bob", "Penthouse");
        BookingInput invalidInventory = new BookingInput("Charlie", "SuiteRoom");

        try {
            validator.validateBooking(validBooking);
            System.out.println("Booking validated successfully for " + validBooking.getGuestName());
        } catch (InvalidBookingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println();

        try {
            validator.validateBooking(invalidRoomType);
        } catch (InvalidBookingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println();

        // simulate inventory reaching zero
        inventory.updateAvailability("SuiteRoom", 0);

        try {
            validator.validateBooking(invalidInventory);
        } catch (InvalidBookingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\nSystem continues running safely.");
    }
}


/* =========================================================
Booking Input Model
========================================================= */

class BookingInput {

    private String guestName;
    private String roomType;

    public BookingInput(String guestName, String roomType) {
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
Room Inventory
========================================================= */

class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {

        inventory = new HashMap<>();

        inventory.put("SingleRoom", 5);
        inventory.put("DoubleRoom", 3);
        inventory.put("SuiteRoom", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1);
    }

    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }
}


/* =========================================================
Custom Exception
========================================================= */

class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}


/* =========================================================
Booking Validator
========================================================= */

class InvalidBookingValidator {

    private RoomInventory inventory;

    public InvalidBookingValidator(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void validateBooking(BookingInput booking) throws InvalidBookingException {

        String roomType = booking.getRoomType();

        // Validate room type
        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidBookingException(
                    "Invalid room type selected: " + roomType);
        }

        // Validate availability
        int available = inventory.getAvailability(roomType);

        if (available <= 0) {
            throw new InvalidBookingException(
                    "No rooms available for type: " + roomType);
        }
    }
}
```
