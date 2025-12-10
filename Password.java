/**
 * Represents a password item that can be used to escape the mansion.
 */
public class Password extends Item {

    /**
     * Constructs a new Password item.
     * @param name The display name of the item.
     * @param description A brief description of the item.
     */
    public Password(String name, String description) {
        super(name, description);
    }

    /**
     * Attempts to use the password(s) to open the Hidden Exit.
     * Requires 3 passwords and being in the Hidden Exit room.
     * @return A message detailing the result of the escape attempt.
     */
    @Override
    public String use(Player player, Game game) {
        int passCount = player.countItemOfType(Password.class);
        if (passCount < 3) {
            return "You currently have only " + passCount + " password(s). You need 3.";
        }

        if (player.getCurrentRoom().getFeatureItem().contains("Hidden Exit")) {
            game.endGame(true, "\nYou entered the correct passwords, and the hidden exit creaked open! You escaped the mansion and achieved [ESCAPE VICTORY].");
            return "Password match successful! Game Over.";
        } else {
            return "You hold the passwords, but there is no exit here to open.";
        }
    }
}
