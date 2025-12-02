public class BoxGirlWeakness extends Item {

    /**
     * Constructs a new BoxGirlWeakness item.
     * @param name The display name of the item.
     * @param description A brief description of the item.
     */
    public BoxGirlWeakness(String name, String description) {
        super(name, description);
    }

    /**
     * Attempts to exorcise the Box Girl using collected weakness items.
     * Requires 2 weaknesses and being in a special room (Miss Mary's or Corpse).
     * @return A message detailing the result of the attempt.
     */
    @Override
    public String use(Player player, Game game) {
        int weakCount = player.countItemOfType(BoxGirlWeakness.class);
        if (weakCount < 2) {
            return "You currently have only " + weakCount + " weakness item(s). You need 2 to perform the exorcism.";
        }

        String roomName = player.getCurrentRoom().getName();
        if (!roomName.contains("Miss Mary") && !roomName.contains("Corpse")) {
            return "You are not in the correct room for exorcism. Find the source of her resentment (Miss Mary's/Corpse Room).";
        }

        game.endGame(true, "\n You successfully gathered the weaknesses and used them on the Box Girl! She vanished with a terrifying scream. You achieved [EXORCISM VICTORY].");
            return "Exorcism successful! Game Over.";

    }
}
