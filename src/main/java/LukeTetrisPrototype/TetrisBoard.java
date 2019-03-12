package LukeTetrisPrototype;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import java.util.Random;
import java.util.Arrays;

public class TetrisBoard {
    public Pane pane = new Pane();
    int boardWidth = 10;
    int boardHeight = 24;
    int[][] boardState = new int[boardHeight][boardWidth];
    Rectangle[][] boardArray = new Rectangle[boardHeight][boardWidth];
    double tileSize = 15;
    public int[][] occupiedTiles = new int[4][2];
    int displaceY = 0;
    int displaceX = 0;
    Tetrimino currentBlock;
    Tetrimino[] blockSet = {new IBlock(), new JBlock(), new LBlock(),
        new OBlock(), new SBlock(), new TBlock(), new ZBlock()};

    public TetrisBoard() {
        for (int r = 0; r < boardArray.length; r++) {
            for (int c = 0; c < boardArray[r].length; c++) {
                Rectangle tile = new Rectangle(tileSize, tileSize, Color.WHITE);
                tile.setStroke(Color.BLACK);
                tile.setY((r-4)*tileSize);
                tile.setX(c*tileSize);
                boardArray[r][c] = tile;
                pane.getChildren().add(tile);
            }
        }

        spawnTetrimino();
    }

    private void spawnTetrimino() {
        currentBlock = pickRandomBlock(blockSet);
        int[][] pieceArray = currentBlock.rotationsArray[0];
        currentBlock.setRotationState(0);
        displaceY = currentBlock.spawnPointY;
        displaceX = currentBlock.spawnPointX;
        int i = 0;
        for (int y = 0; y < pieceArray.length; y++) {
            for (int x = 0; x < pieceArray[y].length; x++) {
                if (1 == pieceArray[y][x]) {
                    // Draw each piece of the block
                    boardArray[y + displaceY][x + displaceX].setFill(currentBlock.paint);
                    occupiedTiles[i][0] = y + displaceY;
                    occupiedTiles[i][1] = x + displaceX;
                    i++;
                }
            }
        }
    }

    public void moveTetrimino(int[][] movingTiles, String dir) {
        for (int[] pair : movingTiles) {
            int y = pair[0];
            int x = pair[1];

            if (dir.equals("vertical")) {
                if (y > boardHeight - 1 || 1 == boardState[y][x]) {
                    setBoardState();
                    checkForFilledRows();
                    spawnTetrimino();
                    return;
                }
            }
            if (dir.equals("horizontal")) {
                if (x < 0 || x >= boardWidth || 1 == boardState[y][x]) {
                    return;
                }
            }
        }

        displaceY += movingTiles[0][0] - occupiedTiles[0][0];
        displaceX += movingTiles[0][1] - occupiedTiles[0][1];

        // Erases the current block.
        for (int[] pair : occupiedTiles) {
            boardArray[pair[0]][pair[1]].setFill(Color.WHITE);
        }
        // Redraws the current block in its new location.
        for (int pair = 0; pair < movingTiles.length; pair++) {
            int y = movingTiles[pair][0];
            int x = movingTiles[pair][1];
            boardArray[y][x].setFill(currentBlock.paint);
            occupiedTiles[pair][0] = movingTiles[pair][0];
            occupiedTiles[pair][1] = movingTiles[pair][1];
        }
    }

    public void rotateTetrimino() {
        int newRotationState = (currentBlock.getRotationState() + 1) % 4;
        int floorKick = checkFloorKick();
        int wallKick = checkWallKick(floorKick);
        if (!isRotationPossible(newRotationState, wallKick, floorKick)) {
            return;
        }

        // Erase the current shape so it can be redrawn.
        for (int[] tile : occupiedTiles) {
            boardArray[tile[0]][tile[1]].setFill(Color.WHITE);
        }

        int[][] pieceArray = currentBlock.rotationsArray[newRotationState];
        currentBlock.setRotationState(newRotationState);
        displaceY += floorKick;
        displaceX += wallKick;
        int i = 0;

        // Applies rotation after checking if it's possible.
        for (int y = 0; y < pieceArray.length; y++) {
            for (int x = 0; x < pieceArray[y].length; x++) {
                int newYPos = y + displaceY;
                int newXPos = x + displaceX;
                if (1 == pieceArray[y][x]) {
                    boardArray[newYPos][newXPos].setFill(currentBlock.paint);
                    occupiedTiles[i][0] = newYPos;
                    occupiedTiles[i][1] = newXPos;
                    i++;
                }
            }
        }
    }

    private int checkFloorKick() {
        int floorKickState = 0;
        int yTracker = -999;
        int rotationState = (currentBlock.getRotationState() + 1) % 4;
        int[][] pieceArray = currentBlock.rotationsArray[rotationState];
        for (int y = 0; y < pieceArray.length; y++) {
            for (int x = 0; x < pieceArray[y].length; x++) {
                if (1 == pieceArray[y][x]) {
                    int newY = y + displaceY;
                    int newX = x + displaceX;
                    if (newY != yTracker) {
                        if (newY >= boardHeight || 0 <= newX && newX < boardWidth && newY < boardHeight && 1 == boardState[newY][newX]) {
                            floorKickState--;
                            yTracker = newY;
                        }
                    }
                }
            }
        }
        return floorKickState;
    }

    private int checkWallKick(int floorKick) {
        int wallKickState = 0;
        int xTracker = -999;
        int rotationState = (currentBlock.getRotationState() + 1) % 4;
        int[][] pieceArray = currentBlock.rotationsArray[rotationState];
        for (int y = 0; y < pieceArray.length; y++) {
            for (int x = 0; x < pieceArray[y].length; x++) {
                if (1 == pieceArray[y][x]) {
                    int newY = y + displaceY + floorKick;
                    int newX = x + displaceX;
                    if (newX != xTracker) {
                        if (newX >= boardWidth || 0 <= newX && newX < boardWidth && newY < boardHeight && 1 == boardState[newY][newX] && x >= 2) {
                            wallKickState--;
                            xTracker = newX;
                        }
                        else if (newX < 0 || 0 <= newX && newX < boardWidth && newY < boardHeight && 1 == boardState[newY][newX] && x <= 1) {
                            wallKickState++;
                            xTracker = newX;
                        }
                    }
                }
            }
        }
        return wallKickState;
    }

    private boolean isRotationPossible(int newRotationState, int wallKick, int floorKick) {
        int[][] pieceArray = currentBlock.rotationsArray[newRotationState];
        for (int y = 0; y < pieceArray.length; y++) {
            for (int x = 0; x < pieceArray[y].length; x++) {
                if (1 == pieceArray[y][x]) {
                    int newY = y + displaceY + floorKick;
                    int newX = x + displaceX + wallKick;
                    if (newY >= boardState.length || 1 == boardState[newY][newX]) {
                        return false;
                    }
                }
            }
        }
        return true;
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
