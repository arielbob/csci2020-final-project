package TetrisPrototype;

import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

public class ZBlock extends Tetrimino {
    public ZBlock() {
        paint = Color.RED;
        rotationsArray[0] = new int[][] {
            {1, 1, 0},
            {0, 1, 1},
            {0, 0, 0}
        };
        rotationsArray[1] = new int[][] {
            {0, 0, 1},
            {0, 1, 1},
            {0, 1, 0}
        };
        rotationsArray[2] = new int[][] {
            {0, 0, 0},
            {1, 1, 0},
            {0, 1, 1}
        };
        rotationsArray[3] = new int[][] {
            {0, 1, 0},
            {1, 1, 0},
            {1, 0, 0}
        };
    }
}
