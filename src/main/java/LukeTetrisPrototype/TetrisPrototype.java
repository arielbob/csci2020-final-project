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
    int[][] movingTiles = new int[4][2];

    public static void main(String[] args) {
        launch();
    }

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
                // TODO - Replace these blocks of code with a method setNewTitleLocation()
                for (int r = 0; r < board.occupiedTiles.length; r++) {
                    for (int c = 0; c < board.occupiedTiles[r].length; c++) {
                        movingTiles[r][c] = board.occupiedTiles[r][c];
                        if (1 == c) {
                            movingTiles[r][c] -= 1;
                        }
                    }
                }
                board.moveTetrimino(movingTiles);
            }
            if (e.getCode() == KeyCode.RIGHT) {
                board.setOccupied(board.occupiedTileY, board.occupiedTileX+1);
                // TODO - Replace these blocks of code with a method setNewTitleLocation()
                for (int pair = 0; pair < board.occupiedTiles.length; pair++) {
                    for (int xy = 0; xy < board.occupiedTiles[pair].length; xy++) {
                        movingTiles[pair][xy] = board.occupiedTiles[pair][xy];
                        if (1 == xy) {
                            movingTiles[pair][xy] += 1;
                        }
                    }
                }
                board.moveTetrimino(movingTiles);
            }
            if (e.getCode() == KeyCode.DOWN) {
                board.setOccupied(board.occupiedTileY+1, board.occupiedTileX);
                // TODO - Replace these blocks of code with a method setNewTitleLocation()
                for (int pair = 0; pair < board.occupiedTiles.length; pair++) {
                    for (int xy = 0; xy < board.occupiedTiles[pair].length; xy++) {
                        movingTiles[pair][xy] = board.occupiedTiles[pair][xy];
                        if (0 == xy) {
                            movingTiles[pair][xy] += 1;
                        }
                    }
                }
                board.moveTetrimino(movingTiles);
            }
        });

        primaryStage.setTitle("Tetris Prototype");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
