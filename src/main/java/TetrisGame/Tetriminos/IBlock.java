package TetrisGame.Tetriminos;

import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

public class IBlock extends Tetrimino {
    public IBlock() {
        paint = Color.AQUA;
        movingNum = 1;
        placedNum = 8*movingNum;
        rotationsArray[0] = new int[][] {
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        };
        rotationsArray[1] = new int[][] {
            {0, 0, 1, 0},
            {0, 0, 1, 0},
            {0, 0, 1, 0},
            {0, 0, 1, 0}
        };
        rotationsArray[2] = new int[][] {
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0}
        };
        rotationsArray[3] = new int[][] {
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0}
        };
    }
}
