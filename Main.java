package hotel;

import hotel.service.*;
import hotel.ui.*;
import hotel.util.Printer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RoomService        roomService        = new RoomService();
        CustomerService    customerService    = new CustomerService();
        ReservationService reservationService = new ReservationService(roomService);
        Scanner            sc                 = new Scanner(System.in);

        System.out.println();
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║          GRAND HORIZON HOTEL RESERVATION SYSTEM           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");

        UserMenu  userMenu  = new UserMenu(roomService, customerService, reservationService, sc);
        AdminMenu adminMenu = new AdminMenu(roomService, customerService, reservationService, sc);

        boolean running = true;
        while (running) {
            Printer.header("MAIN MENU");
            System.out.println("  1. User Portal");
            System.out.println("  2. Admin Panel");
            System.out.println("  0. Exit");
            Printer.line();
            System.out.print("  Choose: ");
            switch (sc.nextLine().trim()) {
                case "1" -> userMenu.show();
                case "2" -> adminMenu.show();
                case "0" -> running = false;
                default  -> Printer.error("Invalid option. Try again.");
            }
        }

        System.out.println();
        System.out.println("  Thank you for using Grand Horizon Hotel System. Goodbye!");
        sc.close();
    }
}
