package com.example.learningjavafx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class Cherry implements GameObject {
    @Override
    public void spawn() {
        // Logic for spawning a cherry
        HelloController helloController = new HelloController();

            // Fixed X-coordinate for cherry spawn
            double gap = helloController.getRectangle2().getX() - (helloController.getRectangle().getX() + helloController.getRectangle().getWidth());
            if (gap > 100) {
                Random random = new Random();
                double cherryX = 600 + (random.nextDouble(0, helloController.getRectangle().getWidth()));

                // Fixed Y-coordinate for cherry spawn
                double cherryY = 600 + Math.random() * 100;

                helloController.setCherry(new ImageView(new Image(getClass().getResourceAsStream("/com/example/learningjavafx/cherry.png"))));
                helloController.getCherry().setFitWidth(50);
                helloController.getCherry().setFitHeight(50);
                helloController.getCherry().setId("Cherry");
                helloController.getCherry().setLayoutX(cherryX);
                helloController.getCherry().setLayoutY(cherryY);
                helloController.getRoot().getChildren().add(helloController.getCherry());
            }


    }
}
