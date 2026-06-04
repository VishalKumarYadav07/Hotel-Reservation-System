package hotel.report;

import hotel.model.Reservation;
import hotel.model.Reservation.ReservationStatus;
import hotel.model.Room;
import hotel.service.CustomerService;
import hotel.service.ReservationService;
import hotel.service.RoomService;
import hotel.util.Printer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportGenerator {
    private final RoomService roomService;
    private final ReservationService reservationService;
    private final CustomerService customerService;

    public ReportGenerator(RoomService rs, ReservationService res, CustomerService cs) {
        this.roomService        = rs;
        this.reservationService = res;
        this.customerService    = cs;
    }

    public void occupancyReport() {
        Printer.header("OCCUPANCY REPORT");
        int total     = roomService.getTotalRooms();
        long booked   = roomService.getBookedCount();
        long available= roomService.getAvailableCount();
        double pct    = total == 0 ? 0 : (booked * 100.0 / total);
        System.out.printf("  Total Rooms   : %d%n", total);
        System.out.printf("  Booked        : %d%n", booked);
        System.out.printf("  Available     : %d%n", available);
        System.out.printf("  Occupancy Rate: %.1f%%%n%n", pct);

        System.out.println("  By Room Type:");
        Map<Room.RoomType, Long> byType = roomService.getAllRooms().stream()
                .filter(r -> r.getStatus() == Room.RoomStatus.BOOKED)
                .collect(Collectors.groupingBy(Room::getType, Collectors.counting()));
        for (Room.RoomType t : Room.RoomType.values()) {
            System.out.printf("    %-8s : %d booked%n", t, byType.getOrDefault(t, 0L));
        }
    }

    public void revenueReport() {
        Printer.header("REVENUE REPORT");
        List<Reservation> all = reservationService.getAllReservations();
        double total     = reservationService.getTotalRevenue();
        double confirmed = all.stream().filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)
                              .mapToDouble(Reservation::getTotalAmount).sum();
        double checkedIn = all.stream().filter(r -> r.getStatus() == ReservationStatus.CHECKED_IN)
                              .mapToDouble(Reservation::getTotalAmount).sum();
        double completed = all.stream().filter(r -> r.getStatus() == ReservationStatus.CHECKED_OUT)
                              .mapToDouble(Reservation::getTotalAmount).sum();
        System.out.printf("  Total Reservations  : %d%n", all.size());
        System.out.printf("  Active (Confirmed)  : ₹%10.2f%n", confirmed);
        System.out.printf("  Currently Checked-In: ₹%10.2f%n", checkedIn);
        System.out.printf("  Completed Stay      : ₹%10.2f%n", completed);
        System.out.printf("  ─────────────────────────────%n");
        System.out.printf("  TOTAL REVENUE       : ₹%10.2f%n", total);
    }

    public void bookingsReport() {
        Printer.header("ALL BOOKINGS REPORT");
        List<Reservation> res = reservationService.getAllReservations();
        if (res.isEmpty()) { System.out.println("  No reservations found."); return; }
        Printer.line();
        res.forEach(r -> System.out.println("  " + r));
        Printer.line();
    }

    public void upcomingCheckInsReport() {
        Printer.header("UPCOMING CHECK-INS (Next 7 Days)");
        LocalDate today = LocalDate.now();
        List<Reservation> upcoming = reservationService.getActiveReservations().stream()
                .filter(r -> !r.getCheckIn().isBefore(today)
                          && r.getCheckIn().isBefore(today.plusDays(7)))
                .sorted((a, b) -> a.getCheckIn().compareTo(b.getCheckIn()))
                .collect(Collectors.toList());
        if (upcoming.isEmpty()) { System.out.println("  No upcoming check-ins."); return; }
        upcoming.forEach(r -> System.out.printf("  %s | Room %d | %s | Check-in: %s%n",
                r.getReservationId(), r.getRoom().getRoomNumber(),
                r.getCustomer().getName(), r.getCheckIn()));
    }

    public void customerReport() {
        Printer.header("CUSTOMER SUMMARY");
        System.out.printf("  Total Registered Customers: %d%n%n", customerService.getTotalCustomers());
        Printer.line();
        customerService.getAllCustomers().forEach(c -> System.out.println("  " + c));
        Printer.line();
    }
}
