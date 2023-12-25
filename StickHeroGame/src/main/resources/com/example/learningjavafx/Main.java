package com.example.learningjavafx;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/learningjavafx/EntryScreen.fxml"));
        Parent root =loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Stick Hero Game");
//        Image icon = new Image("C:\\Users\\YASH SINGH\\OneDrive\\Documents\\StickHeroGame_AP_Project\\src\\main\\resources\\com\\example\\stickherogame_ap_project\\logo.png");
//        stage.getIcons().add(icon);
        stage.setScene(scene);
//        stage.setResizable(false);
//        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
