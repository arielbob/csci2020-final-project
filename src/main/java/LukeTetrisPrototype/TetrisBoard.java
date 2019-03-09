package LukeTetrisPrototype;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class TetrisBoard {
    Pane pane = new Pane();
    int boardWidth = 10;
    int boardHeight = 25;
    int[] columnHeightArray = new int[boardWidth];
    int[][] boardArray = new int[boardHeight][boardWidth];
    Rectangle[][] rectArray = new Rectangle[boardHeight][boardWidth];
    double tileSize = 15;
    int[] occupiedTile = new int[2];

    public TetrisBoard() {
        for (int i = 0; i < boardArray.length; i++) {
            for (int j = 0; j < boardArray[i].length; j++) {
                Rectangle r = new Rectangle(tileSize, tileSize, Color.WHITE);
                r.setStroke(Color.BLACK);
                r.setX(j*tileSize);
                r.setY(i*tileSize);
                rectArray[i][j] = r;
                pane.getChildren().add(r);
            }
        }

        setOccupied(5, 5);
    }

    public void setOccupied(int x, int y) {
        for (int i = 0; i < rectArray.length; i++) {
            for (int j = 0; j < rectArray[i].length; j++) {
                rectArray[i][j].setFill(Color.WHITE);
            }
        }
        occupiedTile[0] = x;
        occupiedTile[1] = y;
        rectArray[x][y].setFill(Color.BLACK);
    }
}
