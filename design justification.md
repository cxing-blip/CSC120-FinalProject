# 1. Objectives and Overall Structure
The objective of this project is to create a text-based adventure game engine that is extensible and easy to maintain. The overall architecture is modular, in that each individual component has a single, well-defined responsibility to reduce coupling and simplify any future extensions.

Core modules and their responsibilities are:
`Game`: This handles the main game loop, input parsing, and player interaction.
GameMap: It is responsible for the generation of the map, distribution of the layout, and room allocation.
`Room`: Shall represent a location entity, defining its connections: up, down, left, and right.
`Item` abstract class and its subclasses contain the logic for different item behavior.
BoxGirl: Responsible for maintaining enemy state, hiding behavior, and combat logic.

Applying the Single-Responsibility Principle to every class will give each class clear and concise structures, greatly reducing the difficulties in debugging and chances of errors.

# 2. Major Design Decisions and Rationales

1). Abstract Item System with Inheritance (`Item` → concrete item types)
Since these items show very different behaviours, the design relies on an abstract class `Item` that enforces a common interface, whereas every specific item class (`Password`, `CounterItem`, `Manuscript`, .) provides its own implementation of the method `use()`.
The advantages of this design include:
High extensibility: New items can be added simply by creating a new subclass

Low Coupling: Logic is not concentrated in `Game` or `Room`
Improved readability and maintenance: The behavior of each item is self-contained, easily debuggable

2). Mapping Structure with Hybrid of Grid (`mapGrid`) + Name Index (`allRooms`)
The map is implemented using a 2D grid for spatial layout, while internally maintaining a dictionary that maps room names to their instances for efficient lookup during gameplay.
Rationale:
Grid (`mapGrid`):
Enables ASCII visualization
Adjacency logic is straightforward and intuitive.
Simplifies the generation of randomized maps.
Name index (`allRooms`):
Allows direct access to special rooms at initialization time

Avoids repeatedly scanning the entire map
Facilitates special event triggers, such as hidden exit
Furthermore, regular rooms are placed around the central hall, and the generation process is orderly; hence, it reduces the possibility of errors and maintains the simplicity and stability of the whole map.

3). Bidirectional Hiding Mechanism Between BoxGirl and Box

To support the mechanic where a boxgirl may “hide” inside a box, a bidirectional relationship is kept in order to maintain consistent state:
The `Box` maintains a reference to its `hiddenOccupant` so it can instantly tell when the player opens it.
It is through the `hiddenInBox` that the state transition of `BoxGirl`, say leaving the box, getting revealed, or defeated, is possible.
This design avoids inconsistent states, like the box thinking it contains the boxgirl while in fact the boxgirl thinks not, and guarantees predictable stable behavior.

4). Randomized Map with Fixed Key Rooms (Hall, Basement, Hidden Exit) The map is partially randomized, but places certain key rooms deterministically in order to balance replayability with guaranteed quest completeness. Random elements: add variety and replay value. Fixed-key rooms: All necessary pathways and activities should remain accessible. Ensuring hall connectivity: improves navigability, prevents soft-locks. This will keep the generation logic simple, but eliminate the possibility of generating unsolvable maps.

# 3. Alternative Designs Consideration
One alternative I considered was to make box contents permanently mutable from either side: that is, to allow the player to "drop" items into a closed box (which would automatically open) or to allow the Box Girl and other systems to directly swap items between boxes and player inventory. This would simplify some code paths because there would be fewer explicit "open/close" state checks.

Reasons I decided against the fully-mutable approach:
- Predictability and fairness: Automatically opening a closed box when the player drops an item could accidentally reveal the Box Girl and end the game in a way that feels unfair. Keeping an explicit requirement that the box must already be opened for a player to put items in maintains a clear risk/choice trade-off.
- Simplicity of state transitions: Requiring the player to place items only into already-opened boxes keeps the open/closed state transitions explicit and reduces edge cases where box closing/opening could conflict with hidden occupants or manuscript-triggered effects.
- Clarity: The current approach maps directly to a simple mental model (open a box → take or put items → box can be closed), which is easier for players to reason about and for reviewers to test.

If the project were extended further (e.g., with more complex inventory interactions or a multiplayer mode), the fully-mutable approach might be revisited with additional safety checks (confirmation prompts, time delays, or locks) to avoid accidental deaths; for the current single-player design the explicit-open requirement is the more robust choice.