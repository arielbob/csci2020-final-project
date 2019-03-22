package TetrisPrototype;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import java.util.Random;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.event.ActionEvent;

import net.test.ClientTest;

public class TetrisBoard {
    public volatile Pane pane = new Pane();
    public boolean stageClosed = false;
    boolean gameOver = false;
    Paint defaultTileColor = Color.WHITE;
    double tileSize = 15;
    int boardWidth = 10;
    int boardHeight = 24;
    int[][] boardState = new int[boardHeight][boardWidth];
    Rectangle[][] boardArray = new Rectangle[boardHeight][boardWidth];
    public int[][] occupiedTiles = new int[4][2];
    int displaceY = 0;
    int displaceX = 0;
    Tetrimino currentBlock;
    Tetrimino[] blockSet = {new IBlock(), new JBlock(), new LBlock(),
        new OBlock(), new SBlock(), new TBlock(), new ZBlock()};

    ClientTest client;

    public TetrisBoard() {
        for (int r = 0; r < boardArray.length; r++) {
            for (int c = 0; c < boardArray[r].length; c++) {
                Rectangle tile = new Rectangle(tileSize, tileSize, defaultTileColor);
                tile.setStroke(Color.BLACK);
                tile.setY((r-4)*tileSize);
                tile.setX(c*tileSize);
                boardArray[r][c] = tile;
                pane.getChildren().add(tile);
                if (r < 4) {
                    tile.setVisible(false);
                }
            }
        }
    }

    public TetrisBoard(ClientTest client) {
        this();
        this.client = client;
    }

    public void startGame() {
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
        int[][] pieceArray = currentBlock.rotationsArray[0];
        currentBlock.setRotationState(0);
        displaceY = currentBlock.spawnPointY;
        displaceX = currentBlock.spawnPointX;
        int i = 0;
        for (int y = 0; y < pieceArray.length; y++) {
            for (int x = 0; x < pieceArray[y].length; x++) {
                if (1 == pieceArray[y][x]) {
                    // Draw each piece of the block
                    boardState[y + displaceY][x + displaceX] = currentBlock.getMovingNum();
                    boardArray[y + displaceY][x + displaceX].setFill(currentBlock.paint);
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
        int[][] movingTiles = setNewBlockLocation(dir);
        for (int[] pair : movingTiles) {
            int y = pair[0];
            int x = pair[1];

            switch (dir) {
                case "down":
                    if (y > boardHeight - 1 || boardState[y][x] >= 8) {
                        checkBoardState();
                        checkForFilledRows();
                        spawnTetrimino();
                        return;
                    }
                    break;
                case "left":
                case "right":
                    if (x < 0 || x >= boardWidth || boardState[y][x] >= 8) {
                        return;
                    }
                    break;
            }
        }
        displaceY += movingTiles[0][0] - occupiedTiles[0][0];
        displaceX += movingTiles[0][1] - occupiedTiles[0][1];

        // Erases the current block.
        for (int[] pair : occupiedTiles) {
            boardState[pair[0]][pair[1]] = 0;
            boardArray[pair[0]][pair[1]].setFill(defaultTileColor);
        }
        // Redraws the current block in its new location.
        for (int pair = 0; pair < movingTiles.length; pair++) {
            int y = movingTiles[pair][0];
            int x = movingTiles[pair][1];
            boardState[y][x] = currentBlock.getMovingNum();
            boardArray[y][x].setFill(currentBlock.paint);
            occupiedTiles[pair][0] = movingTiles[pair][0];
            occupiedTiles[pair][1] = movingTiles[pair][1];
        }

        if (null != client) {
            client.sendBoardState(boardState);
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
            boardState[tile[0]][tile[1]] = 0;
            boardArray[tile[0]][tile[1]].setFill(defaultTileColor);
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
                    boardState[newYPos][newXPos] = currentBlock.getMovingNum();
                    boardArray[newYPos][newXPos].setFill(currentBlock.paint);
                    occupiedTiles[i][0] = newYPos;
                    occupiedTiles[i][1] = newXPos;
                    i++;
                }
            }
        }

        if (null != client) {
            client.sendBoardState(boardState);
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
                        if (newY >= boardHeight ||
                        0 <= newX && newX < boardWidth &&
                        newY < boardHeight && boardState[newY][newX] >= 8) {
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
                        if (newX >= boardWidth ||
                        0 <= newX && newX < boardWidth &&
                        newY < boardHeight &&
                        boardState[newY][newX] >= 8 && x >= 2) {
                            wallKickState--;
                            xTracker = newX;
                        }
                        else if (newX < 0 ||
                        0 <= newX && newX < boardWidth &&
                        newY < boardHeight &&
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
        int[][] pieceArray = currentBlock.rotationsArray[newRotationState];
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
        for (int r = 0; r < boardHeight; r++) {
            for (int c = 0; c < boardWidth; c++) {
                boardState[r][c] = newBoardState[r][c];
                Paint newColor = getColorByNum(boardState[r][c]);
                boardArray[r][c].setFill(newColor);
            }
        }
    }

    private Paint getColorByNum(int num) {
        if (num < 8) {
            num *= 8;
        }
        switch (num) {
            case 8:
                return blockSet[0].paint;
            case 16:
                return blockSet[1].paint;
            case 24:
                return blockSet[2].paint;
            case 32:
                return blockSet[3].paint;
            case 40:
                return blockSet[4].paint;
            case 48:
                return blockSet[5].paint;
            case 56:
                return blockSet[6].paint;
        }
        return defaultTileColor;
    }

    private void checkBoardState() {
        for (int[] pair : occupiedTiles) {
            int y = pair[0];
            int x = pair[1];
            boardState[y][x] = currentBlock.getPlacedNum();
            if (y <= 2) {
                gameOver = true;
                System.out.println("YOU LOSE");
                int[][] loseBoard = {
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
                                boardArray[r + 4][c].setFill(Color.BLACK);
                                break;
                            case 1:
                                boardArray[r + 4][c].setFill(Color.RED);
                                break;
                        }
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
            if (filledTileCount >= boardWidth) {
                clearLine(r);
            }
        }
    }

    private void clearLine(int rowIndex) {
        for (int c = 0; c < boardArray[rowIndex].length; c++) {
            boardArray[rowIndex][c].setFill(defaultTileColor);
            boardState[rowIndex][c] = 0;
        }

        // Shifts all the colored blocks down one row.
        for (int r = rowIndex--; r >= 0; r--) {
            for (int c = 0; c < boardWidth; c++) {
                int tileState = boardState[r][c];
                if (0 != tileState) {
                    Paint tempColor = boardArray[r][c].getFill();
                    boardArray[r][c].setFill(defaultTileColor);
                    boardArray[r + 1][c].setFill(tempColor);
                    boardState[r][c] = 0;
                    boardState[r + 1][c] = tileState;
                }
            }
        }
    }

    private Tetrimino pickRandomBlock(Tetrimino[] blockSet) {
        int rand = new Random().nextInt(blockSet.length);
        return blockSet[rand];
    }
}
