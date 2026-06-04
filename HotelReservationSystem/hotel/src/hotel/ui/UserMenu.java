package hotel.ui;

import hotel.model.*;
import hotel.model.Room.RoomType;
import hotel.service.*;
import hotel.util.Printer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class UserMenu {
    private final RoomService roomService;
    private final CustomerService customerService;
    private final ReservationService reservationService;
    private final Scanner sc;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public UserMenu(RoomService rs, CustomerService cs, ReservationService res, Scanner sc) {
        this.roomService        = rs;
        this.customerService    = cs;
        this.reservationService = res;
        this.sc                 = sc;
    }

    public void show() {
        boolean running = true;
        while (running) {
            Printer.header("USER MENU");
            System.out.println("  1. View Available Rooms");
            System.out.println("  2. Search Rooms by Type");
            System.out.println("  3. Register as New Customer");
            System.out.println("  4. Book a Room");
            System.out.println("  5. View My Reservations");
            System.out.println("  6. Cancel a Reservation");
            System.out.println("  7. Update My Profile");
            System.out.println("  0. Back to Main Menu");
            Printer.line();
            System.out.print("  Choose: ");
            switch (sc.nextLine().trim()) {
                case "1" -> viewAvailableRooms();
                case "2" -> searchByType();
                case "3" -> registerCustomer();
                case "4" -> bookRoom();
                case "5" -> viewMyReservations();
                case "6" -> cancelReservation();
                case "7" -> updateProfile();
                case "0" -> running = false;
                default  -> Printer.error("Invalid option.");
            }
        }
    }

    private void viewAvailableRooms() {
        Printer.header("AVAILABLE ROOMS");
        List<Room> rooms = roomService.getAvailableRooms();
        if (rooms.isEmpty()) { Printer.info("No rooms currently available."); return; }
        rooms.forEach(r -> System.out.println("  " + r));
    }

    private void searchByType() {
        Printer.header("SEARCH BY ROOM TYPE");
        System.out.println("  Types: SINGLE, DOUBLE, SUITE, DELUXE");
        System.out.print("  Enter type: ");
        try {
            RoomType type = RoomType.valueOf(sc.nextLine().trim().toUpperCase());
            List<Room> rooms = roomService.getAvailableRoomsByType(type);
            if (rooms.isEmpty()) { Printer.info("No " + type + " rooms available."); return; }
            rooms.forEach(r -> System.out.println("  " + r));
        } catch (IllegalArgumentException e) { Printer.error("Invalid room type."); }
    }

    private Customer registerCustomer() {
        Printer.header("REGISTER CUSTOMER");
        System.out.print("  Full Name   : "); String name    = sc.nextLine().trim();
        System.out.print("  Phone       : "); String phone   = sc.nextLine().trim();
        System.out.print("  Email       : "); String email   = sc.nextLine().trim();
        System.out.print("  Address     : "); String address = sc.nextLine().trim();
        Customer c = customerService.registerCustomer(name, phone, email, address);
        Printer.success("Customer registered! ID: " + c.getCustomerId());
        return c;
    }

    private void bookRoom() {
        Printer.header("BOOK A ROOM");
        System.out.print("  Enter Customer ID (or press Enter to register): ");
        String cid = sc.nextLine().trim();
        Customer customer;
        if (cid.isEmpty()) {
            customer = registerCustomer();
        } else {
            customer = customerService.findById(cid).orElse(null);
            if (customer == null) { Printer.error("Customer not found."); return; }
        }

        viewAvailableRooms();
        System.out.print("  Enter Room Number: ");
        int roomNum;
        try { roomNum = Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { Printer.error("Invalid room number."); return; }

        System.out.print("  Check-in date  (dd-MM-yyyy): ");
        LocalDate checkIn  = parseDate(sc.nextLine().trim());
        System.out.print("  Check-out date (dd-MM-yyyy): ");
        LocalDate checkOut = parseDate(sc.nextLine().trim());
        if (checkIn == null || checkOut == null) { Printer.error("Invalid date format."); return; }

        System.out.print("  Special Requests (optional): ");
        String requests = sc.nextLine().trim();

        try {
            Reservation res = reservationService.bookRoom(customer, roomNum, checkIn, checkOut, requests);
            Printer.success("Booking confirmed!");
            System.out.println("  " + res);
            System.out.printf("  Total Amount: ₹%.2f%n", res.getTotalAmount());
        } catch (IllegalArgumentException e) { Printer.error(e.getMessage()); }
    }

    private void viewMyReservations() {
        Printer.header("MY RESERVATIONS");
        System.out.print("  Enter Customer ID: ");
        String cid = sc.nextLine().trim();
        List<Reservation> list = reservationService.getReservationsByCustomer(cid);
        if (list.isEmpty()) { Printer.info("No reservations found."); return; }
        list.forEach(r -> System.out.println("  " + r));
    }

    private void cancelReservation() {
        Printer.header("CANCEL RESERVATION");
        System.out.print("  Enter Reservation ID: ");
        String rid = sc.nextLine().trim();
        if (reservationService.cancelReservation(rid))
            Printer.success("Reservation " + rid + " cancelled successfully.");
        else
            Printer.error("Reservation not found or already cancelled.");
    }

    private void updateProfile() {
        Printer.header("UPDATE PROFILE");
        System.out.print("  Enter Customer ID: ");
        String cid = sc.nextLine().trim();
        if (customerService.findById(cid).isEmpty()) { Printer.error("Customer not found."); return; }
        System.out.println("  (Press Enter to keep existing value)");
        System.out.print("  New Name    : "); String name    = sc.nextLine().trim();
        System.out.print("  New Phone   : "); String phone   = sc.nextLine().trim();
        System.out.print("  New Email   : "); String email   = sc.nextLine().trim();
        System.out.print("  New Address : "); String address = sc.nextLine().trim();
        customerService.updateCustomer(cid, name, phone, email, address);
        Printer.success("Profile updated.");
    }

    private LocalDate parseDate(String s) {
        try { return LocalDate.parse(s, FMT); }
        catch (DateTimeParseException e) { return null; }
    }
}
