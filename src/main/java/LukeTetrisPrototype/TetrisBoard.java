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
    int occupiedTileY;
    int occupiedTileX;

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

        for (int i = 0; i < columnHeightArray.length; i++) {
            columnHeightArray[i] = boardHeight;
        }
        
        spawnPiece();
    }

    public void spawnPiece() {
        setOccupied(0, 5);
    }

    public void setOccupied(int y, int x) {
        if (y >= columnHeightArray[occupiedTileX]) {
            // Freeze the shape in place then spawn a new one
            columnHeightArray[occupiedTileX] -= 1;
            occupiedTileY = 0;
            occupiedTileX = 5;
            spawnPiece();
            return;
        }
        if (x < 0 || x >= rectArray[0].length) {
            return;
        }

        rectArray[occupiedTileY][occupiedTileX].setFill(Color.WHITE);
        occupiedTileY = y;
        occupiedTileX = x;
        rectArray[y][x].setFill(Color.ORANGE);
    }
}
