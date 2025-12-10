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

# 3. Bidirectional Hiding Mechanism Between BoxGirl and Box

To support the mechanic where a boxgirl may “hide” inside a box, a bidirectional relationship is kept in order to maintain consistent state:
The `Box` maintains a reference to its `hiddenOccupant` so it can instantly tell when the player opens it.
It is through the `hiddenInBox` that the state transition of `BoxGirl`, say leaving the box, getting revealed, or defeated, is possible.
This design avoids inconsistent states, like the box thinking it contains the boxgirl while in fact the boxgirl thinks not, and guarantees predictable stable behavior.
4). Randomized Map with Fixed Key Rooms (Hall, Basement, Hidden Exit) The map is partially randomized, but places certain key rooms deterministically in order to balance replayability with guaranteed quest completeness. Random elements: add variety and replay value. Fixed-key rooms: All necessary pathways and activities should remain accessible. Ensuring hall connectivity: improves navigability, prevents soft-locks. This will keep the generation logic simple, but eliminate the possibility of generating unsolvable maps.