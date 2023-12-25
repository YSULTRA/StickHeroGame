package com.example.learningjavafx;

import javafx.application.Platform;

import java.io.IOException;

import static javafx.application.Application.launch;

public class GameLoop extends Thread {

    private HelloApplication game;

    public GameLoop(HelloApplication game) {
        this.game = game;
    }

    @Override
    public void run() {
        while (true) {

            // Check if the line movement is done
            if (HelloApplication.LineMovementDone) {
                // Reset the game
                resetGame();

                // Sleep to control the loop speed
                try {
                    Thread.sleep(2000); // Sleep for 2 seconds before starting the next level
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Sleep to control the loop speed
            try {
                Thread.sleep(10); // Adjust the sleep time as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void resetGame() {
        // Your game reset logic here
        // For example, reload the scene, reset variables, etc.
        Platform.runLater(() -> {
            try {
                game.Repeat(null); // Call your method to reset the game
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        // Assuming you start the game loop in your main method
        HelloApplication game = new HelloApplication();
        GameLoop gameLoop = new GameLoop(game);
        gameLoop.start();

    }
}

