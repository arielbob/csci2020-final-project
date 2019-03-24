package TetrisGame.EndBoards;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import javafx.concurrent.Task;

public class EndBoard {
    private final int HIDDEN_ROWS = 3;
    private final int BOARD_HEIGHT = 24 - HIDDEN_ROWS;
    private final int BOARD_WIDTH = 10;
    Paint paint;
    int[][] board = new int[BOARD_HEIGHT][BOARD_WIDTH];

    public int[][] getBoard() {
        return board;
    }

    public void setEndBoard(int[][] board) {
        this.board = board;
    }

    public void drawBoard(Rectangle[][] rectArray) {
        for (int r = BOARD_HEIGHT - 1; r >= 0 ; r--) {
            for (int c = 0; c < BOARD_WIDTH; c++) {
                switch (board[r][c]) {
                    case 0:
                        rectArray[r + HIDDEN_ROWS][c].setFill(Color.BLACK);
                        break;
                    case 1:
                        rectArray[r + HIDDEN_ROWS][c].setFill(paint);
                        break;
                }
            }
        }
    }

    public void animateBoard(Rectangle[][] rectArray) {
        Task<Void> task = new Task<Void>() {
            @Override protected Void call() throws Exception {
                for (int r = BOARD_HEIGHT - 1; r >= 0 ; r--) {
                    for (int c = 0; c < BOARD_WIDTH; c++) {
                        switch (board[r][c]) {
                            case 0:
                                rectArray[r + HIDDEN_ROWS][c].setFill(Color.BLACK);
                                break;
                            case 1:
                                rectArray[r + HIDDEN_ROWS][c].setFill(paint);
                                break;
                        }
                    }

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException interrupted) {
                        System.out.println("Drawing Interrupted!");
                        break;
                    }
                }
                return null;
            }
        };
        new Thread(task).start();
    }
}
