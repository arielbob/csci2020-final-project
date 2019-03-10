package LukeTetrisPrototype;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

public class TetrisBoard {
    Pane pane = new Pane();
    int boardWidth = 10;
    int boardHeight = 24;
    int[] columnHeightArray = new int[boardWidth];
    //int[][] boardArray = new int[boardHeight][boardWidth];
    Rectangle[][] boardArray = new Rectangle[boardHeight][boardWidth];
    double tileSize = 15;
    int occupiedTileY;
    int occupiedTileX;
    int[][] occupiedTiles = new int[4][2];
    Tetrimino currentBlock;

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
            columnHeightArray[i] = boardHeight - 1;
        }

        //spawnPiece();
        spawnTetrimino();
    }

    public void spawnTetrimino() {
        //IBlock tetrimino = new IBlock();
        LBlock tetrimino = new LBlock();
        currentBlock = tetrimino;
        int[][] pieceArray = tetrimino.rotationState[1];
        int i = 0;
        for (int y = 0; y < pieceArray.length; y++) {
            for (int x = 0; x < pieceArray[y].length; x++) {
                if (1 == pieceArray[y][x]) {
                    // draw each piece
                    boardArray[y][x].setFill(tetrimino.paint);
                    occupiedTiles[i][0] = y;
                    occupiedTiles[i][1] = x;
                    System.out.println("occupiedTile" + i + ": " + y + ", " + x);
                    i++;
                }
            }
        }
    }

    public void moveTetrimino(int[][] movingTiles, String dir) {
        for (int[] pair : movingTiles) {
            int y = pair[0];
            int x = pair[1];
            if (dir.equals("verti") && y > columnHeightArray[pair[1]]) {
                setColumnHeightArray();
                checkForFilledRows();
                spawnTetrimino();
                return;
            }
            if (x < 0 || x >= boardArray[0].length)) {
                return;
            }
        }

        for (int pair = 0; pair < occupiedTiles.length; pair++) {
            int y = occupiedTiles[pair][0];
            int x = occupiedTiles[pair][1];
            boardArray[y][x].setFill(Color.WHITE);
        }
        for (int pair = 0; pair < movingTiles.length; pair++) {
            int y = movingTiles[pair][0];
            int x = movingTiles[pair][1];
            boardArray[y][x].setFill(currentBlock.paint);
            occupiedTiles[pair][0] = movingTiles[pair][0];
            occupiedTiles[pair][1] = movingTiles[pair][1];
        }
    }

    public void setColumnHeightArray() {
        for (int[] pair : occupiedTiles) {
            if (pair[0] <= columnHeightArray[pair[1]]) {
                columnHeightArray[pair[1]] = pair[0] - 1;
            }
        }
    }

    public void spawnPiece() {
        setOccupied(3, 1);
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
