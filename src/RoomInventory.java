/**
 * =========================================================
 * CLASS - RoomInventory
 * =========================================================
 *
 * Use Case 3: Centralized Room Inventory Management
 *
 * Description:
 * Manages room availability using a centralized HashMap.
 * This eliminates scattered availability variables.
 *
 * @version 3.1
 */

import java.util.HashMap;
import java.util.Map;

public class RoomInventory {

    private HashMap<String, Integer> inventory;

    /** Constructor initializes inventory */
    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("SingleRoom", 5);
        inventory.put("DoubleRoom", 3);
        inventory.put("SuiteRoom", 2);
    }

    /** Get availability for a room type */
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    /** Update availability */
    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    /** Display full inventory */
    public void displayInventory() {
        System.out.println("Current Room Inventory:");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Available: " + entry.getValue());
        }
    }
}