package com.example.learningjavafx;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import junit.textui.TestRunner;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.io.Serializable;

class Tester implements Runnable{

    @Override
    public void run() {
        Result result = JUnitCore.runClasses(com.example.learningjavafx.HelloControllerTest.class);
        for(Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println("All Test Passed "+result.wasSuccessful()    );
    }
}


public class HelloController extends Application implements Runnable {

    private static final String PLAYER_DETAILS_FILE = "playerdetails.txt";

    @FXML
    private Button playbutton; // Add this line to reference the FXML button
    @FXML
    private Label scoreLabel;

    private Player player = Player.getInstance();;

    @FXML
    private Label Life;

    public ImageView getCherry() {
        return cherry;
    }

    public void setCherry(ImageView cherry) {
        this.cherry = cherry;
    }

    private ImageView cherry;

    public int getNoOfLife() {
        return noOfLife;
    }

    private int noOfLife = 3;
    private Stage stage;
    private Scene scene;

    public AnchorPane getRoot() {
        return root;
    }

    public void setRoot(AnchorPane root) {
        this.root = root;
    }

    private AnchorPane root;
    private Line line1;
    private Timeline growthTimeline;

    private ScheduledExecutorService gameLoopExecutor; // Rename to gameLoopExecutor

    private RotateTransition rotationTransition;
    private boolean isGrowing = false;
    private boolean isFirstTime = true;
    private boolean heroMovedOrFallen = false;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    private ImageView imageView;
    private boolean isFirstSpacePress = true;

    public Rectangle getRectangle2() {
        return rectangle2;
    }

    public void setRectangle2(Rectangle rectangle2) {
        this.rectangle2 = rectangle2;
    }

    private Rectangle rectangle2;

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    private Rectangle rectangle;

    private FadeTransition fadeOutTransition;
    private FadeTransition fadeInTransition;

    private Boolean isExitStatus = false;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getSuccessfulMovements() {
        return successfulMovements;
    }

    public void setSuccessfulMovements(int successfulMovements) {
        this.successfulMovements = successfulMovements;
    }

    private int successfulMovements = 0;

    private Stage pauseStage;
    private Stage exitStage;
    private Scene pauseScene;
    private Scene exitScene;

    private boolean secondPress = false;
    public static boolean LineMovementDone = false;

    public static void main(String[] args) throws InterruptedException {
        Tester tester = new Tester();
        Thread T1 = new Thread(tester);
        T1.start();
        T1.join();
        System.out.println("Thread Tester Completed All The Test !");
        launch();
    }

    double generateRandomWidth() {
        // Generate a random width between 100 and 280
        Random random = new Random();
        return random.nextDouble(85, 280);
    }

    public void spawnRectangle() {
        rectangle2 = new Rectangle();
        rectangle2.setX(new Random().nextDouble(650, 1300));
        rectangle2.setY(600);
        rectangle2.setWidth(generateRandomWidth());
        rectangle2.setHeight(400);
        rectangle2.setFill(Color.BLACK);
        rectangle2.autosize();

        root.getChildren().add(rectangle2);
    }



    public void initializeGame() {
        spawnHero();
        spawnRectangle();
        initializeAnimations();
//        initializeScoreLabel();
    }

    public void initializeAnimations() {

        fadeOutTransition = new FadeTransition(Duration.seconds(1), imageView);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);

        fadeInTransition = new FadeTransition(Duration.seconds(1), imageView);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);

        fadeOutTransition.setOnFinished(fadeInEvent -> {
            // After the fade in, reset the game state or perform other actions
            try {
                resetGameState();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        growthTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(line1.endYProperty(), line1.getEndY())),
                new KeyFrame(Duration.seconds(5000), new KeyValue(line1.endYProperty(), line1.getEndY() - 1000000))
        );

        rotationTransition = new RotateTransition(Duration.seconds(1), line1);
        rotationTransition.setAxis(Rotate.Z_AXIS);

        rotationTransition.setOnFinished(rotationEvent -> {
            handleRotationFinished();
        });

        growthTimeline.setOnFinished(growthEvent -> {
            handleGrowthFinished();
        });
    }
    public void initializeScoreLabel() {
        // Create a label for the score
        scoreLabel = new Label("Score: " + successfulMovements);
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setFont(new Font(20));

        // Add label to AnchorPane
        root.getChildren().add(scoreLabel);
        AnchorPane.setTopAnchor(scoreLabel, 10.0);
        AnchorPane.setRightAnchor(scoreLabel, 10.0);
    }


