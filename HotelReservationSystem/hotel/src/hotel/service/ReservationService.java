package hotel.service;

import hotel.model.Customer;
import hotel.model.Reservation;
import hotel.model.Reservation.ReservationStatus;
import hotel.model.Room;
import hotel.model.Room.RoomStatus;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReservationService {
    private final Map<String, Reservation> reservations = new LinkedHashMap<>();
    private final RoomService roomService;

    public ReservationService(RoomService roomService) {
        this.roomService = roomService;
    }

    public Reservation bookRoom(Customer customer, int roomNumber,
                                LocalDate checkIn, LocalDate checkOut,
                                String specialRequests) throws IllegalArgumentException {
        if (!checkOut.isAfter(checkIn))
            throw new IllegalArgumentException("Check-out must be after check-in.");

        Room room = roomService.getRoom(roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("Room " + roomNumber + " not found."));

        if (!room.isAvailable())
            throw new IllegalArgumentException("Room " + roomNumber + " is not available.");

        // Conflict check among confirmed reservations
        boolean conflict = reservations.values().stream()
                .filter(r -> r.getRoom().getRoomNumber() == roomNumber)
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED
                          || r.getStatus() == ReservationStatus.CHECKED_IN)
                .anyMatch(r -> checkIn.isBefore(r.getCheckOut()) && checkOut.isAfter(r.getCheckIn()));

        if (conflict)
            throw new IllegalArgumentException("Room " + roomNumber + " is already reserved for those dates.");

        Reservation res = new Reservation(customer, room, checkIn, checkOut, specialRequests);
        reservations.put(res.getReservationId(), res);
        room.setStatus(RoomStatus.BOOKED);
        return res;
    }

    public boolean cancelReservation(String reservationId) {
        Reservation res = reservations.get(reservationId);
        if (res == null || res.getStatus() == ReservationStatus.CANCELLED) return false;
        res.setStatus(ReservationStatus.CANCELLED);
        res.getRoom().setStatus(RoomStatus.AVAILABLE);
        return true;
    }

    public boolean checkIn(String reservationId) {
        Reservation res = reservations.get(reservationId);
        if (res == null || res.getStatus() != ReservationStatus.CONFIRMED) return false;
        res.setStatus(ReservationStatus.CHECKED_IN);
        return true;
    }

    public boolean checkOut(String reservationId) {
        Reservation res = reservations.get(reservationId);
        if (res == null || res.getStatus() != ReservationStatus.CHECKED_IN) return false;
        res.setStatus(ReservationStatus.CHECKED_OUT);
        res.getRoom().setStatus(RoomStatus.AVAILABLE);
        return true;
    }

    public Optional<Reservation> findById(String id) {
        return Optional.ofNullable(reservations.get(id));
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations.values());
    }

    public List<Reservation> getReservationsByCustomer(String customerId) {
        return reservations.values().stream()
                .filter(r -> r.getCustomer().getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    public List<Reservation> getActiveReservations() {
        return reservations.values().stream()
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED
                          || r.getStatus() == ReservationStatus.CHECKED_IN)
                .collect(Collectors.toList());
    }

    public double getTotalRevenue() {
        return reservations.values().stream()
                .filter(r -> r.getStatus() != ReservationStatus.CANCELLED)
                .mapToDouble(Reservation::getTotalAmount).sum();
    }
}
