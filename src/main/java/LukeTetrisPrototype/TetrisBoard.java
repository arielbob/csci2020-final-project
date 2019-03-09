package LukeTetrisPrototype;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

public class TetrisBoard {
    Pane pane = new Pane();
    int boardWidth = 10;
    int boardHeight = 25;
    int[] columnHeightArray = new int[boardWidth];
    //int[][] boardArray = new int[boardHeight][boardWidth];
    Rectangle[][] boardArray = new Rectangle[boardHeight][boardWidth];
    double tileSize = 15;
    int occupiedTileY;
    int occupiedTileX;

    public TetrisBoard() {
        for (int r = 0; r < boardArray.length; r++) {
            for (int c = 0; c < boardArray[r].length; c++) {
                Rectangle tile = new Rectangle(tileSize, tileSize, Color.WHITE);
                tile.setStroke(Color.BLACK);
                tile.setX(c*tileSize);
                tile.setY(r*tileSize);
                boardArray[r][c] = tile;
                pane.getChildren().add(tile);
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
            checkForFilledRows();
            spawnPiece();
            return;
        }
        if (x < 0 || x >= boardArray[0].length) {
            return;
        }

        boardArray[occupiedTileY][occupiedTileX].setFill(Color.WHITE);
        occupiedTileY = y;
        occupiedTileX = x;
        boardArray[y][x].setFill(Color.ORANGE);
    }

    public void checkForFilledRows() {
        for (int r = 0; r < boardArray.length; r++) {
            int filledTileCount = 0;
            for (int c = 0; c < boardArray[r].length; c++) {
                if (boardArray[r][c].getFill() != Color.WHITE) {
                     filledTileCount++;
                }
            }
            if (filledTileCount >= boardWidth) {
                clearLine(r);
            }
        }
    }

    public void clearLine(int rowIndex) {
        for (int c = 0; c < boardArray[rowIndex].length; c++) {
            boardArray[rowIndex][c].setFill(Color.WHITE);
            if (columnHeightArray[c] < boardArray.length) {
                columnHeightArray[c] += 1;
            }
        }

        for (int r = rowIndex--; r >= 0; r--) {
            for (int c = 0; c < boardArray[r].length; c++) {
                Paint tempColor = boardArray[r][c].getFill();
                if (tempColor != Color.WHITE) {
                    boardArray[r][c].setFill(Color.WHITE);
                    boardArray[r + 1][c].setFill(tempColor);
                }
            }
        }
    }
}
