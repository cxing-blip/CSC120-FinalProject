import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

/**
 * Main game class that initializes and runs the mansion escape game.
 */
public class Game {
    private Player player;
    private GameMap map;
    private BoxGirl boxGirl;
    private BoxGirl boxGirl2;
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

        // Game Background and Goal
        System.out.println(
            "-----------------------------------------------------------" +
            "\nPast midnight, you step into an abandoned old villa—a place long swallowed by dust, silence, and things better left forgotten.\n" +
            "Splintered wooden boards and half-rotted suitcases lie scattered across the floor.\n" +
            "A faint, sickly-sweet smell seeps through the dark halls.\n" +
            "Local whispers once said the discarded boxes here would awaken after nightfall—\n" +
            "that they speak in the voices of past visitors…\n" +
            "and that the creatures wearing those voices, known as boxgirls, still linger in the shadows.\n" +
            "\n" +
            "You feel the floor tremble softly beneath your feet.\n" +
            "Something has noticed your presence.\n" +
            "The villa is watching.\n" +
            "\n" +
            "HOW TO WIN: \n" +
            "1. Find three passwords:\n" +
            "Only by collecting all three passwords can you unlock the hidden exit and escape this villa.\n" +
            "\n" +
            "2. Defeat:\n" +
            "Discover at least two weak points of the boxgirl, and carry them to the room where Miss Mary or the corpse is located in order to defeat it."
        );

        initializeGame();
        System.out.println("You are currently in the " + player.getCurrentRoom().getName() + ".");

        while (!isGameOver) {
            playerTurn();
            if (isGameOver) break;

            boxGirl.checkPassiveTriggers(player, this);
            if (boxGirl2 != null) {
                boxGirl2.checkPassiveTriggers(player, this);
            }
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

        // 5 Passwords
        allItems.add(new Password("Password-A", "A piece of paper with numbers."));
        allItems.add(new Password("Password-B", "A piece of paper with numbers."));
        allItems.add(new Password("Password-C", "A piece of paper with numbers."));
        allItems.add(new Password("Password-D", "A piece of paper with numbers."));
        allItems.add(new Password("Password-E", "A piece of paper with numbers."));

        // 5 Box Girl Weaknesses
        allItems.add(new BoxGirlWeakness("White Camellia", "A rare white camellia. One of the Box Girl's weaknesses."));
        allItems.add(new BoxGirlWeakness("Gasoline", "A small can of gasoline. One of the Box Girl's weaknesses."));
        allItems.add(new BoxGirlWeakness("Rusty Chain", "A rusty chain with a binding power. One of the Box Girl's weaknesses."));
        allItems.add(new BoxGirlWeakness("Lighter", "One of the Box Girl's weaknesses."));
        allItems.add(new BoxGirlWeakness("Old Scissor", "One of the Box Girl's weaknesses."));

        // 3 Counter Items
        allItems.add(new CounterItem("Matches", "Can temporarily frighten the Box Girl."));
        allItems.add(new CounterItem("Rope", "Can temporarily restrain the Box Girl."));
        allItems.add(new CounterItem("Axe", "Can temporarily frighten the Box Girl."));

        // 7 Manuscripts
        allItems.add(new Manuscript("Manuscript-Beside", "Box Girl is right beside you", "Right Beside You"));
        allItems.add(new Manuscript("Manuscript-Beside", "Box Girl is right beside you", "Right Beside You"));
        allItems.add(new Manuscript("Manuscript-Move", "Box Girl moves freely on the same floor", "Moving Around"));
        allItems.add(new Manuscript("Manuscript-Door", "Door cannot be passed", "Locked Door"));
        allItems.add(new Manuscript("Manuscript-Open", "Forces player to open a box", "I Want to Open It"));
        allItems.add(new Manuscript("Manuscript-Take", "Box Girl steals an item", "Give It to Me"));
        allItems.add(new Manuscript("Manuscript-TwinSister", "You released Box Girl's sister! Now there are 2 box girls", "Box Girl"));

        // 7 Empty Box Markers
        for(int i = 0; i < 7; i++) {
            allItems.add(new Item("Empty Box", "A placeholder for an empty box.") {
                @Override public String use(Player p, Game g) { return "This box is empty."; }
            });
        }

        map.distributeItems(allItems);

        Room startRoom = map.getRoom("First Floor Hall");
        Box startBox = map.getRoom("Second Floor Hall").getBoxes().get(0);
        player = new Player(startRoom, this);
        boxGirl = new BoxGirl(map.getRoom("Second Floor Hall"), startBox);
    }

