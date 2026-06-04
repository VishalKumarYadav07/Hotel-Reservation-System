package hotel.model;

public class Room {
    public enum RoomType { SINGLE, DOUBLE, SUITE, DELUXE }
    public enum RoomStatus { AVAILABLE, BOOKED, MAINTENANCE }

    private final int roomNumber;
    private final RoomType type;
    private final double pricePerNight;
    private RoomStatus status;

    public Room(int roomNumber, RoomType type, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.status = RoomStatus.AVAILABLE;
    }

    public int getRoomNumber()         { return roomNumber; }
    public RoomType getType()          { return type; }
    public double getPricePerNight()   { return pricePerNight; }
    public RoomStatus getStatus()      { return status; }
    public void setStatus(RoomStatus s){ this.status = s; }
    public boolean isAvailable()       { return status == RoomStatus.AVAILABLE; }

    @Override
    public String toString() {
        return String.format("Room %-4d | %-8s | ₹%8.2f/night | %s",
                roomNumber, type, pricePerNight, status);
    }
}
