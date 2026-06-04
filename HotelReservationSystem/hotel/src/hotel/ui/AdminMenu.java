package hotel.ui;

import hotel.model.Room;
import hotel.model.Room.RoomStatus;
import hotel.model.Room.RoomType;
import hotel.report.ReportGenerator;
import hotel.service.*;
import hotel.util.Printer;

import java.util.Scanner;

public class AdminMenu {
    private final RoomService roomService;
    private final CustomerService customerService;
    private final ReservationService reservationService;
    private final ReportGenerator reportGenerator;
    private final Scanner sc;
    private static final String ADMIN_PASSWORD = "admin123";

    public AdminMenu(RoomService rs, CustomerService cs, ReservationService res, Scanner sc) {
        this.roomService        = rs;
        this.customerService    = cs;
        this.reservationService = res;
        this.reportGenerator    = new ReportGenerator(rs, res, cs);
        this.sc                 = sc;
    }

    public void show() {
        Printer.header("ADMIN LOGIN");
        System.out.print("  Password: ");
        if (!ADMIN_PASSWORD.equals(sc.nextLine().trim())) {
            Printer.error("Incorrect password.");
            return;
        }
        Printer.success("Admin access granted.");

        boolean running = true;
        while (running) {
            Printer.header("ADMIN PANEL");
            System.out.println("  ── Room Management ──");
            System.out.println("  1.  View All Rooms");
            System.out.println("  2.  Add New Room");
            System.out.println("  3.  Remove Room");
            System.out.println("  4.  Set Room to Maintenance");
            System.out.println("  ── Reservation Management ──");
            System.out.println("  5.  View All Reservations");
            System.out.println("  6.  View Active Reservations");
            System.out.println("  7.  Check-In Guest");
            System.out.println("  8.  Check-Out Guest");
            System.out.println("  ── Customer Management ──");
            System.out.println("  9.  View All Customers");
            System.out.println("  10. Search Customer by Name");
            System.out.println("  11. Remove Customer");
            System.out.println("  ── Reports ──");
            System.out.println("  12. Occupancy Report");
            System.out.println("  13. Revenue Report");
            System.out.println("  14. All Bookings Report");
            System.out.println("  15. Upcoming Check-Ins");
            System.out.println("  0.  Back to Main Menu");
            Printer.line();
            System.out.print("  Choose: ");
            switch (sc.nextLine().trim()) {
                case "1"  -> viewAllRooms();
                case "2"  -> addRoom();
                case "3"  -> removeRoom();
                case "4"  -> setMaintenance();
                case "5"  -> viewAllReservations();
                case "6"  -> viewActiveReservations();
                case "7"  -> checkIn();
                case "8"  -> checkOut();
                case "9"  -> viewAllCustomers();
                case "10" -> searchCustomer();
                case "11" -> removeCustomer();
                case "12" -> reportGenerator.occupancyReport();
                case "13" -> reportGenerator.revenueReport();
                case "14" -> reportGenerator.bookingsReport();
                case "15" -> reportGenerator.upcomingCheckInsReport();
                case "0"  -> running = false;
                default   -> Printer.error("Invalid option.");
            }
        }
    }

    private void viewAllRooms() {
        Printer.header("ALL ROOMS");
        roomService.getAllRooms().forEach(r -> System.out.println("  " + r));
    }

    private void addRoom() {
        Printer.header("ADD ROOM");
        System.out.print("  Room Number   : ");
        int num;
        try { num = Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { Printer.error("Invalid number."); return; }

        System.out.println("  Types: SINGLE, DOUBLE, SUITE, DELUXE");
        System.out.print("  Room Type     : ");
        RoomType type;
        try { type = RoomType.valueOf(sc.nextLine().trim().toUpperCase()); }
        catch (IllegalArgumentException e) { Printer.error("Invalid type."); return; }

        System.out.print("  Price/Night(₹): ");
        double price;
        try { price = Double.parseDouble(sc.nextLine().trim()); }
        catch (NumberFormatException e) { Printer.error("Invalid price."); return; }

        if (roomService.addRoom(new Room(num, type, price)))
            Printer.success("Room " + num + " added successfully.");
        else
            Printer.error("Room " + num + " already exists.");
    }

    private void removeRoom() {
        Printer.header("REMOVE ROOM");
        System.out.print("  Room Number: ");
        try {
            int num = Integer.parseInt(sc.nextLine().trim());
            if (roomService.removeRoom(num)) Printer.success("Room " + num + " removed.");
            else Printer.error("Room not found or currently booked.");
        } catch (NumberFormatException e) { Printer.error("Invalid number."); }
    }

    private void setMaintenance() {
        Printer.header("SET ROOM TO MAINTENANCE");
        System.out.print("  Room Number: ");
        try {
            int num = Integer.parseInt(sc.nextLine().trim());
            roomService.getRoom(num).ifPresentOrElse(r -> {
                r.setStatus(RoomStatus.MAINTENANCE);
                Printer.success("Room " + num + " set to MAINTENANCE.");
            }, () -> Printer.error("Room not found."));
        } catch (NumberFormatException e) { Printer.error("Invalid number."); }
    }

    private void viewAllReservations() {
        Printer.header("ALL RESERVATIONS");
        var list = reservationService.getAllReservations();
        if (list.isEmpty()) { Printer.info("No reservations."); return; }
        Printer.line();
        list.forEach(r -> System.out.println("  " + r));
        Printer.line();
    }

    private void viewActiveReservations() {
        Printer.header("ACTIVE RESERVATIONS");
        var list = reservationService.getActiveReservations();
        if (list.isEmpty()) { Printer.info("No active reservations."); return; }
        list.forEach(r -> System.out.println("  " + r));
    }

    private void checkIn() {
        Printer.header("CHECK-IN GUEST");
        System.out.print("  Reservation ID: ");
        String rid = sc.nextLine().trim();
        if (reservationService.checkIn(rid)) Printer.success("Guest checked in for " + rid);
        else Printer.error("Reservation not found or not in CONFIRMED status.");
    }

    private void checkOut() {
        Printer.header("CHECK-OUT GUEST");
        System.out.print("  Reservation ID: ");
        String rid = sc.nextLine().trim();
        reservationService.findById(rid).ifPresentOrElse(res -> {
            if (reservationService.checkOut(rid)) {
                Printer.success("Guest checked out. Total Bill: ₹" + res.getTotalAmount());
            } else {
                Printer.error("Reservation not in CHECKED_IN status.");
            }
        }, () -> Printer.error("Reservation not found."));
    }

    private void viewAllCustomers() {
        Printer.header("ALL CUSTOMERS");
        var list = customerService.getAllCustomers();
        if (list.isEmpty()) { Printer.info("No customers registered."); return; }
        Printer.line();
        list.forEach(c -> System.out.println("  " + c));
        Printer.line();
    }

    private void searchCustomer() {
        Printer.header("SEARCH CUSTOMER");
        System.out.print("  Enter Name: ");
        String name = sc.nextLine().trim();
        var list = customerService.findByName(name);
        if (list.isEmpty()) { Printer.info("No customers found."); return; }
        list.forEach(c -> System.out.println("  " + c));
    }

    private void removeCustomer() {
        Printer.header("REMOVE CUSTOMER");
        System.out.print("  Customer ID: ");
        String cid = sc.nextLine().trim();
        if (customerService.removeCustomer(cid)) Printer.success("Customer removed.");
        else Printer.error("Customer not found.");
    }
}
