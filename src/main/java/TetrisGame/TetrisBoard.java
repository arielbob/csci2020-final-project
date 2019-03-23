package TetrisGame;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Random;
import java.util.Arrays;
import TetrisGame.Tetriminos.*;
import net.client.TetrisClient;
import net.test.ClientTest;

public class TetrisBoard {
    public volatile Pane pane = new Pane();
    public boolean stageClosed = false;
    boolean gameOver = false;

    Paint defaultTileColor = Color.WHITE;
    double tileSize = 15;
    int hiddenRowNum = 3;
    private final int BOARD_HEIGHT = 24;
    private final int BOARD_WIDTH = 10;
    int[][] boardState = new int[BOARD_HEIGHT][BOARD_WIDTH];
    Rectangle[][] boardArray = new Rectangle[BOARD_HEIGHT][BOARD_WIDTH];

    public int[][] occupiedTiles = new int[4][2];
    int displaceY = 0;
    int displaceX = 0;
    Tetrimino currentBlock;
    Tetrimino[] blockSet = {new IBlock(), new JBlock(), new LBlock(),
        new OBlock(), new SBlock(), new TBlock(), new ZBlock()};

    TetrisClient client;

    public TetrisBoard() {
        for (int r = 0; r < boardArray.length; r++) {
            for (int c = 0; c < boardArray[r].length; c++) {
                Rectangle tile = new Rectangle(tileSize, tileSize, defaultTileColor);
                tile.setStroke(Color.BLACK);
                tile.setY((r - hiddenRowNum) * tileSize);
                tile.setX(c * tileSize);
                boardArray[r][c] = tile;
                pane.getChildren().add(tile);
                if (r < hiddenRowNum) {
                    tile.setVisible(false);
                }
            }
        }
    }

    public TetrisBoard(TetrisClient client) {
        this();
        this.client = client;
    }

