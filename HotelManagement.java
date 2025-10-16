// HotelManagement.java
// Simple console-based Hotel Management System in Java
// Features:
// - Add, View, Book, Checkout, Search, Modify, Delete Rooms
// - Saves and loads data from "rooms.csv"

import java.io.*;
import java.util.*;

class Room {
    int id;
    String type;
    double price;
    boolean booked;
    String guestName;
    int days;

    Room(int id, String type, double price) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.booked = false;
        this.guestName = "";
        this.days = 0;
    }

    @Override
    public String toString() {
        return String.format("%-5d %-12s %-10.2f %-8s %-15s %-4s",
                id, type, price, booked ? "Yes" : "No",
                booked ? guestName : "-", booked ? days : "-");
    }

    public String toCSV() {
        return id + "," + type + "," + price + "," + (booked ? "1" : "0") + "," + guestName + "," + days;
    }

    public static Room fromCSV(String line) {
        try {
            String[] parts = line.split(",", -1);
            if (parts.length < 6) return null;
            Room r = new Room(Integer.parseInt(parts[0]), parts[1], Double.parseDouble(parts[2]));
            r.booked = parts[3].equals("1");
            r.guestName = parts[4];
            r.days = Integer.parseInt(parts[5]);
            return r;
        } catch (Exception e) {
            return null;
        }
    }
}

class Hotel {
    ArrayList<Room> rooms = new ArrayList<>();
    final String fileName = "rooms.csv";
    Scanner sc = new Scanner(System.in);

    Hotel() {
        loadFromFile();
    }

    void addRoom() {
        System.out.print("Enter room id: ");
        int id = sc.nextInt();
        sc.nextLine();
        for (Room r : rooms) {
            if (r.id == id) {
                System.out.println("Room ID already exists!");
                return;
            }
        }
        System.out.print("Enter room type (Single/Double/Suite): ");
        String type = sc.nextLine();
        System.out.print("Enter price per day: ");
        double price = sc.nextDouble();
        rooms.add(new Room(id, type, price));
        System.out.println("Room added successfully!");
        saveToFile();
    }

    void displayRooms() {
        if (rooms.isEmpty()) {
            System.out.println("No rooms available!");
            return;
        }
        System.out.printf("%-5s %-12s %-10s %-8s %-15s %-4s\n",
                "ID", "Type", "Price", "Booked", "Guest", "Days");
        System.out.println("-----------------------------------------------------------");
        for (Room r : rooms) {
            System.out.println(r);
        }
    }

    Room findRoomById(int id) {
        for (Room r : rooms) if (r.id == id) return r;
        return null;
    }

    void bookRoom() {
        System.out.print("Enter room id to book: ");
        int id = sc.nextInt();
        sc.nextLine();
        Room r = findRoomById(id);
        if (r == null) {
            System.out.println("Room not found!");
            return;
        }
        if (r.booked) {
            System.out.println("Room already booked by " + r.guestName);
            return;
        }
        System.out.print("Enter guest name: ");
        r.guestName = sc.nextLine();
        System.out.print("Enter number of days: ");
        r.days = sc.nextInt();
        r.booked = true;
        System.out.println("Room booked successfully for " + r.guestName +
                ". Total = Rs. " + (r.price * r.days));
        saveToFile();
    }

    void checkoutRoom() {
        System.out.print("Enter room id to checkout: ");
        int id = sc.nextInt();
        Room r = findRoomById(id);
        if (r == null) {
            System.out.println("Room not found!");
            return;
        }
        if (!r.booked) {
            System.out.println("Room is not booked.");
            return;
        }
        double total = r.price * r.days;
        System.out.println("Guest: " + r.guestName);
        System.out.println("Days: " + r.days);
        System.out.println("Total Bill: Rs. " + total);
        r.booked = false;
        r.guestName = "";
        r.days = 0;
        System.out.println("Checkout successful. Room is now available.");
        saveToFile();
    }

    void searchByType() {
        sc.nextLine();
        System.out.print("Enter room type to search: ");
        String type = sc.nextLine();
        boolean found = false;
        for (Room r : rooms) {
            if (r.type.equalsIgnoreCase(type)) {
                if (!found) {
                    System.out.printf("%-5s %-12s %-10s %-8s\n", "ID", "Type", "Price", "Booked");
                    System.out.println("-------------------------------------");
                }
                found = true;
                System.out.printf("%-5d %-12s %-10.2f %-8s\n",
                        r.id, r.type, r.price, r.booked ? "Yes" : "No");
            }
        }
        if (!found) System.out.println("No rooms of type " + type + " found.");
    }

    void modifyRoom() {
        System.out.print("Enter room id to modify: ");
        int id = sc.nextInt();
        sc.nextLine();
        Room r = findRoomById(id);
        if (r == null) {
            System.out.println("Room not found!");
            return;
        }
        System.out.print("New type (leave blank to keep same): ");
        String newType = sc.nextLine();
        if (!newType.isEmpty()) r.type = newType;
        System.out.print("New price (0 to keep same): ");
        double newPrice = sc.nextDouble();
        if (newPrice > 0) r.price = newPrice;
        System.out.println("Room updated successfully!");
        saveToFile();
    }

    void deleteRoom() {
        System.out.print("Enter room id to delete: ");
        int id = sc.nextInt();
        Room r = findRoomById(id);
        if (r == null) {
            System.out.println("Room not found!");
            return;
        }
        if (r.booked) {
            System.out.println("Cannot delete a booked room! Checkout first.");
            return;
        }
        rooms.remove(r);
        System.out.println("Room deleted successfully!");
        saveToFile();
    }

    void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            pw.println("id,type,price,booked,guestName,days");
            for (Room r : rooms) {
                pw.println(r.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e);
        }
    }

    void loadFromFile() {
        File f = new File(fileName);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            br.readLine(); // header
            String line;
            while ((line = br.readLine()) != null) {
                Room r = Room.fromCSV(line);
                if (r != null) rooms.add(r);
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e);
        }
    }

    void menu() {
        while (true) {
            System.out.println("\n--- HOTEL MANAGEMENT SYSTEM ---");
            System.out.println("1. Add Room");
            System.out.println("2. Display Rooms");
            System.out.println("3. Book Room");
            System.out.println("4. Checkout Room");
            System.out.println("5. Search by Type");
            System.out.println("6. Modify Room");
            System.out.println("7. Delete Room");
            System.out.println("8. Exit");
            System.out.print("Choose option: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> addRoom();
                case 2 -> displayRooms();
                case 3 -> bookRoom();
                case 4 -> checkoutRoom();
                case 5 -> searchByType();
                case 6 -> modifyRoom();
                case 7 -> deleteRoom();
                case 8 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}

public class HotelManagement {
    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        hotel.menu();
    }
}

