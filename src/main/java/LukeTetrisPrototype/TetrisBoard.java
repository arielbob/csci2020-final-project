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
    int occupiedTileX;
    int occupiedTileY;

    public TetrisBoard() {
        for (int h = 0; h < boardArray.length; h++) {
            for (int w = 0; w < boardArray[h].length; w++) {
                Rectangle r = new Rectangle(tileSize, tileSize, Color.WHITE);
                r.setStroke(Color.BLACK);
                r.setX(w*tileSize);
                r.setY(h*tileSize);
                rectArray[h][w] = r;
                pane.getChildren().add(r);
            }
        }

        setOccupied(5, 5);
    }

    public void setOccupied(int y, int x) {
        for (int h = 0; h < rectArray.length; h++) {
            for (int w = 0; w < rectArray[h].length; w++) {
                rectArray[h][w].setFill(Color.WHITE);
            }
        }
        occupiedTileX = x;
        occupiedTileY = y;
        rectArray[y][x].setFill(Color.BLACK);
    }
}
