/**
 * =========================================================
 * CLASS - RoomSearchService
 * =========================================================
 *
 * Use Case 4: Room Search & Availability Check
 *
 * Provides read-only access to room availability.
 * Displays only rooms that currently have availability.
 *
 * @version 4.1
 */

public class RoomSearchService {

    private RoomInventory inventory;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    /** Displays available rooms */
    public void searchAvailableRooms() {

        System.out.println("Available Rooms:\n");

        checkRoom("SingleRoom", new SingleRoom());
        checkRoom("DoubleRoom", new DoubleRoom());
        checkRoom("SuiteRoom", new SuiteRoom());
    }

    /** Helper method to validate and display rooms */
    private void checkRoom(String roomType, Room room) {

        int available = inventory.getAvailability(roomType);

        if (available > 0) {

            System.out.println(roomType + ":");
            room.displayRoomDetails();
            System.out.println("Available: " + available);
            System.out.println();
        }
    }
}