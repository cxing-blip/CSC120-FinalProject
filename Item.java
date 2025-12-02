public abstract class Item {
    private String name;
    private String description;
    private boolean isObtained = false;

    /**
     * Constructs a new Item.
     * @param name The display name of the item.
     * @param description A brief description of the item's purpose.
     */
    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Defines the action taken when the player uses this item.
     * @param player The player using the item.
     * @param game The current game instance for state manipulation.
     * @return A message describing the result of the item usage.
     */
    public abstract String use(Player player, Game game);

    /**
     * @return The name of the item.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The description of the item.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return True if the item is currently in the player's inventory.
     */
    public boolean isObtained() {
        return isObtained;
    }

    /**
     * Sets the obtained status of the item.
     * @param obtained The new status.
     */
    public void setObtained(boolean obtained) {
        this.isObtained = obtained;
    }
}
