package UserInterface;

import TetrisGame.*;
import FileManagement.FileManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

public class StartInterface extends Application {
    private HomeScene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        scene = new HomeScene(primaryStage);
        primaryStage.setScene(scene.getScene());
    }

    @Override
    public void stop() {
        System.out.println("Stage is closing");
        scene.close();
    }

    public static void main(String[] args) {
        launch();
    }
}
