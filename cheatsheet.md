# Cheatsheet

This cheatsheet lists the commands for players, important item types, and victory conditions and special rooms.

1. Commands for Players

Enter one of the commands below on your turn.

- move
	- Description: Move to a connected room.
	- Usage: Type `move` then enter a direction when prompted (`NORTH`, `SOUTH`, `EAST`, `WEST`, `UP`, `DOWN`).
	- Example:
		- move
		- NORTH

- open box
	- Description: Open an unopened box in the current room.
	- Usage: Type `open box`, see the list of unopened box IDs, then enter the numeric ID to open.
	- Notes: Some boxes contain Items (Passwords, Weaknesses, Manuscripts, CounterItems). Manuscripts auto-activate the Box Girl's skill, and then disappears.

- use item
	- Description: Use an item from your inventory.
	- Usage: Type `use item`, the game lists your inventory, then enter the name of the item to use (e.g., `Matches`, `Password-A`).
	- Behavior: Each item has a class-defined `use()` behavior. Some uses may end the game (victory), move the Box Girl, or alter the map.


2. Inventory & Item types (what they do)

- Password (class: `Password`)
	- Purpose: Pieces of a code needed to open the Hidden Exit.
	- How to use: `use item` → enter password name (or one password instance). If you have 3 Passwords and you are in the Hidden Exit room, using a Password triggers ESCAPE victory.

- Box Girl Weakness (class: `BoxGirlWeakness`)
	- Purpose: Items used to exorcise the Box Girl.
	- How to use: `use item` → if you have at least 2 weaknesses and are in Miss Mary's Room or the Corpse Location, using one will trigger EXORCISM victory.

- Counter Item (class: `CounterItem`)
	- Purpose: A consumable that temporarily wards off or frightens the Box Girl.
	- How to use: `use item` → the item is removed from inventory, sets a short deterrent state, and if Box Girl shares your room, she will be forced to move.

- Manuscript (class: `Manuscript`)
	- Purpose: Unlocks skills for the Box Girl when found/opened; cannot be used by the player.
	- Behavior: Opening a box with a Manuscript auto-activates the corresponding skill, which may immediately affect boxes, movement, or cause the Box Girl to act.

- Empty Box marker
	- Purpose: Represents boxes with no useful items. The opening shows, "This box is empty."


3. Room & Map notes

- Rooms have features (stored in `featureItem`) such as:
	- "Carved door (Miss Mary's Location)" — a hint for Miss Mary's location
	- "Seeping Walls (Corpse Location)" — a hint for the Corpse location
	- "Rocking Chair (Hidden Exit)" — is used to identify the Hidden Exit room

- Movement directions are the connection keys in each room (`NORTH`, `SOUTH`, `EAST`, `WEST`, `UP`, `DOWN`).


4. Victory conditions and tips

There are two ways to win the game:

1) ESCAPE VICTORY
	 - Requirement: Collect 3 `Password` items and be at the Hidden Exit room (the room whose feature contains "Hidden Exit").
	 - How to trigger: Use any of the 3 `Password` items that you have collected while in that room.

2) EXORCISM VICTORY
	 - Requirement: Collect at least 2 `BoxGirlWeakness` items and be in a special room (Miss Mary's room or the Corpse location).
	 - How to trigger: Use any of 2 (or more) `BoxGirlWeakness` items that you have collected while in that room.


5. Example play session

1) Game prints starting room: "You are currently in the First Floor Hall."
2) Player types: `move` then `NORTH` → player moves rooms.
3) Player types: `open box` → chooses a box ID and may receive an item.
4) Player types: `use item` → chooses `Rope` (CounterItem) to expel the Box Girl from your current room.


6. Challenges to Win

- Limited item discovery
	- Challenge: Items (Passwords and Weaknesses) are scattered in boxes across many rooms; you may open many empty boxes before finding useful items.
	- Tip: Systematically explore rooms and open boxes in each room before moving on.

- Box Girl encounters and hiding
	- Challenge: Opening the boxe that contains the Box Girl will immidiately end the game since she kills you.
	- Tip: There are signs in the text that indicates the Box Girl is in the same room as you. If you suspect she may be hiding, opening many boxes in the same room increases the chance of uncovering her.

- Locating the Hidden Exit
	- Challenge: The Hidden Exit room is identified only by a feature hint in the room description (e.g., "Rocking Chair (Hidden Exit)"), and you will only see the room description when you enter that room.
	- Tip: Pay attention to feature text when entering rooms.

- Finding Miss Mary's room or the Corpse location
	- Challenge: Exorcism requires being in a specific special room marked by features; these rooms may be easy to miss.
	- Tip: Read room feature text carefully and backtrack through halls if you suspect a special room is nearby. Pay attention to the collected weakness items as you explore.


# Game World Layout

This game uses a procedurally generated map each time the player starts a new session. The generation rules and fixed elements are documented below so reviewers can verify victory conditions even though the specific map layout varies across runs.

1. Fixed elements
- Three central halls always exist: `Basement`, `First Floor Hall`, and `Second Floor Hall`.
	- These three halls are the only locations that support vertical movement (UP/DOWN).
	- The player always starts in `First Floor Hall`.

2. Randomized elements
- Each playthrough randomly places several rooms around the central halls. Specifically:
	- First and Second floors each have three rooms attached to their hall in distinct horizontal directions (NORTH/SOUTH/EAST/WEST). The room names, features, and exact directions are randomized.
	- Each room contains multiple boxes. Items (Passwords, Weaknesses, CounterItems, Manuscripts, and Empty Box markers) are randomly distributed across all boxes.

3. Movement rules
- Horizontal movement (NORTH/SOUTH/EAST/WEST) follows the room connections that are generated for that playthrough. A direction may be unavailable from a given room; attempting to move that way will report "There is no path in that direction.".
- Vertical movement (UP/DOWN) is only possible between the three halls: Basement <-> First Floor Hall <-> Second Floor Hall. Other rooms do not support UP/DOWN.

4. Special behaviors
- Manuscripts: Opening a manuscript will immediately activate a Box Girl skill (manuscripts disappear when activated).
- Box Girl hiding: The Box Girl may hide inside an unopened box. The player is only caught if they open the box that contains the Box Girl.

5. Victory conditions (map-related)
- Escape Victory: Use three Password items at the Hidden Exit room. The Hidden Exit is indicated by the room's feature (e.g. "Rocking Chair (Hidden Exit)").
- Exorcism Victory: Use at least two BoxGirlWeakness items while in the Miss Mary's room or the Corpse location (these rooms are marked by feature text or room names in the generated map).

6. Debugging and reproducibility
- The game prints a small ASCII map after generation for debugging (grid coordinates and a legend showing room names and features). Although the layout is randomized, the presence of fixed hall locations and feature markers ensures that reviewers can verify win conditions reliably.
- If reproducible runs are required, a random seed can be added to the generator to produce identical layouts across runs.



