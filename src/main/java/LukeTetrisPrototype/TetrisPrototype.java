package LukeTetrisPrototype;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TetrisPrototype extends Application {
    public static void main(String[] args) {
        launch();
    }

    int boardWidth = 10;
    int boardHeight = 25;
    int[][] boardArray = new int[boardHeight][boardWidth];
    double tileSize = 15;
    TetrisBoard board = new TetrisBoard();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();
        Scene scene = new Scene(pane);
        pane.setPrefSize(500, 500);

        pane.getChildren().add(board.pane);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                board.setOccupied(board.occupiedTileY, board.occupiedTileX-1);
            }
            if (e.getCode() == KeyCode.RIGHT) {
                board.setOccupied(board.occupiedTileY, board.occupiedTileX+1);
            }
            if (e.getCode() == KeyCode.DOWN) {
                board.setOccupied(board.occupiedTileY+1, board.occupiedTileX);
            }
        });

        primaryStage.setTitle("Tetris Prototype");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
