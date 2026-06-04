# Grand Horizon Hotel Reservation System
## Java Console Application

### Project Structure
```
src/hotel/
├── Main.java                    ← Entry point
├── model/
│   ├── Room.java                ← Room entity (type, status, price)
│   ├── Customer.java            ← Customer entity
│   └── Reservation.java         ← Reservation entity (check-in/out, billing)
├── service/
│   ├── RoomService.java         ← Room CRUD + availability logic
│   ├── CustomerService.java     ← Customer registration & search
│   └── ReservationService.java  ← Booking, cancel, check-in/out, conflict detection
├── ui/
│   ├── UserMenu.java            ← Guest-facing interactive menu
│   └── AdminMenu.java           ← Admin panel (password: admin123)
├── report/
│   └── ReportGenerator.java     ← Occupancy, revenue, bookings, upcoming check-ins
└── util/
    └── Printer.java             ← Console formatting helpers
```

### How to Compile & Run
```bash
# 1. Compile (from project root)
mkdir -p out
find src -name "*.java" | xargs javac -d out

# 2. Run
java -cp out hotel.Main
```

### Key Features
| Feature                     | Details                                              |
|-----------------------------|------------------------------------------------------|
| View available rooms        | Filter by all or by type (SINGLE/DOUBLE/SUITE/DELUXE)|
| Book a room                 | Detects date conflicts, calculates total bill        |
| Cancel reservation          | Frees up the room automatically                      |
| Check-in / Check-out        | Status transitions + room release on checkout        |
| Customer management         | Register, update profile, search by name             |
| Admin panel                 | Password-protected (admin123)                        |
| Add / Remove rooms          | Admins can manage inventory                          |
| Maintenance mode            | Mark rooms as under maintenance                      |
| Reports                     | Occupancy %, revenue breakdown, upcoming check-ins   |

### Default Demo Rooms
| Room | Type   | Price/Night |
|------|--------|-------------|
| 101–103 | SINGLE | ₹1200–1500 |
| 201–203 | DOUBLE | ₹2200–2500 |
| 301–302 | DELUXE | ₹3800      |
| 401–402 | SUITE  | ₹6500–7000 |

### Admin Panel Password
```
admin123
```

### Date Format
All dates must be entered as: `dd-MM-yyyy`  
Example: `15-06-2025`
