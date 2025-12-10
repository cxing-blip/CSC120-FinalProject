public class CounterItem extends Item {

    /**
     * Constructs a new CounterItem.
     * @param name The display name of the item.
     * @param description A brief description of the item.
     */
    public CounterItem(String name, String description) {
        super(name, description);
    }

    /**
     * Uses the counter item to prevent Box Girl's actions this turn.
     * If the Box Girl is in the same room, she is moved away.
     * @return A message detailing the item's effect.
     */
    @Override
    public String use(Player player, Game game) {
        player.getInventory().remove(this);

        if (player.getCurrentRoom().equals(game.getBoxGirl().getCurrentRoom())) {
            game.getBoxGirl().randomMove(game, player);
            return getName() + "'s power startled the Box Girl, and she fled your room!";
        } else {
            return "The aura of the " + getName() + " calms the atmosphere. You feel safe for now.";
        }
    }
}
