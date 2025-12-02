import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class BoxGirl {
    private Room currentRoom;
    private List<Manuscript> unlockedSkills;
    private Random random = new Random();
    private Box hiddenInBox = null;

    /**
     * Constructs a new BoxGirl.
     * @param startRoom The room where the Box Girl starts (e.g., Basement).
     */
    public BoxGirl(Room startRoom) {
        this.currentRoom = startRoom;
        this.unlockedSkills = new ArrayList<>();
    }

    /**
     * Checks for passive actions (movement, crying, skill activation) after the player's turn.
     * @param player The player instance.
     * @param game The main game instance.
     */
    public void checkPassiveTriggers(Player player, Game game) {
        if (player.isJustUsedCounterItem()) {
            player.setJustUsedCounterItem(false);
            System.out.println(" CALM ENVIRONMENT! The item you used temporarily deterred the evil presence.");
            return;
        }

        if (!unlockedSkills.isEmpty() && random.nextDouble() < 0.3) {
            Manuscript skill = unlockedSkills.get(random.nextInt(unlockedSkills.size()));
            System.out.println("\n !!!! Resentment explodes around you! " + skill.getName() + " is activated!");
            this.activateSkill(skill.getSkillName(), player, game);
        }

    this.randomMove(game);
    }

    /**
     * Moves the Box Girl to a random adjacent room.
     */
    /**
     * Moves the Box Girl to a random adjacent room. She may choose to hide in an unopened box in the destination.
     */
    public void randomMove(Game game) {
        if (this.hiddenInBox != null) {
            this.hiddenInBox.clearHiddenOccupant();
            this.hiddenInBox = null;
        }

        List<Room> possibleMoves = new ArrayList<>(currentRoom.getConnections().values());
        if (possibleMoves.isEmpty()) return;

        Room dest = possibleMoves.get(random.nextInt(possibleMoves.size()));
        this.currentRoom = dest;

        if (random.nextDouble() < 0.4) {
            List<Box> unopened = new ArrayList<>();
            for (Box b : dest.getBoxes()) {
                if (!b.isOpen() && b.getHiddenOccupant() == null) unopened.add(b);
            }

            if (!unopened.isEmpty()) {
                Box chosen = unopened.get(random.nextInt(unopened.size()));
                chosen.setHiddenOccupant(this);
                this.hiddenInBox = chosen;
                System.out.println("You sense nothing unusual... but something might be hiding nearby.");
            }
        }
    }

    /**
     * Executes the effect of a specific unlocked skill.
     * @param skillName The name of the skill to execute.
     * @param player The player instance.
     * @param game The main game instance.
     */
    public void activateSkill(String skillName, Player player, Game game) {
        switch(skillName) {
            case "Right Beside You":
                this.currentRoom = player.getCurrentRoom();
                System.out.println("You catch a glimpse of a moving box in your periphery... It's right beside you!");
                break;
            case "Moving Around":
                player.getCurrentRoom().scrambleBoxes(true);
                System.out.println("The boxes in the room seem to have moved, and some have even closed...");
                break;
            case "I Want to Open It":
                System.out.println("The Box Girl's resentment compels you to a strong impulse! You must open a box!");

                Box targetBox = player.getCurrentRoom().getBoxes().stream()
                        .filter(b -> !b.isOpen() && b.getContent() instanceof Manuscript)
                        .findAny().orElse(
                                player.getCurrentRoom().getBoxes().stream().filter(b -> !b.isOpen()).findAny().orElse(null)
                        );

                if (targetBox != null) {
                    System.out.println("Your body moves against your will to open Box " + targetBox.getId() + "!");
                    Item content = targetBox.open();
                    if (content != null) {
                        System.out.println("You found: " + content.getName());
                        if (content instanceof Manuscript) {
                            ((Manuscript) content).activateSkill(game);
                        } else {
                            player.addItem(content);
                        }
                    }
                } else {
                    System.out.println("The Box Girl tried to force you, but all boxes are already open.");
                }
                break;
            case "Give It to Me":
                Item weakestItem = player.getInventory().stream()
                        .filter(i -> i instanceof BoxGirlWeakness)
                        .findFirst().orElse(null);

                if (weakestItem != null) {
                    player.getInventory().remove(weakestItem);
                    game.addItemToRandomBox(weakestItem);
                    System.out.println("A shadow quickly sweeps past, and your " + weakestItem.getName() + " is stolen!");
                } else {
                    System.out.println("The Box Girl attempted to steal, but you don't possess her greatest fear.");
                }
                break;
            case "Locked Door":
                System.out.println("You feel an oppressive force around the exits, making the doors feel stuck.");
                break;
        }
    }

    /**
     * @return The Box Girl's current room.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * @return The box the BoxGirl is currently hiding in, or null if visible.
     */
    public Box getHiddenInBox() { return hiddenInBox; }

    /**
     * Unlocks a new skill for the Box Girl based on the collected manuscript.
     * @param manuscript The Manuscript item.
     */
    public void unlockSkill(Item manuscript) {
        if (manuscript instanceof Manuscript) {
            this.unlockedSkills.add((Manuscript) manuscript);
            System.out.println("Box Girl Skill [" + ((Manuscript) manuscript).getSkillName() + "] has been unlocked!");
        }
    }
}
