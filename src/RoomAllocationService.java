/**
 * =========================================================
 * CLASS - RoomAllocationService
 * =========================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Allocates rooms safely while preventing duplicate room IDs.
 *
 * @version 6.1
 */

import java.util.*;

public class RoomAllocationService {

    private RoomInventory inventory;
    private BookingRequestQueue queue;

    private Map<String, Set<String>> allocatedRooms;

    public RoomAllocationService(RoomInventory inventory, BookingRequestQueue queue) {
        this.inventory = inventory;
        this.queue = queue;
        allocatedRooms = new HashMap<>();
    }

    /** Process booking requests */
    public void processBookings() {

        while (!queue.isEmpty()) {

            Reservation reservation = queue.getNextRequest();
            String roomType = reservation.getRoomType();

            int available = inventory.getAvailability(roomType);

            if (available > 0) {

                String roomId = generateRoomId(roomType);

                allocatedRooms
                        .computeIfAbsent(roomType, k -> new HashSet<>())
                        .add(roomId);

                inventory.updateAvailability(roomType, available - 1);

                System.out.println("Reservation Confirmed");
                System.out.println("Guest: " + reservation.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Room ID: " + roomId);
                System.out.println();

            } else {

                System.out.println("Reservation Failed for "
                        + reservation.getGuestName()
                        + " - No rooms available");
            }
        }
    }

    /** Generate unique room ID */
    private String generateRoomId(String roomType) {

        int count = allocatedRooms
                .getOrDefault(roomType, new HashSet<>())
                .size() + 1;

        return roomType + "-" + count;
    }
}