    public void resetGrowthTimeline() {
        growthTimeline.pause();
        growthTimeline.setCycleCount(1);
        growthTimeline.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO, new KeyValue(line1.endYProperty(), line1.getEndY())),
                new KeyFrame(Duration.seconds(5000), new KeyValue(line1.endYProperty(), line1.getEndY() - 1000000))
        );
    }
    void updateScoreLabel(int score) {
        int dummyScore = successfulMovements + score;  // Calculate the updated score

        if (dummyScore >= 0) {
            successfulMovements = dummyScore;  // Update successfulMovements with the new score
            player.setLastScore(successfulMovements);  // Set last score to the updated score

            if (dummyScore > player.getHighestScore()) {
                player.setHighestScore(dummyScore);  // Update highest score if the new score is higher
            }
        }

        // Increment the score and update the label text
        scoreLabel.setText("Score: " + successfulMovements);
    }

    private void loadPlayerDetails() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(PLAYER_DETAILS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                processPlayerDetails(line);
            }
            reader.close();
        } catch (IOException e) {
            // Handle exception (e.g., file not found, invalid format)
            e.printStackTrace();
        }
    }

    void processPlayerDetails(String line) {
        if (line.startsWith("Highest Score:")) {
            int highestScore = Integer.parseInt(line.split(":")[1].trim());
            player.setHighestScore(highestScore);
        }
    }

    public void handleGrowthFinished() {
        isGrowing = false;
        resetGrowthTimeline();
        growthTimeline.pause();
        growthTimeline.setCycleCount(1);

        double pivotX = line1.getStartX();
        double pivotY = line1.getStartY() + line1.getEndY();
        Rotate rotate = new Rotate(90, 600, 600);
        line1.getTransforms().add(rotate);

        rotationTransition.setCycleCount(1);
        rotationTransition.play();


        // Add the following lines to reset transformations
//        line1.getTransforms().clear();;
    }

    public void handleRotationFinished() {
        double lineEndX = line1.getEndX();
        double lineEndY = line1.getEndY();

        LineMovementDone = true;

        double rotatedX = Math.cos(Math.toRadians(90)) * (lineEndX - line1.getStartX()) - Math.sin(Math.toRadians(90)) * (lineEndY - line1.getStartY()) + line1.getStartX();
        double rotatedY = Math.sin(Math.toRadians(90)) * (lineEndX - line1.getStartX()) + Math.cos(Math.toRadians(90)) * (lineEndY - line1.getStartY()) + line1.getStartY();

        double imageViewX = rotatedX - imageView.getFitWidth();
        double imageViewY = rotatedY - imageView.getFitHeight();

        double layoutXDiff = imageViewX - imageView.getLayoutX();

        TranslateTransition translate = new TranslateTransition(Duration.seconds(1), imageView);
        translate.setToX(imageView.getTranslateX() + layoutXDiff + 100);
        translate.setInterpolator(Interpolator.EASE_BOTH);
        translate.setOnFinished(translateEvent -> {
            try {
                handleTranslateFinished();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        translate.play();

    }
    private boolean cherryCollisionDuringAnimation = false;

    public void handleCollisionDuringAnimation() {
        // Check for collision during animation
        if (intersectsInverted(imageView, cherry)) {
            // The imageView intersects with the cherry during animation
            cherryCollisionDuringAnimation = true;
            // Perform other actions if needed
        }
        // Continue the animation or perform other actions
    }
    private boolean intersectsInverted(Node node1, Node node2) {
        Bounds bounds1 = node1.localToScene(node1.getBoundsInLocal());
        Bounds bounds2 = node2.localToScene(node2.getBoundsInLocal());

        return bounds1.intersects(bounds2);
    }
    public void handleTranslateFinished() throws IOException {
        if (cherryCollisionDuringAnimation) {
            // The imageView intersects with the cherry, perform your actions here
            // For example, remove the cherry and increase the score
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.25), cherry);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(fadeEvent -> {
                root.getChildren().remove(cherry);
//                spawnCherry(); // Respawn a new cherry
            });
            fadeOut.play();

            // Increase the score by +2
            updateScoreLabel(2);
            cherryCollisionDuringAnimation = false;
            player.setNumberOfCherries(player.getNumberOfCherries()+1);

        }


        if (imageView.getBoundsInParent().intersects(rectangle2.getBoundsInParent())) {
            line1.setVisible(false);
            // Handle collision with rectangle2
//            spawnRectangle();
            fadeOutTransition.play();
            Rotate fallRotate = new Rotate(90, 600, 600);
            line1.getTransforms().add(fallRotate);
            heroMovedOrFallen = true;
//            growthTimeline.setCycleCount();
            updateScoreLabel(1);

        } else {
            // Handle falling
            TranslateTransition fallTransition = new TranslateTransition(Duration.seconds(1), imageView);
            fallTransition.setToY(imageView.getTranslateY() + 500);
            fallTransition.setInterpolator(Interpolator.EASE_BOTH);
            fallTransition.setOnFinished(fallEvent -> {
                try {
                    handleFallFinished();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            updateScoreLabel(-1);
            Rotate fall = new Rotate(90, 600, 600);
            line1.getTransforms().add(fall);
            fallTransition.play();

            if(noOfLife <1){
                isExitStatus = true;
            }else {
                noOfLife-=1;
            }

            fallTransition.setOnFinished(event -> {
                this.stage.close();
            });


        }

    }

    public void handleFallFinished() throws IOException {
        // Handle falling finished, reset game state or perform other actions
        heroMovedOrFallen = true;
//        resetGameState();
    }

    public void resetGameState() throws IOException {
        // Clear all existing nodes
        for (Node node : root.getChildren()) {
            if (node instanceof ImageView && node.getId() != null && node.getId().equals("Hero")) {
                imageView = (ImageView) node;

                // Set layoutX and layoutY for the ImageView
                imageView.setLayoutX(imageView.getLayoutX() - 600);
                imageView.setLayoutY(450);

                break;
            }
        }

        this.stage.close();
    }


    @FXML
    public void startGame(ActionEvent e) throws IOException {
        Repeat();
    }



    private boolean isFlipped = false;
    private boolean isPlayerInverted = false;

    private Label HighscoreLabel;
    private GameObjectFactory gameObjectFactory = new GameObjectFactory();
    public void spawnCherry(String s) {
        GameObject cherry = gameObjectFactory.createGameObject("cherry");
        cherry.spawn();
    }
    private void spawnCherry() {
        // Fixed X-coordinate for cherry spawn
        double gap = rectangle2.getX() - (rectangle.getX() + rectangle.getWidth());
        if (gap > 150) {
            Random random = new Random();
            double cherryX = 600 + (random.nextDouble(0, rectangle.getWidth()));

            // Fixed Y-coordinate for cherry spawn
            double cherryY = 600 + Math.random() * 100;

            cherry = new ImageView(new Image(getClass().getResourceAsStream("/com/example/learningjavafx/cherry.png")));
            cherry.setFitWidth(50);
            cherry.setFitHeight(50);
            cherry.setId("Cherry");
            cherry.setLayoutX(cherryX);
            cherry.setLayoutY(cherryY);
            root.getChildren().add(cherry);
        }
    }
    public void spawnHero() {
        Image hero = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/learningjavafx/hero.png")));
        imageView = new ImageView(hero);
        imageView.setFitWidth(140);
        imageView.setFitHeight(150);
        imageView.setLayoutX(463);
        imageView.setLayoutY(450);
        imageView.setId("Hero");
        root.getChildren().add(imageView);
    }
    public void spawnHero(String s) {
        GameObject hero = gameObjectFactory.createGameObject("hero");
        hero.spawn();
    }

    public void Repeat() throws IOException {
        CompletableFuture<Void> repeatFuture = new CompletableFuture<>();



        LineMovementDone = false;
        AnimationTimer collisionCheckTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                checkCollisionDuringAnimation();
            }
        };


        CompletableFuture.runAsync(() -> {

            if(noOfLife == 0){
                showExitScreen();
                return;
            }
            // UI-related code
            if (this.root != null) {
                root.getChildren().clear();
            }

            // Create a new AnchorPane
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/learningjavafx/Imageview.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            root = new AnchorPane();
            root.setPrefSize(1920, 1080);

            Image backgroundImage = new Image(getClass().getResourceAsStream("/com/example/learningjavafx/1374122-mountains-valley-scenery-digital-art-4k-pc-wallpaper.jpg"));
            BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
            BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
            root.setBackground(new Background(background));

            if (this.scene != null) {
                this.scene.getWindow().hide();
            }

            // Create a new Line
            line1 = new Line();
            line1.setStartX(600);
            line1.setStartY(600);
            line1.setEndX(600);
            line1.setEndY(600);
            line1.setStrokeWidth(10);
            line1.setStroke(Color.DARKVIOLET);
            line1.autosize();

            // Create a new Rectangle
            rectangle = new Rectangle();
            rectangle.setX(400);
            rectangle.setY(600);
            rectangle.setWidth(200);
            rectangle.setHeight(400);
            rectangle.setFill(Color.BLACK);
            rectangle.autosize();

            scoreLabel =  new Label();
            scoreLabel.setText("Score: "+ (successfulMovements));
            // Set the position in the top-left corner
            scoreLabel.setTextFill(Color.YELLOW);
            scoreLabel.setFont(new Font(30));
            scoreLabel.setLayoutX(250);
            scoreLabel.setLayoutY(100);
            scoreLabel.setPrefWidth(242);
            scoreLabel.setPrefHeight(67);
            scoreLabel.setStyle(
                    "-fx-font-family: 'Verdana';" +
                            "-fx-text-fill: yellow;" +  // Change text color to yellow+
                            "-fx-border-color: transparent;" +
                            "-fx-padding: 15px 32px;" +
                            "-fx-text-decoration: none;" +
                            "-fx-font-size: 30px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-border-radius: 80px;" +
                            "-fx-cursor: hand;" +
                            "-fx-transition: all 0.9s;"

            );
            HighscoreLabel =  new Label();
            HighscoreLabel.setText("Highest Score: "+ (player.getHighestScore()));
            // Set the position in the top-left corner
            HighscoreLabel.setTextFill(Color.YELLOW);
            HighscoreLabel.setFont(new Font(30));
            HighscoreLabel.setLayoutX(250);
            HighscoreLabel.setLayoutY(150);
            HighscoreLabel.setPrefWidth(400);
            HighscoreLabel.setPrefHeight(67);
            HighscoreLabel.setStyle(
                    "-fx-font-family: 'Verdana';" +
                            "-fx-text-fill: yellow;" +  // Change text color to yellow+
                            "-fx-border-color: transparent;" +
                            "-fx-padding: 15px 32px;" +
                            "-fx-text-decoration: none;" +
                            "-fx-font-size: 30px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-border-radius: 80px;" +
                            "-fx-cursor: hand;" +
                            "-fx-transition: all 0.9s;"

            );
            root.getChildren().add(HighscoreLabel);
            Life = new Label();
            Life.setText("Hearts: "+ (noOfLife));
            // Set the position in the top-left corner
            Life.setTextFill(Color.DARKRED);
            Life.setFont(new Font(30));
            Life.setLayoutX(1500);
            Life.setLayoutY(100);
            Life.setPrefWidth(242);
            Life.setPrefHeight(67);
            Life.setStyle("-fx-font-family: 'Verdana';" +// Change text color to yellow+
                    "-fx-border-color: transparent;" +
                    "-fx-padding: 15px 32px;" +
                    "-fx-text-decoration: none;" +
                    "-fx-font-size: 30px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-border-radius: 80px;" +
                    "-fx-cursor: hand;" +
                    "-fx-transition: all 0.9s;");


            root.getChildren().add(Life);

            // Add label to AnchorPane


            root.getChildren().add(scoreLabel);

            // Add Line and Rectangle to the AnchorPane
            root.getChildren().addAll(line1, rectangle);
            initializeGame();

            spawnCherry();




//            initializeScoreLabel();

            // Set the new scene on the stage
            Scene newScene = new Scene(root);
            this.stage = new Stage();
            this.stage.setScene(newScene);

            newScene.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.SPACE) {
                    if (!isGrowing) {
                        isGrowing = true;
                        resetGrowthTimeline();
                        growthTimeline.setCycleCount(Animation.INDEFINITE);
                        growthTimeline.play();
                    }
                } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    // Pause the game
                    showPauseScreen();
                }else if (keyEvent.getCode() == KeyCode.DOWN) {
                    // Flip the hero upside down around the base when down arrow key is pressed
                    if (!isFlipped) {
                        isFlipped = true;
                        isPlayerInverted = true;
                        imageView.setLayoutY(imageView.getLayoutY() + imageView.getFitHeight());
                    }
                    imageView.setScaleY(-1);
                }
            });

            newScene.setOnKeyReleased(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.SPACE) {
                    isGrowing = false;
                    growthTimeline.pause();
                    resetGrowthTimeline();
                    handleGrowthFinished();
                }else if (keyEvent.getCode() == KeyCode.DOWN) {
                    // Reset the hero orientation and layout when down arrow key is released
                    if (isFlipped) {
                        isFlipped = false;
                        isPlayerInverted=false;
                        imageView.setLayoutY(imageView.getLayoutY() - imageView.getFitHeight());
                    }
                    imageView.setScaleY(1);
                }
            });

            line1.requestFocus();
            collisionCheckTimer.start();
            this.stage.showAndWait();
            collisionCheckTimer.stop();

            // Complete the CompletableFuture when the execution is completed
            repeatFuture.complete(null);
        }, Platform::runLater);

        // Wait for the completion of the previous execution before calling Repeat again
        repeatFuture.thenRunAsync(() -> {
            try {
                Repeat();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, Platform::runLater);
    }
    private void checkCollisionDuringAnimation() {
        if (isPlayerInverted && intersectsInverted(imageView, cherry)) {
            // The imageView intersects with the cherry during animation while the player is inverted
            cherryCollisionDuringAnimation = true;


            // Perform other actions if needed
        }
    }
    private void showPauseScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/learningjavafx/PauseScreen.fxml"));
            AnchorPane pauseRoot = loader.load();

            PauseScreenController pauseScreenController = loader.getController();
            pauseScreenController.setGameController(this); // Pass the reference to the game controller

            pauseScene = new Scene(pauseRoot);
            pauseStage = new Stage();
            pauseStage.setScene(pauseScene);
            pauseStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showExitScreen() {
        try {
            AnchorPane exitRoot = new AnchorPane();
            exitRoot.setPrefSize(1920, 1080);

            Image backgroundImage = new Image(getClass().getResourceAsStream("/com/example/learningjavafx/1374122-mountains-valley-scenery-digital-art-4k-pc-wallpaper.jpg"));
            BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
            BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
            exitRoot.setBackground(new Background(background));
            Button exitButton = new Button("EXIT");
            exitButton.setLayoutX(852);
            exitButton.setLayoutY(508);
            exitButton.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #ffcc66, #ff9900);" +
                            "-fx-border-color: #cc3525;" +
                            "-fx-border-width: 5;" +
                            "-fx-background-radius: 13 13 13 13;" +
                            "-fx-border-radius: 10 10 10 10;" +
                            "-fx-text-fill: #cc3525;" +
                            "-fx-font-size: 18px;" +
                            "-fx-font-family: 'Verdana';" +
                            "-fx-font-weight: bold;" +
                            "-fx-effect: dropshadow(gaussian, #000000, 10, 0, 0, 1);" +
                            "-fx-pref-width: 216px;" +
                            "-fx-pref-height: 64px;"
            );

// Hover effect
            exitButton.setOnMouseEntered(e -> exitButton.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #ffcc66, #ff9900);" +
                            "-fx-border-color: #cc3525;" +
                            "-fx-border-width: 5;" +
                            "-fx-background-radius: 13 13 13 13;" +
                            "-fx-border-radius: 10 10 10 10;" +
                            "-fx-text-fill: #cc3525;" +
                            "-fx-font-size: 18px;" +
                            "-fx-font-family: 'Verdana';" +
                            "-fx-font-weight: bold;" +
                            "-fx-effect: dropshadow(gaussian, #000000, 10, 0, 0, 1);" +
                            "-fx-pref-width: 216px;" +
                            "-fx-pref-height: 64px;" +
                            "-fx-scale-x: 1.1;" +
                            "-fx-scale-y: 1.1;" +
                            "-fx-cursor: hand;"
            ));

// Reset style on mouse exit
            exitButton.setOnMouseExited(e -> exitButton.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #ffcc66, #ff9900);" +
                            "-fx-border-color: #cc3525;" +
                            "-fx-border-width: 5;" +
                            "-fx-background-radius: 13 13 13 13;" +
                            "-fx-border-radius: 10 10 10 10;" +
                            "-fx-text-fill: #cc3525;" +
                            "-fx-font-size: 18px;" +
                            "-fx-font-family: 'Verdana';" +
                            "-fx-font-weight: bold;" +
                            "-fx-effect: dropshadow(gaussian, #000000, 10, 0, 0, 1);" +
                            "-fx-pref-width: 216px;" +
                            "-fx-pref-height: 64px;"
            ));


            exitButton.setOnAction(e -> {
                // Close the application
                PauseScreenController pauseScreenController = new PauseScreenController();
                pauseScreenController.exitGame(e);
            });
            Label scoreLabel2 = new Label("Your Score: " + successfulMovements);
            scoreLabel2.setLayoutX(760);
            scoreLabel2.setLayoutY(410);

            scoreLabel2.setAlignment(Pos.CENTER);

            scoreLabel2.setFont(Font.font("Berlin Sans FB Demi Bold", 64.0));
            scoreLabel2.setStyle(
                    "-fx-alignment: center;" + /* Center the text within the label */
                            "-fx-font-family: 'Berlin Sans FB Demi Bold';" + /* Set font family */
                            "-fx-font-size: 64.0;" + /* Set font size */
                            "-fx-text-fill: black;" + /* Set text color */

                            /* Add some additional styles for a cool effect */
                            "-fx-scale-x: 1.1;" + /* Enlarge horizontally on hover */
                            "-fx-scale-y: 1.1;" + /* Enlarge vertically on hover */
                            "-fx-cursor: hand;" /* Show hand cursor on hover */
            );



            exitRoot.getChildren().addAll(exitButton, scoreLabel2);
            // Layout setup for button and label
//            AnchorPane.setTopAnchor(exitButton, 500.0);
//            AnchorPane.setLeftAnchor(exitButton, 800.0);
//            AnchorPane.setTopAnchor(scoreLabel, 50.0);
//            AnchorPane.setLeftAnchor(scoreLabel, 800.0);

            exitScene = new Scene(exitRoot);
            exitStage= new Stage();
            exitStage.setScene(exitScene);
            exitStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        pauseStage.close();
        // Resume the game loop or perform other actions
        // For example, you can continue the game loop from where it was paused.
    }

    public void exitGame() {
        pauseStage.close();
        // Code to exit the game
        Platform.exit(); // This will close the JavaFX application
        System.exit(0); // This will terminate the entire Java application
    }


    @Override
    public void start(Stage stage) throws Exception {
        loadPlayerDetails();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/learningjavafx/EntryScreen.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

//        startGameLoop();

        stage.show();
    }

    @Override
    public void run() {
        try {
            Repeat();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
