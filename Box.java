public class Box {
    private static int nextId = 1;
    private int id;
    private boolean isOpen = false;
    private Item content;
    private BoxGirl hiddenOccupant = null;

    /**
     * Constructs a new box with a unique ID.
     */
    public Box() {
        this.id = nextId++;
    }

    /**
     * Opens the box, retrieving its content if not already open.
     * @return The item contained, or null if empty or already opened.
     */
    public Item open() {
        if (isOpen) {
            java.lang.System.out.println("This box has already been opened.");
            return null;
        }
        isOpen = true;
        return this.content;
    }

    /**
     * @return The BoxGirl hiding in this box, or null if none.
     */
    public BoxGirl getHiddenOccupant() { return hiddenOccupant; }

    /**
     * Set the BoxGirl as hiding inside this box.
     */
    public void setHiddenOccupant(BoxGirl bg) { this.hiddenOccupant = bg; }

    /**
     * Clear any BoxGirl hiding in this box.
     */
    public void clearHiddenOccupant() { this.hiddenOccupant = null; }

    /**
     * Sets the content of the box. Used during map generation and the 'Moving Around' skill.
     * @param content The item to place inside the box.
     */
    public void setContent(Item content) {
        this.content = content;
    }

    /**
     * Closes the box. Used by the Box Girl's 'Moving Around' skill.
     */
    public void close() {
        if (this.hiddenOccupant != null) return;

        if (this.content == null || !this.content.getName().equals("Empty Box")) {
            this.isOpen = false;
        }
    }

    /**
     * @return The unique ID of the box.
     */
    public int getId() {
        return id;
    }

    /**
     * @return True if the box has been opened.
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * @return The item content of the box.
     */
    public Item getContent() {
        return content;
    }
}
