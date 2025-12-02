import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Game {
    private Player player;
    private GameMap map;
    private BoxGirl boxGirl;
    private Scanner scanner = new Scanner(System.in);
    private boolean isGameOver = false;

    /**
     * Main method to start the game.
     */
    public static void main(String[] args) {
        new Game().start();
    }

    /**
     * Initializes the game state and starts the main game loop.
     */
    public void start() {
        initializeGame();

        // Game Background and Goal
        System.out.println("Welcome to THE BOX GIRL");
        System.out.println("BACKGROUND: You have been trapped inside a haunted Japanese mansion by the 'Box Girl', a monstrous entity concealed within a box.");
        System.out.println("GOAL: Escape before she catches you. You can achieve victory in one of two ways:");
        System.out.println("  1. ESCAPE: Find 3 Passwords and use them at the Hidden Exit location.");
        System.out.println("  2. EXORCISE: Find 2 Weakness items and use them in a special room (Miss Mary's or Corpse location).");
        System.out.println("-------------------------------------");
        System.out.println("You are currently in the " + player.getCurrentRoom().getName() + ".");

        while (!isGameOver) {
            playerTurn();
            if (isGameOver) break;

            boxGirl.checkPassiveTriggers(player, this);
            checkGameState();
        }

        System.out.println("\n GAME OVER");
    }

    /**
     * Initializes all game components: generates the map, defines and distributes all items,
     * creates the Player, and initializes the BoxGirl's starting position.
     */
    private void initializeGame() {
        map = new GameMap();
        map.generateRandomMap();

        List<Item> allItems = new ArrayList<>();

        // 3 Passwords
        allItems.add(new Password("Password-A", "A piece of paper with numbers."));
        allItems.add(new Password("Password-B", "A piece of paper with numbers."));
        allItems.add(new Password("Password-C", "A piece of paper with numbers."));

        // 5 Box Girl Weaknesses
        allItems.add(new BoxGirlWeakness("White Camellia", "A rare white camellia."));
        allItems.add(new BoxGirlWeakness("Gasoline", "A small can of gasoline."));
        allItems.add(new BoxGirlWeakness("Rusty Chain", "A rusty chain with a binding power."));
        allItems.add(new BoxGirlWeakness("Weakness Fragment-1", "A fragment of a weakness."));
        allItems.add(new BoxGirlWeakness("Weakness Fragment-2", "A fragment of a weakness."));

        // 3 Counter Items
        allItems.add(new CounterItem("Matches", "Can temporarily frighten the Box Girl."));
        allItems.add(new CounterItem("Rope", "Can temporarily restrain the Box Girl."));
        allItems.add(new CounterItem("Axe", "Can temporarily frighten the Box Girl."));

        // 5 Manuscripts
        allItems.add(new Manuscript("Manuscript-Beside", "It is right beside you", "Right Beside You"));
        allItems.add(new Manuscript("Manuscript-Move", "Box Girl moves freely on the same floor", "Moving Around"));
        allItems.add(new Manuscript("Manuscript-Door", "Door cannot be passed", "Locked Door"));
        allItems.add(new Manuscript("Manuscript-Open", "Forces player to open a box", "I Want to Open It"));
        allItems.add(new Manuscript("Manuscript-Take", "Box Girl steals an item", "Give It to Me"));

        // 4 Empty Box Markers
        for(int i = 0; i < 4; i++) {
            allItems.add(new Item("Empty Box", "A placeholder for an empty box.") {
                @Override public String use(Player p, Game g) { return "This box is empty."; }
            });
        }

        map.distributeItems(allItems);

        Room startRoom = map.getRoom("First Floor Hall");
        player = new Player(startRoom, this);
        boxGirl = new BoxGirl(map.getRoom("Basement"));
    }

    /**
     * Executes the player's turn, processing input and actions.
     */
    private void playerTurn() {
        player.getCurrentRoom().describe();

        System.out.println("\n YOUR TURN");
        System.out.println("Choose action: [move] / [open box] / [use item] / [whereami]");
        String action = scanner.nextLine().toLowerCase().trim();

        switch (action) {
            case "move":
                handleMove();
                break;
            case "open box":
                handleOpenBox();
                break;
            case "use item":
                handleUseItem();
                break;
            case "whereami":
                System.out.println("You are currently in the " + player.getCurrentRoom().getName() + ".");
                break;
            default:
                System.out.println("Action failed: Invalid command. Turn lost.");
                break;
        }
    }
    /**
     * Handles the player's movement command.
     */
    private void handleMove() {
        Room current = player.getCurrentRoom();

        System.out.println("Enter movement direction (e.g.: NORTH, SOUTH, UP, BASEMENT):");
        String direction = scanner.nextLine().toUpperCase().trim();

        Room nextRoom = current.getNeighbor(direction);

        if (nextRoom != null) {
            player.move(nextRoom);
        } else {
            System.out.println("Action failed: There is no path in that direction.");
        }
    }

    /**
     * Handles the player's action of opening a box in the current room.
     */
    private void handleOpenBox() {
        List<Box> boxes = player.getCurrentRoom().getBoxes();
        List<Box> unopenedBoxes = new ArrayList<>();

        System.out.print("Unopened boxes IDs: ");
        for (Box box : boxes) {
            if (!box.isOpen()) {
                unopenedBoxes.add(box);
                System.out.print(box.getId() + " ");
            }
        }
        System.out.println();

        if (unopenedBoxes.isEmpty()) {
            System.out.println("Action failed: All boxes in this room are already open.");
            return;
        }

        /**
         * Handles the player's action of using an item from their inventory.
         */
        System.out.println("Enter the ID of the box to open:");
        try {
            int boxId = Integer.parseInt(scanner.nextLine().trim());
            Box targetBox = boxes.stream()
                    .filter(b -> b.getId() == boxId && !b.isOpen())
                    .findFirst()
                    .orElse(null);

            if (targetBox != null) {
                if (targetBox.getHiddenOccupant() != null) {
                    System.out.println("Result: As you open the box, a monstrous presence emerges!");
                    endGame(false, "You opened the box that hid the Box Girl. She caught you immediately.");
                    return;
                }

                Item content = targetBox.open();

                if (content == null || content.getName().equals("Empty Box")) {
                    System.out.println("Result: The box is empty.");
                } else if (content instanceof Manuscript) {
                    System.out.println("Result: RESENTMENT BURST. A " + content.getName() + " was found and activated the Box Girl's power.");
                    ((Manuscript) content).activateSkill(this);
                } else {
                    player.addItem(content);
                }
            } else {
                System.out.println("Action failed: Invalid box ID or the box is already open.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Action failed: Invalid input. Please enter a number.");
        }
    }

    /**
     * Handles the player's action of using an item from their inventory.
     */
    private void handleUseItem() {
        List<Item> inventory = player.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("Action failed: Your inventory is empty.");
            return;
        }

        System.out.println("YOUR ITEMS：");
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println((i + 1) + ". " + inventory.get(i).getName());
        }

        System.out.println("Enter the name of the item to use:");
        String itemName = scanner.nextLine().trim();

        Item targetItem = getItemFromInventoryByName(itemName);

        if (targetItem != null) {
            String result = targetItem.use(player, this);
            System.out.println("Result: " + result);
        } else {
            System.out.println("Action failed: You do not have that item.");
        }
    }

    /**
     * Checks the game state for immediate failure conditions (encountering Box Girl).
     */
    private void checkGameState() {
        if (player.getCurrentRoom().equals(boxGirl.getCurrentRoom()) && boxGirl.getHiddenInBox() == null) {
            System.out.println("\n CAUGHT!!! The Box Girl is in the room with you and reveals herself!");
            endGame(false, "You failed to escape the Box Girl's pursuit.");
        }
    }

    /**
     * Ends the game and displays the final message.
     * @param win True if the player won, false otherwise.
     * @param message The final message explaining the outcome.
     */
    public void endGame(boolean win, String message) {
        isGameOver = true;
        System.out.println("-------------------------------------------");
        System.out.println(win ? "VICTORY ACHIEVED!" : "FAILURE!");
        System.out.println(message);
        System.out.println("-------------------------------------------");
    }

    /**
     * Finds an item in the player's inventory by name (case-insensitive).
     * @param name The name of the item to find.
     * @return The Item object, or null if not found.
     */
    public Item getItemFromInventoryByName(String name) {
        return player.getInventory().stream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Hides a stolen item back into a random unopened empty box.
     * @param item The item to be hidden.
     */
    public void addItemToRandomBox(Item item) {
        List<Box> allBoxes = map.getAllBoxes();
        Collections.shuffle(allBoxes);

        Box target = allBoxes.stream()
                .filter(b -> !b.isOpen() && b.getContent() == null)
                .findFirst()
                .orElse(null);

        if (target != null) {
            target.setContent(item);
            System.out.println("Status: The stolen item has been re-hidden.");
        } else {
            System.out.println("Status: The item is lost, Box Girl failed to find a hiding spot.");
        }
    }

    /**
     * @return The Map instance.
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * @return The BoxGirl instance.
     */
    public BoxGirl getBoxGirl() {
        return boxGirl;
    }
}
