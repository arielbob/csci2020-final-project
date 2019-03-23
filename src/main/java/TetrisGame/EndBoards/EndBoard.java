package TetrisGame.EndBoards;

import javafx.scene.shape.Rectangle;

public class EndBoard {
    private final int BOARD_HEIGHT = 21;
    private final int BOARD_WIDTH = 10;
    int[][] board = new int[BOARD_HEIGHT][BOARD_WIDTH];

    public int[][] getBoard() {
        return board;
    }

    public void setEndBoard(int[][] board) {
        this.board = board;
    }
}