    /**
     * Executes the player's turn, processing input and actions.
     */
    private void playerTurn() {
        player.getCurrentRoom().describe();

        System.out.println("\n YOUR TURN");
        System.out.println("Choose action: [move] / [open box] / [use item] / [put item] / [help]");
        String action = scanner.nextLine().toLowerCase().trim();

        switch (action) {
            case "move":
                handleMove();
                break;
            case "open box":
                handleOpenBox();
                break;
            case "put item":
                handlePutItem();
                break;
            case "use item":
                handleUseItem();
                break;
            case "help":
                printHelp();
                break;
            default:
                System.out.println("Action failed: Invalid command. Turn lost.");
                break;
        }
    }

    /**
     * Handles putting an item from the player's inventory into an already-opened box in the current room.
     * This action consumes the player's turn. After placing the item the box will be closed (unless it contains a hidden occupant).
     */
    private void handlePutItem() {
        List<Box> boxes = player.getCurrentRoom().getBoxes();
        List<Box> openedBoxes = new ArrayList<>();

        System.out.print("Opened boxes IDs: ");
        for (Box box : boxes) {
            if (box.isOpen()) {
                openedBoxes.add(box);
                System.out.print(box.getId() + " ");
            }
        }
        System.out.println();

        if (openedBoxes.isEmpty()) {
            System.out.println("Action failed: There are no opened boxes in this room to put items into.");
            return;
        }

        List<Item> inventory = player.getInventory();
        if (inventory.isEmpty()) {
            System.out.println("Action failed: Your inventory is empty.");
            return;
        }

        System.out.println("YOUR ITEMS: ");
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println((i + 1) + ". " + inventory.get(i).getName());
        }

        System.out.println("Enter the ID of the opened box to put the item into:");
        try {
            int boxId = Integer.parseInt(scanner.nextLine().trim());
            Box targetBox = boxes.stream()
                    .filter(b -> b.getId() == boxId && b.isOpen())
                    .findFirst()
                    .orElse(null);

            if (targetBox == null) {
                System.out.println("Action failed: Invalid box ID or the box is not open.");
                return;
            }

            System.out.println("Enter the name of the item to put into the box:");
            String itemName = scanner.nextLine().trim();
            Item itemToPut = getItemFromInventoryByName(itemName);

            if (itemToPut == null) {
                System.out.println("Action failed: You do not have that item.");
                return;
            }

            boolean placed = targetBox.putItem(itemToPut);
            if (placed) {
                player.removeItem(itemToPut);
                System.out.println("Result: You placed " + itemToPut.getName() + " into box " + targetBox.getId() + ". The box is now closed.");
            } else {
                System.out.println("Action failed: Could not place the item into the box.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Action failed: Invalid input. Please enter a number.");
        }
    }

    /**
     * Print cheatsheet.md contents to the console. Falls back to a short help message on error.
     */
    private void printHelp() {
        try {
            String content = Files.readString(Paths.get("cheatsheet.md"));
            System.out.println("\n--- HELP ---\n" + content + "\n--- END HELP ---\n");
        } catch (IOException e) {
            System.out.println("Help file not available. Short help:\n- move\n- open box\n- use item\n");
        }
    }
    /**
     * Handles the player's movement command.
     */
    private void handleMove() {
        Room current = player.getCurrentRoom();

        System.out.println("Enter movement direction (e.g.: NORTH, SOUTH, EAST, WEST, UP, DOWN):");
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
                    if (content instanceof Manuscript && ((Manuscript) content).getSkillName().equalsIgnoreCase("Box Girl")) {
                        Box startBox2 = map.getRoom("Basement").getBoxes().get(2);
                        boxGirl2 = new BoxGirl(map.getRoom("Basement"), startBox2);
                        System.out.println("Result: You have released the Box Girl's sister! Now there are two box girls.");
                    } else {
                        System.out.println("Result: RESENTMENT BURST. A " + content.getName() + " was found and activated the Box Girl's power. The manuscript disappeared in the air.");
                        ((Manuscript) content).activateSkill(this);
                    }
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

        System.out.println("YOUR ITEMS: ");
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
