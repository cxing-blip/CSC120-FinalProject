import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class GameMap {
    private Map<String, Room> allRooms;
    private Room[][] mapGrid;
    private int size = 5;
    private Random random = new Random();

    /**
     * Constructs a new Map instance and initializes the internal structures.
     */
    public GameMap() {
        this.allRooms = new HashMap<>();
        this.mapGrid = new Room[size][size];
    }

    /**
     * Creates a new Room object, adds it to the map's list of all rooms,
     * and places it in the grid at the specified coordinates.
     *
     * @param name The name of the room.
     * @param x The x-coordinate of the room in the map grid.
     * @param y The y-coordinate of the room in the map grid.
     * @param feature A unique descriptive feature of the room.
     * @return The newly created Room object.
     */
    private Room createRoom(String name, int x, int y, String feature) {
        Room room = new Room(name, x, y, feature);
        allRooms.put(name, room);
        mapGrid[x][y] = room;
        return room;
    }

    /**
     * Generates the random mansion layout, placing rooms and establishing connections.
     */
    public void generateRandomMap() {
        // Define Rooms and Fixed Locations
        Room r1FHall = createRoom("First Floor Hall", 2, 2, "Carved door (Miss Mary's Location)");
        Room r2FHall = createRoom("Second Floor Hall", 4, 2, "Crystal Chandelier");
        Room rBasement = createRoom("Basement", 0, 0, "Seeping Walls (Corpse Location)");

        // Establish fixed connections (Stairs/Basement access)
        r1FHall.addConnection("UP", r2FHall);
        r2FHall.addConnection("DOWN", r1FHall);
        r1FHall.addConnection("BASEMENT", rBasement);
        rBasement.addConnection("UPPER", r1FHall);

        // Randomly Place Other Rooms
        List<String> roomNames = new ArrayList<>(Arrays.asList("Dining Room", "Living Room", "Bathroom", "Storage Room", "Crimson Room", "Children's Room"));
        List<String> features = new ArrayList<>(Arrays.asList("Long Table", "Fireplace", "Bathtub", "Old Goods", "Blood Stains", "Rocking Chair (Hidden Exit)"));
        Collections.shuffle(roomNames);
        Collections.shuffle(features);

        List<int[]> availableCoords = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (mapGrid[x][y] == null) {
                    availableCoords.add(new int[]{x, y});
                }
            }
        }
        Collections.shuffle(availableCoords);

        int roomsToPlace = Math.min(roomNames.size(), availableCoords.size());
        for (int i = 0; i < roomsToPlace; i++) {
            int[] coord = availableCoords.get(i);
            createRoom(roomNames.get(i), coord[0], coord[1], features.get(i));
        }

        // Establish Random Adjacent Connections
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Room current = mapGrid[x][y];
                if (current == null) continue;

                checkAndConnect(current, x - 1, y, "NORTH", "SOUTH");
                checkAndConnect(current, x + 1, y, "SOUTH", "NORTH");
                checkAndConnect(current, x, y - 1, "WEST", "EAST");
                checkAndConnect(current, x, y + 1, "EAST", "WEST");
            }
        }
    }

    /**
     * Checks a neighboring grid coordinate for a potential room and attempts to establish a bidirectional connection.
     * @param r1 The starting Room to connect from.
     * @param x2 The x-coordinate of the potential neighboring Room (r2).
     * @param y2 The y-coordinate of the potential neighboring Room (r2).
     * @param dir1 The direction of the connection from r1 to r2 (e.g., "NORTH").
     * @param dir2 The opposite direction of the connection from r2 to r1 (e.g., "SOUTH").
     */
    private void checkAndConnect(Room r1, int x2, int y2, String dir1, String dir2) {
        if (x2 >= 0 && x2 < size && y2 >= 0 && y2 < size) {
            Room r2 = mapGrid[x2][y2];
            if (r2 != null && !r1.getConnections().containsKey(dir1)) {
                if (random.nextDouble() < 0.3 || r1.getName().contains("Hall") || r2.getName().contains("Hall")) {
                    r1.addConnection(dir1, r2);
                    r2.addConnection(dir2, r1);
                }
            }
        }
    }

    /**
     * Randomly distributes all items into the boxes across the mansion.
     * @param allItems A list containing all Item objects (including "Empty Box" markers).
     */
    public void distributeItems(List<Item> allItems) {
        List<Box> allBoxes = new ArrayList<>();
        allRooms.values().forEach(room -> allBoxes.addAll(room.getBoxes()));

        Collections.shuffle(allItems);
        Collections.shuffle(allBoxes);

        for (int i = 0; i < Math.min(allItems.size(), allBoxes.size()); i++) {
            allBoxes.get(i).setContent(allItems.get(i));
        }
    }

    /**
     * Retrieves a room by its name.
     * @param name The name of the room.
     * @return The Room object.
     */
    public Room getRoom(String name) { return allRooms.get(name); }

    /**
     * @return A list of all boxes in the mansion.
     */
    public List<Box> getAllBoxes() {
        List<Box> boxes = new ArrayList<>();
        allRooms.values().forEach(room -> boxes.addAll(room.getBoxes()));
        return boxes;
    }
}
