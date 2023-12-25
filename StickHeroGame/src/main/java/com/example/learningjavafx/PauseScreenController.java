// PauseScreenController.java
package com.example.learningjavafx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PauseScreenController {
    private HelloController gameController;

    @FXML
    private Label scoreField;
    public void setGameController(HelloController gameController) {
        this.gameController = gameController;
    }

    @FXML
    private void resumeGame(ActionEvent event) {
        // Code to resume the game
        // You may need to keep track of the game state and continue from where it was paused
        // For example, you can close the Pause screen and resume the game loop.
        if (gameController != null) {
            gameController.resumeGame();
        }
    }

    @FXML
    public void updateScoreField(int score) {
        scoreField.setText("Your Score: " + score);
    }

    @FXML
    public void exitGame(ActionEvent event) {
        // Code to exit the game
        HelloController helloController = new HelloController();
        Player p = helloController.getPlayer();

        // Update player details
        if (p.getHighestScore() < helloController.getSuccessfulMovements()) {
            p.setHighestScore(helloController.getSuccessfulMovements());
        } else{
            p.setLastScore(p.getLastScore());
        }

        // Save player details to a text file
        savePlayerDetails(p);

        // Close the application
        Platform.exit(); // This will close the JavaFX application
        System.exit(0); // This will terminate the entire Java application
    }

    private void savePlayerDetails(Player player) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("playerdetails.txt"))) {
            // Write player details to the file
            writer.write("Highest Score: " + player.getHighestScore());
            writer.newLine();
            writer.write("Last Score: " + player.getLastScore());
            writer.newLine();
            writer.write("Number of Cherries: " + player.getNumberOfCherries());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
