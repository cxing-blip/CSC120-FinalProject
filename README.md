# CSC120-FinalProject

## Deliverables:
 - Your final codebase
 - Your revised annotated architecture diagram
 - Design justification (including a brief discussion of at least one alternative you considered)
 - A map of your game's layout (if applicable)
 - `cheatsheet.md`
 - Completed `rubric.md`
  
## Additional Reflection Questions

 - What was your **overall approach** to tackling this project?
    `We are creating a Java-based single-player text game inspired by a Japanese card game “箱女”. The player is trapped in a house, and their primary goal is to escape. There are three ways to escape, which will be told to the player at the beginning of the game, but the specific clues about items and actions that the player needs to escape can only be found out by opening boxes in rooms in the house. The player's main action is to explore multiple rooms and open boxes in them that may contain useful items, clues, or deadly traps. A hostile entity, Box Girl, will be in any random empty box on the same map. She will be able to kill the player if the player opens the box that contains the Box Girl, and she can also implement some skills that hinder the player from exploration.`

 - What **new thing(s)** did you learn / figure out in completing this project?
    `Iteration: for the boxgirl's random move method, we found it was very hard to garantee the boxgirl will stay in box(very weird but very tricky problem). We found out that iteration could be used for this situation to check adn move to a random room with un-opened boxes.`
    `2D Array: since the map is randomly generated everytime the game restart, it will be hard to find a right way to exit the villa or defeat the boxgirl. Finally, we decided to use a 2D Array to generate the map and show it to the player, giving them a general idea for the game`

 - Is there anything that you wish you had **implemented differently**?
    `There's two things different with out orginal thoughts. The first is the decision of the boxgirl, first we thought to completely follow the card game's construction, which boxgirl will use it's skill only once when it makes the most benefits for her. However, as we wrote it, we found there's so many conditions to consider since decisions made by human is very complicated. Therefore, we finally used a random number to decide whether skills will be used in this turn. The second thing we change is the boxgirl's movement. In the card game, boxgirl's player will probably move towards the visitor player to make more chance to kill them, but it's also very complicated and we haven't found an easy way to achieve it, so we used iteration method to let boxgirl decide the box/direction that she's going to. Although the unlimited skill using chance isn't benefited player than the orginal form, the random moving boxgirl offered more chance for player's to survive. So this is a compromise for this single player game.`

 - If you had **unlimited time**, what additional features would you implement?
    `We would want to reduce the difference between our game and the orginal card game as much as possible, such as iterate the decision made by boxgirl to make it's depends more on the player's action instead of the random number generated. This will definately make more fun and playability. Furthermore, we might want to add a map and controlers that shown by the data structure PPT that professor offered, to make it more interactable and show information in a more efficient way.`

 - What was the most helpful **piece of feedback** you received while working on your project? Who gave it to you?
    `We thought the most helpful feedback is the win rate of the game. Soon as we thought we completed our porject and brought it to class, none of our classmates/instructor succeed in this game. This data feedback strongly suggested that there's some problems inside our game affecting the win rate. To adjust this, we looked it again today's noontime and found it was caused by the birth place of the boxgirl. We soonly fixed it to reduce the rate of player and boxgirl's meeting in the first several turns after the game begins.`

 - If you could go back in time and give your past self some **advice** about this project, what hints would you give?
    `Do not over estimate our skills in programming. We thought we could at least cover 80% of the card game's feature, but it really took us a long time to figure out the appropriate method to programme it. Even like that, we changed a lot of things at last to make it easier to approach.`

 - _If you worked with a team:_ please comment on how your **team dynamics** influenced your experience working on this project.
  `We are close friend outside of the class so most of the things are done together. We constructed the idea first, actually inspired by a novel. Then build a structure of the game to firstly make it able to run. In the third week we changed many method to make it more playable and fit in the logic. The general structure were mostly built by Yuhan, and Lois did many revisions.`
