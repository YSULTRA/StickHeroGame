package com.example.learningjavafx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

// Hero.java
public class Hero implements GameObject {

    HelloController helloController = new HelloController();
    @Override
    public void spawn() {
        // Logic for spawning a hero
        Image hero = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/learningjavafx/hero.png")));
        helloController.setImageView(new ImageView(hero));
        helloController.getImageView().setFitWidth(140);
        helloController.getImageView().setFitHeight(150);
        helloController.getImageView().setLayoutX(463);
        helloController.getImageView().setLayoutY(450);
        helloController.getImageView().setId("Hero");
        helloController.getRoot().getChildren().add(helloController.getImageView());

    }
}
