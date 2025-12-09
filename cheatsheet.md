This cheatsheet lists the commands for players, important item types, and victory conditions and special rooms.


1. Commands for Players

Enter one of the commands below on your turn.

- move
	- Description: Move to a connected room.
	- Usage: Type `move` then enter a direction when prompted (e.g. `NORTH`, `SOUTH`, `UP`, `DOWN`, `BASEMENT`, `UPPER`).
	- Example:
		- move
		- NORTH

- open box
	- Description: Open an unopened box in the current room.
	- Usage: Type `open box`, see the list of unopened box IDs, then enter the numeric ID to open.
	- Notes: Some boxes contain Items (Passwords, Weaknesses, Manuscripts, CounterItems). Manuscripts auto-activate the Box Girl's skill.

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

- Movement directions are the connection keys in each room (`NORTH`, `SOUTH`, `EAST`, `WEST`, `UP`, `DOWN`, `BASEMENT`, `UPPER`, etc.).


4. Victory conditions and tips

There are two ways to win the game:

1) ESCAPE VICTORY
	 - Requirement: Collect 3 `Password` items and be at the Hidden Exit room (the room whose feature contains "Hidden Exit").
	 - How to trigger: Use a `Password` while in that room. The `Password.use()` method checks `player.countItemOfType(Password.class)` and `player.getCurrentRoom().getFeatureItem().contains("Hidden Exit")`.

2) EXORCISM VICTORY
	 - Requirement: Collect at least 2 `BoxGirlWeakness` items and be in a special room (Miss Mary's room or the Corpse location).
	 - How to trigger: Use a `BoxGirlWeakness` while in a room where `room.getName()` contains "Miss Mary" or "Corpse". The `BoxGirlWeakness.use()` method performs exactly this check.


5. Example play session (short)

1) Game prints starting room: "You are currently in the First Floor Hall."
2) Player types: `move` then `NORTH` → player moves rooms.
3) Player types: `open box` → chooses a box ID and may receive an item.
4) Player types: `use item` → chooses `Matches` (CounterItem) to ward off the Box Girl.
