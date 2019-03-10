package LukeTetrisPrototype;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import java.util.Random;

public class TetrisBoard {
    Pane pane = new Pane();
    int boardWidth = 10;
    int boardHeight = 24;
    int[][] boardState = new int[boardHeight][boardWidth];
    Rectangle[][] boardArray = new Rectangle[boardHeight][boardWidth];
    double tileSize = 15;
    int occupiedTileY;
    int occupiedTileX;
    int[][] occupiedTiles = new int[4][2];
    Tetrimino currentBlock;
    Tetrimino[] blockSet = {new IBlock(), new JBlock(), new LBlock(),
        new OBlock(), new SBlock(), new TBlock(), new ZBlock()};

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

        spawnTetrimino();
    }

    private void spawnTetrimino() {
        Tetrimino tetrimino = pickRandomBlock(blockSet);
        currentBlock = tetrimino;
        int[][] pieceArray = tetrimino.rotationState[0];
        int i = 0;
        for (int y = 0; y < pieceArray.length; y++) {
            for (int x = 0; x < pieceArray[y].length; x++) {
                if (1 == pieceArray[y][x]) {
                    // draw each piece
                    boardArray[y][x].setFill(tetrimino.paint);
                    occupiedTiles[i][0] = y;
                    occupiedTiles[i][1] = x;
                    i++;
                }
            }
        }
    }

    public void moveTetrimino(int[][] movingTiles, String dir) {
        for (int[] pair : movingTiles) {
            int y = pair[0];
            int x = pair[1];

            if (dir.equals("verti")) {
                if (y > boardHeight - 1 || 1 == boardState[y][x]) {
                    setBoardState();
                    checkForFilledRows();
                    spawnTetrimino();
                    return;
                }
            }
            if (dir.equals("hori")) {
                if (x < 0 || x >= boardArray[0].length || 1 == boardState[y][x]) {
                    return;
                }
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

    public void rotateTetrimino() {
        System.out.println("Rotate");
    }

    private void setBoardState() {
        for (int[] pair : occupiedTiles) {
            int y = pair[0];
            int x = pair[1];
            boardState[y][x] = 1;
        }
    }

    private void checkForFilledRows() {
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

    private void clearLine(int rowIndex) {
        for (int c = 0; c < boardArray[rowIndex].length; c++) {
            boardArray[rowIndex][c].setFill(Color.WHITE);
            boardState[rowIndex][c] = 0;
        }

        // Shifts all the colored blocks down one row.
        for (int r = rowIndex--; r >= 0; r--) {
            for (int c = 0; c < boardArray[r].length; c++) {
                Paint tempColor = boardArray[r][c].getFill();
                if (tempColor != Color.WHITE) {
                    boardArray[r][c].setFill(Color.WHITE);
                    boardArray[r + 1][c].setFill(tempColor);
                    boardState[r][c] = 0;
                    boardState[r + 1][c] = 1;
                }
            }
        }
    }

    private Tetrimino pickRandomBlock(Tetrimino[] blockSet) {
        int rand = new Random().nextInt(blockSet.length);
        return blockSet[rand];
    }

}
