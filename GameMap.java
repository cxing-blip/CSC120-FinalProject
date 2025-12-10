import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class GameMap {
    private Map<String, Room> allRooms;
    private Room[][] mapGrid;
    private int size = 9;

    /**
     * Constructs a new Map instance and initializes the internal structures.
     */
    public GameMap() {
        this.allRooms = new HashMap<>();
        this.mapGrid = new Room[size][3];
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

        List<String> roomNames = new ArrayList<>(Arrays.asList("Dining Room", "Living Room", "Bathroom", "Children's Room", "Study Room", "Guest Room"));
        List<String> features = new ArrayList<>(Arrays.asList(null, null, null, "Rocking Chair (Hidden Exit)", "Seeping Walls (Corpse Location)", "Carved door (Miss Mary's Location)"));
        Collections.shuffle(roomNames);
        Collections.shuffle(features);

        Room basement = createRoom("Basement", 0, 1, null);
        Room firstHall = createRoom("First Floor Hall", 3, 1, null);
        Room secondHall = createRoom("Second Floor Hall", 7, 1, null);

        firstHall.addConnection("UP", secondHall);
        secondHall.addConnection("DOWN", firstHall);
        firstHall.addConnection("DOWN", basement);
        basement.addConnection("UP", firstHall);

        String[] directions = new String[]{"NORTH", "SOUTH", "EAST", "WEST"};
        int nameIndex = 0, featIndex = 0;

        for (Room hall : Arrays.asList(firstHall, secondHall)) {
            List<String> pool = new ArrayList<>(Arrays.asList(directions));
            Collections.shuffle(pool);
            List<String> chosen = pool.subList(0, 3);

            int hx = -1, hy = -1;
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < 3; y++) {
                    if (mapGrid[x][y] == hall) { hx = x; hy = y; break; }
                }
                if (hx != -1) break;
            }

            for (String dir : chosen) {
                int nx = hx, ny = hy;
                switch (dir) {
                    case "NORTH": nx = hx - 1; break;
                    case "SOUTH": nx = hx + 1; break;
                    case "WEST": ny = hy - 1; break;
                    case "EAST": ny = hy + 1; break;
                }

                if (nx < 0) nx = hx + 1;
                if (nx >= size) nx = hx - 1;
                if (ny < 0) ny = hy + 1;
                if (ny >= size) ny = hy - 1;

                if (mapGrid[nx][ny] != null) {
                    boolean placed = false;
                    for (int dx = -1; dx <= 1 && !placed; dx++) {
                        for (int dy = -1; dy <= 1 && !placed; dy++) {
                            int tx = hx + dx, ty = hy + dy;
                            if (tx >= 0 && tx < size && ty >= 0 && ty < size && mapGrid[tx][ty] == null) {
                                nx = tx; ny = ty; placed = true;
                            }
                        }
                    }
                    if (!placed) continue;
                }

                String rname = roomNames.get(nameIndex % roomNames.size()) + (nameIndex >= roomNames.size() ? "-" + nameIndex : "");
                String feat = features.get(featIndex % features.size());
                Room r = createRoom(rname, nx, ny, feat);

                hall.addConnection(dir, r);
                String opp = dir.equals("NORTH") ? "SOUTH" : dir.equals("SOUTH") ? "NORTH" : dir.equals("EAST") ? "WEST" : "EAST";
                r.addConnection(opp, hall);

                nameIndex++; featIndex++;
            }
        }

        printAsciiMap();
    }

    /**
     * Prints a simple ASCII map of the grid layout to the console.
     */
    private void printAsciiMap() {
        int cols = mapGrid[0].length;
        System.out.println("\n------------------------ ASCII MAP ------------------------");
        for (int x = 0; x < size; x++) {
            StringBuilder line = new StringBuilder();
            for (int y = 0; y < cols; y++) {
                Room r = mapGrid[x][y];
                if (r == null) {
                    line.append("[                 ] ");
                } else {
                    String n = r.getName();
                    line.append("[" + String.format("%-17s", n) + "] ");
                }
            }
            System.out.println(line.toString());
        }
        System.out.println("------------------------- END MAP -------------------------\n");
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
