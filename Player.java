import java.util.List;
import java.util.ArrayList;

/**
 * Represents the player navigating through the mansion.
 */
public class Player {
    private Room currentRoom;
    private List<Item> inventory;
    private Game game;

    /**
     * Constructs a new Player.
     * @param startRoom The room where the player begins.
     * @param game The main game instance.
     */
    public Player(Room startRoom, Game game) {
        this.currentRoom = startRoom;
        this.inventory = new ArrayList<>();
        this.game = game;
    }

    /**
     * Adds an item to the player's inventory.
     * @param item The item to be added.
     */
    public void addItem(Item item) {
        this.inventory.add(item);
        item.setObtained(true);
        System.out.println("Result: Obtained: " + item.getName());
        System.out.println(item.getDescription());
    }

    /**
     * Updates the player's current location.
     * @param newRoom The room the player moves to.
     */
    public void move(Room newRoom) {
        this.currentRoom = newRoom;
        System.out.println("\n Moved to the " + newRoom.getName());
    }

    /**
     * Counts the number of items of a specific class type in the inventory.
     * @param type The Class object representing the item type (e.g., BoxGirlWeakness.class).
     * @return The count of items of that type.
     */
    public int countItemOfType(Class<? extends Item> type) {
        int count = 0;
        for (Item item : inventory) {
            if (type.isInstance(item)) {
                count++;
            }
        }
        return count;
    }

    /**
     * @return The player's current room.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * @return The player's current inventory list.
     */
    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * @return The main game instance.
     */
    public Game getGame() {
        return game;
    }
}
