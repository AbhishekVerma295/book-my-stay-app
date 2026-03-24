/**
 * =========================================================
 * MAIN CLASS - UseCase5BookingRequestQueue
 * =========================================================
 *
 * Use Case 5: Booking Request (First-Come-First-Served)
 *
 * Demonstrates request intake using Queue.
 *
 * @version 5.1
 */

public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        System.out.println("Hotel Booking Request System\n");

        BookingRequestQueue queue = new BookingRequestQueue();

        Reservation r1 = new Reservation("Alice", "SingleRoom");
        Reservation r2 = new Reservation("Bob", "DoubleRoom");
        Reservation r3 = new Reservation("Charlie", "SuiteRoom");

        queue.addRequest(r1);
        queue.addRequest(r2);
        queue.addRequest(r3);

        queue.displayRequests();
    }
}