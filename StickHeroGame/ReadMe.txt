Members     Roll No
Yash Singh  2022589
Yogesh Kumar  2022593

Here are step-by-step instructions on how to run the code in IntelliJ IDEA:

Java and JavaFx Should be Installed in your system.

1. **Extract the ZIP:**
   - Extract the contents of the ZIP file to a location of your choice.

2. **Open as IntelliJ Project:**
   - Open IntelliJ IDEA.
   - From the main menu, select `File` > `Open...`.
   - Navigate to the extracted folder, select it, and click `Open It as Intellij Project `.

3. **Locate Main Class:**
   - In the `Project Explorer` (usually on the left side), navigate to `src/main/java/com/example/learningjavafx/`.
   - Right-click on the `HelloController.java` file.

4. **Run the Main Method:**
   - In the context menu, find and click on `Run 'HelloController.main()'`.

5. **Play the Game:**
   - The game window should appear, and you can interact with it using the controls mentioned in Readme.
   - Enjoy playing the game!


**Design Patterns Used**
    Factory Design Pattern (used in GameObjectFactory.Java)
    Singleton Design Pattern (used in Player.java)

**Junit Test Used**
    HelloControllerTest.Java

**Thread Used**
    HelloController.Java --> Main method;

Sure, here's a basic control guide for the game:

**Game Controls:**

Please Read Controls Guide Before Playing:

- *Space Bar:* Press and hold to make the Stick grow. Release to stop it from growing. Do not immediately press space bar again. Allow the animations to complete.
- **Down Arrow Key:** Flip the hero upside down around the base while pressed. Release to reset the hero's orientation.
- **Escape Key:** Pause the game and access the pause menu.

*Objective:*
- Grow the stick to reach the highest possible score by avoiding falling off the stick and collecting cherries.

**Scoring:**
- Gain points by successfully passing each levels.
- Collect cherries to earn additional points.

**Obstacles:**
Black rectangles will appear on the screen. Unable to Reach Next Rectangle and falling downward reduces your lives and decrease your score.
**Cherries:**
- Collect cherries to increase your score.

**Life:**
- You start with a certain number of lives (hearts). Unable to Reach Next Level and falling downward reduces your lives and decrease your score.
- If you run out of lives, the game will end.

**Special Actions:**
- Pressing the down arrow key will flip the hero upside down, changing its interaction with the environment.

**Pause Menu:**
- Press the Escape key to access the pause menu.
- In the pause menu, you can resume the game or exit to the main menu.

**Game Over:**
- The game ends when you run out of lives.
- A game over screen will display your final score.
- You can choose to exit the game or restart.

Enjoy playing the game!