    public void startGame() {
        gameOver = false;
        for (int r = 0; r < BOARD_HEIGHT; r++) {
            for (int c = 0; c < BOARD_WIDTH; c++) {
                setTileAt(r, c, 0);
            }
        }

		spawnTetrimino();

        // Makes the blocks float down the grid.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!gameOver && !stageClosed) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                moveTetrimino("down");
                            }
                        });
                        Thread.sleep(500);
                    }
                }
                catch (InterruptedException ex) {
                }
            }
        }).start();
	}

    private void spawnTetrimino() {
        if (gameOver) {
            return;
        }

        currentBlock = pickRandomBlock(blockSet);
        int[][] pieceArray = currentBlock.getRotationsArray(0);
        currentBlock.setRotationState(0);
        displaceY = currentBlock.getSpawnPointY() + hiddenRowNum;
        displaceX = currentBlock.getSpawnPointX();
        int i = 0;
        for (int y = 0; y < pieceArray.length; y++) {
            for (int x = 0; x < pieceArray[y].length; x++) {
                if (1 == pieceArray[y][x]) {
                    // Draw each piece of the block
                    setTileAt(y + displaceY, x + displaceX, currentBlock.getMovingNum());
                    occupiedTiles[i][0] = y + displaceY;
                    occupiedTiles[i][1] = x + displaceX;
                    i++;
                }
            }
        }
    }

    public int[][] setNewBlockLocation(String direction) {
        int dir = 0;
        int xory = 0;
        switch (direction) {
            case "left":
                xory = 1;
                dir = -1;
                break;
            case "right":
                xory = 1;
                dir = 1;
                break;
            case "down":
                xory = 0;
                dir = 1;
                break;
        }

        int[][] movingTiles = new int[4][2];
        for (int pair = 0; pair < occupiedTiles.length; pair++) {
            for (int xy = 0; xy < occupiedTiles[pair].length; xy++) {
                movingTiles[pair][xy] = occupiedTiles[pair][xy];
                if (xy == xory) {
                    movingTiles[pair][xy] += dir;
                }
            }
        }
        return movingTiles;
    }

    public void moveTetrimino(String dir) {
        if (gameOver) {
            return;
        }
        int[][] movingTiles = setNewBlockLocation(dir);
        for (int[] pair : movingTiles) {
            int y = pair[0];
            int x = pair[1];

            switch (dir) {
                case "down":
                    if (y > BOARD_HEIGHT - 1 || boardState[y][x] >= 8) {
                        checkBoardState();
                        checkForFilledRows();
                        spawnTetrimino();
                        return;
                    }
                    break;
                case "left":
                case "right":
                    if (x < 0 || x >= BOARD_WIDTH || boardState[y][x] >= 8) {
                        return;
                    }
                    break;
            }
        }
        displaceY += movingTiles[0][0] - occupiedTiles[0][0];
        displaceX += movingTiles[0][1] - occupiedTiles[0][1];

        // Erases the current block.
        for (int[] pair : occupiedTiles) {
            setTileAt(pair[0], pair[1], 0);
        }
        // Redraws the current block in its new location.
        for (int pair = 0; pair < movingTiles.length; pair++) {
            int y = movingTiles[pair][0];
            int x = movingTiles[pair][1];
            setTileAt(y, x, currentBlock.getMovingNum());
            occupiedTiles[pair][0] = movingTiles[pair][0];
            occupiedTiles[pair][1] = movingTiles[pair][1];
        }

        if (null != client) {
            try {
                client.sendBoardState(boardState);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            setTileAt(tile[0], tile[1], 0);
        }

        int[][] pieceArray = currentBlock.getRotationsArray(newRotationState);
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
                    setTileAt(newYPos, newXPos, currentBlock.getMovingNum());
                    occupiedTiles[i][0] = newYPos;
                    occupiedTiles[i][1] = newXPos;
                    i++;
                }
            }
        }

        if (null != client) {
            try {
                client.sendBoardState(boardState);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int checkFloorKick() {
        int floorKickState = 0;
        int yTracker = -999;
        int rotationState = (currentBlock.getRotationState() + 1) % 4;
        int[][] pieceArray = currentBlock.getRotationsArray(rotationState);
        for (int y = 0; y < pieceArray.length; y++) {
            for (int x = 0; x < pieceArray[y].length; x++) {
                if (1 == pieceArray[y][x]) {
                    int newY = y + displaceY;
                    int newX = x + displaceX;
                    if (newY != yTracker) {
                        if (newY >= BOARD_HEIGHT ||
                        0 <= newX && newX < BOARD_WIDTH &&
                        newY < BOARD_HEIGHT && boardState[newY][newX] >= 8) {
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
        int[][] pieceArray = currentBlock.getRotationsArray(rotationState);
        for (int y = 0; y < pieceArray.length; y++) {
            for (int x = 0; x < pieceArray[y].length; x++) {
                if (1 == pieceArray[y][x]) {
                    int newY = y + displaceY + floorKick;
                    int newX = x + displaceX;
                    if (newX != xTracker) {
                        if (newX >= BOARD_WIDTH ||
                        0 <= newX && newX < BOARD_WIDTH &&
                        newY < BOARD_HEIGHT &&
                        boardState[newY][newX] >= 8 && x >= 2) {
                            wallKickState--;
                            xTracker = newX;
                        }
                        else if (newX < 0 ||
                        0 <= newX && newX < BOARD_WIDTH &&
                        newY < BOARD_HEIGHT &&
                        boardState[newY][newX] >= 8 && x <= 1) {
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
        if (gameOver) {
            return false;
        }

        int[][] pieceArray = currentBlock.getRotationsArray(newRotationState);
        for (int y = 0; y < pieceArray.length; y++) {
            for (int x = 0; x < pieceArray[y].length; x++) {
                if (1 == pieceArray[y][x]) {
                    int newY = y + displaceY + floorKick;
                    int newX = x + displaceX + wallKick;
                    if (newY >= boardState.length || boardState[newY][newX] >= 8) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void setBoardState(int[][] newBoardState) {
        for (int r = 0; r < BOARD_HEIGHT; r++) {
            for (int c = 0; c < BOARD_WIDTH; c++) {
                setTileAt(r, c, newBoardState[r][c]);
            }
        }
    }

    private Paint getColorByNum(int num) {
        if (num < 0) {
            num = 0;
        }
        else if (num < 8) {
            num *= 8;
        }
        switch (num) {
            case 8:
                return blockSet[0].getPaint();
            case 16:
                return blockSet[1].getPaint();
            case 24:
                return blockSet[2].getPaint();
            case 32:
                return blockSet[3].getPaint();
            case 40:
                return blockSet[4].getPaint();
            case 48:
                return blockSet[5].getPaint();
            case 56:
                return blockSet[6].getPaint();
        }
        return defaultTileColor;
    }

    private void checkBoardState() {
        for (int[] pair : occupiedTiles) {
            int y = pair[0];
            int x = pair[1];
            boardState[y][x] = currentBlock.getPlacedNum();
            if (y <= hiddenRowNum - 2) {
                gameOver = true;
                System.out.println("YOU LOSE");
                setLose();

                if (null != client) {
                    try {
                        client.sendLose();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return;
            }
        }
    }

    private void checkForFilledRows() {
        for (int r = 0; r < boardArray.length; r++) {
            int filledTileCount = 0;
            for (int c = 0; c < boardArray[r].length; c++) {
                if (boardState[r][c] >= 8) {
                     filledTileCount++;
                }
            }
            if (filledTileCount >= BOARD_WIDTH) {
                clearLine(r);
            }
        }
    }

    private void clearLine(int rowIndex) {
        for (int c = 0; c < boardArray[rowIndex].length; c++) {
            setTileAt(rowIndex, c, 0);
        }

        // Shifts all the colored blocks down one row.
        for (int r = rowIndex--; r >= 0; r--) {
            for (int c = 0; c < BOARD_WIDTH; c++) {
                int tileState = boardState[r][c];
                if (tileState >= 8) {
                    setTileAt(r, c, 0);
                    setTileAt(r + 1, c, tileState);
                }
            }
        }
    }

    private void setTileAt(int r, int c, int value) {
        boardState[r][c] = value;
        boardArray[r][c].setFill(getColorByNum(value));
    }

    private Tetrimino pickRandomBlock(Tetrimino[] blockSet) {
        int rand = new Random().nextInt(blockSet.length);
        return blockSet[rand];
    }

    public void setLose() {
        int[][] loseBoard = {
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 0, 0, 0}
        };
        for(int r = 0; r < loseBoard.length; r++) {
            for(int c = 0; c < loseBoard[r].length; c++) {
                switch (loseBoard[r][c]) {
                    case 0:
                        boardArray[r + hiddenRowNum][c].setFill(Color.BLACK);
                        break;
                    case 1:
                        boardArray[r + hiddenRowNum][c].setFill(Color.RED);
                        break;
                }
            }
        }
    }

    public void setWin() {
        gameOver = true;
        System.out.println("YOU WIN");
        int[][] winBoard = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 1, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 1, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 1, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 1, 1, 0, 1, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 1, 1, 0, 0, 0, 1, 0, 0},
            {0, 0, 1, 0, 1, 0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 0, 1, 1, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        for(int r = 0; r < winBoard.length; r++) {
            for(int c = 0; c < winBoard[r].length; c++) {
                switch (winBoard[r][c]) {
                    case 0:
                        boardArray[r + hiddenRowNum][c].setFill(Color.BLACK);
                        break;
                    case 1:
                        boardArray[r + hiddenRowNum][c].setFill(Color.web("#00ff1d"));
                        break;
                }
            }
        }
    }
}
