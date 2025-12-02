/**
 * Represents a Manuscript that unlocks a new skill for the Box Girl.
 * Cannot be used directly by the player.
 */
public class Manuscript extends Item {
    private String skillName;

    /**
     * Constructs a new Manuscript.
     * @param name The display name of the manuscript.
     * @param description A description of the skill it unlocks.
     * @param skillName The identifier for the Box Girl's new skill.
     */
    public Manuscript(String name, String description, String skillName) {
        super(name, description);
        this.skillName = skillName;
    }

    /**
     * Handlers for when the player attempts to use the manuscript (always fails).
     * @return A message stating the item cannot be used.
     */
    @Override
    public String use(Player player, Game game) {
        return "The manuscript cannot be used directly by the player. Its contents have already empowered the Box Girl.";
    }

    /**
     * Activates the Box Girl's skill associated with this manuscript.
     * @param game The current game instance.
     */
    public void activateSkill(Game game) {
        game.getBoxGirl().unlockSkill(this);
    }

    /**
     * @return The name of the skill unlocked by this manuscript.
     */
    public String getSkillName() { return skillName; }
}
