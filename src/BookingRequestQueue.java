/**
 * =========================================================
 * CLASS - BookingRequestQueue
 * =========================================================
 *
 * Manages booking requests using a FIFO Queue.
 *
 * @version 5.1
 */

import java.util.LinkedList;
import java.util.Queue;

public class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    /** Add booking request */
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }
    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean isEmpty() {
        return requestQueue.isEmpty();
    }

    /** Display queued requests */
    public void displayRequests() {

        System.out.println("\nCurrent Booking Requests:");

        for (Reservation r : requestQueue) {
            r.displayReservation();
        }
    }
}