import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * Represents a location in the mansion with specific coordinates, features, boxes, and connections.
 */
public class Room {
    private String name;
    private int x, y;
    private List<Box> boxes;
    private Map<String, Room> connections;
    private String featureItem;

    /**
     * Constructs a new Room.
     * @param name The name of the room.
     * @param x The X-coordinate on the map grid.
     * @param y The Y-coordinate on the map grid.
     * @param featureItem The defining feature or special item location of the room.
     */
    public Room(String name, int x, int y, String featureItem) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.featureItem = featureItem;
        this.boxes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            this.boxes.add(new Box());
        }
        this.connections = new HashMap<>();
    }

    /**
     * Prints the current state and available exits of the room to the console.
     */
    public void describe() {
        System.out.println("\n** Current Room: " + name + " **");
        System.out.println("Room Feature: " + featureItem);

        System.out.print("Room Exits (Direction -> Room Name): ");
        if (connections.isEmpty()) {
            System.out.println("None. You are trapped!");
        } else {
            List<String> exits = new ArrayList<>();
            for(Map.Entry<String, Room> entry : connections.entrySet()) {
                exits.add(entry.getKey() + " -> " + entry.getValue().getName());
            }
            // Use String.join without FQN
            System.out.println(String.join(" | ", exits));
        }

        System.out.print("Room Boxes: ");
        int openCount = 0;
        for (Box box : boxes) {
            if (!box.isOpen()) {
                System.out.print("[Box ID: " + box.getId() + "] ");
            } else {
                openCount++;
            }
        }
        System.out.println("(" + openCount + " boxes are already open)");
    }

    /**
     * Randomly shuffles the content of all boxes in the room.
     * Optionally resets the 'opened' status of boxes.
     * @param resetOpen If true, closes previously opened boxes (unless they contain "Empty Box").
     */
    public void scrambleBoxes(boolean resetOpen) {
        List<Item> contents = new ArrayList<>();
        for (Box box : boxes) {
            contents.add(box.getContent());
            if (resetOpen) {
                box.close();
            }
        }
        Collections.shuffle(contents);

        for (int i = 0; i < boxes.size(); i++) {
            boxes.get(i).setContent(contents.get(i));
        }
    }

    /**
     * Adds a connection (door/stair) to an adjacent room.
     * @param direction The direction of the connection (e.g., NORTH, UP).
     * @param room The adjacent Room object.
     */
    public void addConnection(String direction, Room room) { connections.put(direction, room); }

    /**
     * Retrieves the room connected in the specified direction.
     * @param direction The direction to check.
     * @return The connected Room, or null if no connection exists.
     */
    public Room getNeighbor(String direction) { return connections.get(direction); }

    /**
     * @return The list of boxes in the room.
     */
    public List<Box> getBoxes() { return boxes; }

    /**
     * @return The map of connections (Direction -> Room).
     */
    public Map<String, Room> getConnections() { return connections; }

    /**
     * @return The name of the feature item/location.
     */
    public String getFeatureItem() { return featureItem; }

    /**
     * @return The name of the room.
     */
    public String getName() { return name; }
}
