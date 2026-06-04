package hotel.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {
    public enum ReservationStatus { CONFIRMED, CANCELLED, CHECKED_IN, CHECKED_OUT }

    private static int counter = 1000;

    private final String reservationId;
    private final Customer customer;
    private final Room room;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private ReservationStatus status;
    private final LocalDate bookingDate;
    private String specialRequests;

    public Reservation(Customer customer, Room room, LocalDate checkIn, LocalDate checkOut, String specialRequests) {
        this.reservationId   = "RES-" + (++counter);
        this.customer        = customer;
        this.room            = room;
        this.checkIn         = checkIn;
        this.checkOut        = checkOut;
        this.status          = ReservationStatus.CONFIRMED;
        this.bookingDate     = LocalDate.now();
        this.specialRequests = specialRequests;
    }

    public long getNights()          { return ChronoUnit.DAYS.between(checkIn, checkOut); }
    public double getTotalAmount()   { return getNights() * room.getPricePerNight(); }

    public String getReservationId()          { return reservationId; }
    public Customer getCustomer()             { return customer; }
    public Room getRoom()                     { return room; }
    public LocalDate getCheckIn()             { return checkIn; }
    public LocalDate getCheckOut()            { return checkOut; }
    public ReservationStatus getStatus()      { return status; }
    public LocalDate getBookingDate()         { return bookingDate; }
    public String getSpecialRequests()        { return specialRequests; }
    public void setStatus(ReservationStatus s){ this.status = s; }

    @Override
    public String toString() {
        return String.format(
            "%s | Room %-3d | %-18s | %s → %s | %d nights | ₹%.2f | %s",
            reservationId, room.getRoomNumber(), customer.getName(),
            checkIn, checkOut, getNights(), getTotalAmount(), status);
    }
}
