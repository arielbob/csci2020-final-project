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
    TetrisBoard board = new TetrisBoard();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();
        Scene scene = new Scene(pane);
        pane.setPrefSize(500, 500);

        pane.getChildren().add(board.pane);

        scene.setOnKeyPressed(e -> {
            //setMovingTilesLocation();
            if (e.getCode() == KeyCode.LEFT) {
                //board.setOccupied(board.occupiedTileY, board.occupiedTileX-1);
                int[][] movingTiles = setNewBlockLocation("left");
                board.moveTetrimino(movingTiles);
            }
            if (e.getCode() == KeyCode.RIGHT) {
                //board.setOccupied(board.occupiedTileY, board.occupiedTileX+1);
                int[][] movingTiles = setNewBlockLocation("right");
                board.moveTetrimino(movingTiles);
            }
            if (e.getCode() == KeyCode.DOWN) {
                //board.setOccupied(board.occupiedTileY+1, board.occupiedTileX);
                int[][] movingTiles = setNewBlockLocation("down");
                board.moveTetrimino(movingTiles);
            }
        });

        primaryStage.setTitle("Tetris Prototype");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public int[][] setNewBlockLocation(String direction) {
        int dir = 0;
        int xory = 0;
        if (direction.equals("left")) {
            xory = 1;
            dir = -1;
        }
        else if (direction.equals("right")) {
            xory = 1;
            dir = 1;
        }
        else if (direction.equals("down")) {
            xory = 0;
            dir = 1;
        }

        int[][] movingTiles = new int[4][2];
        for (int pair = 0; pair < board.occupiedTiles.length; pair++) {
            for (int xy = 0; xy < board.occupiedTiles[pair].length; xy++) {
                movingTiles[pair][xy] = board.occupiedTiles[pair][xy];
                if (xy == xory) {
                    movingTiles[pair][xy] += dir;
                }
            }
        }

        for (int pair = 0; pair < board.occupiedTiles.length; pair++) {
            System.out.println("occupiedTiles" + pair + ": " + board.occupiedTiles[pair][0] + ", " + board.occupiedTiles[pair][1]);
        }
        for (int pair = 0; pair < movingTiles.length; pair++) {
            System.out.println("movingTiles" + pair + ": " + movingTiles[pair][0] + ", " + movingTiles[pair][1]);
        }
        return movingTiles;
    }
}
