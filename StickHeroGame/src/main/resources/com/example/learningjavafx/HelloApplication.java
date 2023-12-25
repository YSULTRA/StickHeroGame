package com.example.learningjavafx;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class HelloApplication extends Application {

    private Stage stage;
    private Scene scene;
    private AnchorPane root;
    @FXML
//    private ImageView hero;

    private double x;

    private Line line1;
    private Timeline growthTimeline;
    private RotateTransition rotationTransition;
    private boolean isGrowing = false;
    private boolean isFirstTime = true;


    public static boolean LineMovementDone = false;




    public static void main(String[] args) {
        launch();
    }
    private double generateRandomWidth() {
        // Generate a random width between 100 and 280
        Random random = new Random();
        return random.nextDouble(100,280);
    }
    public void Repeat(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/learningjavafx/Imageview.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        Image hero = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/learningjavafx/hero.png")));
        ImageView imageView = new ImageView(hero);
        imageView.setFitWidth(140);
        imageView.setFitHeight(150);
        imageView.setLayoutX(463);
        imageView.setLayoutY(450);
        root.getChildren().add(imageView);

        line1 = new Line();
        line1.setStartX(600);  // Set starting X coordinate
        double startX = line1.getStartX();
        line1.setStartY(600);  // Set starting Y coordinate
        double startY = line1.getStartY();
        line1.setEndX(600);    // Set ending X coordinate (same as starting X to make it vertical initially)
        line1.setEndY(600);    // Set ending Y coordinate
        line1.setStrokeWidth(10);
        line1.setStroke(Color.DARKVIOLET);
        line1.autosize();

        Rectangle rectangle = new Rectangle();
        rectangle.setX(400);
        rectangle.setY(600);
        rectangle.setWidth(200);
        rectangle.setHeight(400);
        rectangle.setFill(Color.BLACK);
        rectangle.autosize();

        Rectangle rectangle2 = new Rectangle();
        rectangle2.setX(1000);
        rectangle2.setY(600);
        rectangle2.setWidth(generateRandomWidth());
        rectangle2.setHeight(400);
        rectangle2.setFill(Color.GREEN);
        rectangle2.autosize();

        root.getChildren().add(line1);
        root.getChildren().add(rectangle);
        root.getChildren().add(rectangle2);

        scene = new Scene(root);

        growthTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(line1.endYProperty(), line1.getEndY())),
                new KeyFrame(Duration.seconds(5000), new KeyValue(line1.endYProperty(), line1.getEndY() - 1000000))
        );

        rotationTransition = new RotateTransition(Duration.seconds(1), line1);
        rotationTransition.setAxis(Rotate.Z_AXIS);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.W) {
                isGrowing = true;
                growthTimeline.setCycleCount(Animation.INDEFINITE);
                growthTimeline.play();
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.W) {
                isGrowing = false;
                growthTimeline.stop();
                growthTimeline.setCycleCount(1);


                double pivotX = startX;
                double pivotY = startY + line1.getEndY();
                Rotate rotate = new Rotate(90, startX, startY);
                line1.getTransforms().add(rotate);


//                Rotate reset = new Rotate(270, startX, startY);
//                line1.getTransforms().add(reset);

                rotationTransition.setCycleCount(1);
                rotationTransition.setOnFinished(rotationEvent -> {
                    double lineEndX = line1.getEndX();
                    double lineEndY = line1.getEndY();

                    LineMovementDone = true;

                    double rotatedX = Math.cos(Math.toRadians(90)) * (lineEndX - startX) - Math.sin(Math.toRadians(90)) * (lineEndY - startY) + startX;
                    double rotatedY = Math.sin(Math.toRadians(90)) * (lineEndX - startX) + Math.cos(Math.toRadians(90)) * (lineEndY - startY) + startY;

                    double imageViewX = rotatedX - imageView.getFitWidth();
                    double imageViewY = rotatedY - imageView.getFitHeight();

                    double layoutXDiff = imageViewX - imageView.getLayoutX();

                    TranslateTransition translate = new TranslateTransition(Duration.seconds(1), imageView);
                    translate.setToX(imageView.getTranslateX() + layoutXDiff + 100);
                    translate.setInterpolator(Interpolator.EASE_BOTH);
                    translate.play();

                    translate.setOnFinished(translateEvent -> {
                        if (imageView.getBoundsInParent().intersects(rectangle2.getBoundsInParent())) {
//                            root.getChildren().remove(line1);
//                            line1.setVisible(false);
//
//                            FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), imageView);
//                            fadeOutTransition.setToValue(0);
//
//                            fadeOutTransition.setOnFinished(fadeOutEvent -> {
//                                TranslateTransition rectangleMoveTransition = new TranslateTransition(Duration.seconds(0.5), rectangle);
//                                rectangleMoveTransition.setToX(rectangle.getX() - 500);
//
//                                rectangleMoveTransition.setOnFinished(moveEvent -> {
//                                    root.getChildren().remove(rectangle);
//
//                                    ImageView imageView1 = new ImageView(hero);
//                                    imageView1.setFitWidth(140);
//                                    imageView1.setFitHeight(150);
//                                    imageView1.setLayoutX(463);
//                                    imageView1.setLayoutY(450);
//                                    imageView1.setOpacity(0);
//
//                                    rectangle2.setX(rectangle.getX());
//                                    rectangle2.setY(rectangle.getY());
//
//                                    Rectangle rectangleNext = new Rectangle();
//                                    rectangleNext.setLayoutY(600);
//                                    rectangleNext.setLayoutX(new Random().nextDouble(800,1500));
//                                    rectangleNext.setWidth(200);
//                                    rectangleNext.setHeight(400);
//                                    root.getChildren().add(imageView1);
//                                    root.getChildren().add(rectangleNext);
//                                    FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(1), imageView1);
//                                    fadeInTransition.setToValue(1);
//                                    fadeInTransition.play();
//                                    Rotate reset = new Rotate(270, startX, startY);
//                                    line1.getTransforms().add(reset);
//                                    line1.setEndX(600);
//                                    line1.setEndY(600);
//                                    line1.setVisible(true);
//
//                                });
//
//                                rectangleMoveTransition.play();
//                            });
//
//                            fadeOutTransition.play();

                        } else {
                            TranslateTransition fallTransition = new TranslateTransition(Duration.seconds(1), imageView);
                            fallTransition.setToY(imageView.getTranslateY() + 500);
                            fallTransition.setInterpolator(Interpolator.EASE_BOTH);
                            fallTransition.play();

                            Rotate fallRotate = new Rotate(90, startX, startY);
                            line1.getTransforms().add(fallRotate);
                        }

                    });
                });
                rotationTransition.play();
            }
        });

        line1.requestFocus();
        stage.setScene(scene);
        stage.show();


    }




    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/learningjavafx/EntryScreen.fxml"));
        Parent root =loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
