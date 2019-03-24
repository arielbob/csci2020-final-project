package TetrisGame.Tetriminos;

import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

public class JBlock extends Tetrimino {
    public JBlock() {
        paint = Color.BLUE;
        movingNum = 2;
        placedNum = 8*movingNum;
        rotationsArray[0] = new int[][] {
            {1, 0, 0},
            {1, 1, 1},
            {0, 0, 0}
        };
        rotationsArray[1] = new int[][] {
            {0, 1, 1},
            {0, 1, 0},
            {0, 1, 0}
        };
        rotationsArray[2] = new int[][] {
            {0, 0, 0},
            {1, 1, 1},
            {0, 0, 1}
        };
        rotationsArray[3] = new int[][] {
            {0, 1, 0},
            {0, 1, 0},
            {1, 1, 0}
        };
    }
}
