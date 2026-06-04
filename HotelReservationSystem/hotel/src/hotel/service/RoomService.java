package hotel.service;

import hotel.model.Room;
import hotel.model.Room.RoomType;
import hotel.model.Room.RoomStatus;

import java.util.*;
import java.util.stream.Collectors;

public class RoomService {
    private final Map<Integer, Room> rooms = new LinkedHashMap<>();

    public RoomService() {
        // Pre-load demo rooms
        addRoom(new Room(101, RoomType.SINGLE,  1200));
        addRoom(new Room(102, RoomType.SINGLE,  1200));
        addRoom(new Room(103, RoomType.SINGLE,  1500));
        addRoom(new Room(201, RoomType.DOUBLE,  2200));
        addRoom(new Room(202, RoomType.DOUBLE,  2200));
        addRoom(new Room(203, RoomType.DOUBLE,  2500));
        addRoom(new Room(301, RoomType.DELUXE,  3800));
        addRoom(new Room(302, RoomType.DELUXE,  3800));
        addRoom(new Room(401, RoomType.SUITE,   6500));
        addRoom(new Room(402, RoomType.SUITE,   7000));
    }

    public boolean addRoom(Room room) {
        if (rooms.containsKey(room.getRoomNumber())) return false;
        rooms.put(room.getRoomNumber(), room);
        return true;
    }

    public boolean removeRoom(int roomNumber) {
        Room r = rooms.get(roomNumber);
        if (r == null || !r.isAvailable()) return false;
        rooms.remove(roomNumber);
        return true;
    }

    public Optional<Room> getRoom(int roomNumber) {
        return Optional.ofNullable(rooms.get(roomNumber));
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    public List<Room> getAvailableRooms() {
        return rooms.values().stream()
                .filter(Room::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Room> getAvailableRoomsByType(RoomType type) {
        return rooms.values().stream()
                .filter(r -> r.isAvailable() && r.getType() == type)
                .collect(Collectors.toList());
    }

    public void setRoomStatus(int roomNumber, RoomStatus status) {
        getRoom(roomNumber).ifPresent(r -> r.setStatus(status));
    }

    public int getTotalRooms()     { return rooms.size(); }
    public long getAvailableCount(){ return rooms.values().stream().filter(Room::isAvailable).count(); }
    public long getBookedCount()   { return rooms.values().stream().filter(r -> r.getStatus() == RoomStatus.BOOKED).count(); }
}
