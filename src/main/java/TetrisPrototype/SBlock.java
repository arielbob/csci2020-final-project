package TetrisPrototype;

import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

public class SBlock extends Tetrimino {
    public SBlock() {
        paint = Color.web("#00ff1d");
        rotationsArray[0] = new int[][] {
            {0, 1, 1},
            {1, 1, 0},
            {0, 0, 0}
        };
        rotationsArray[1] = new int[][] {
            {0, 1, 0},
            {0, 1, 1},
            {0, 0, 1}
        };
        rotationsArray[2] = new int[][] {
            {0, 0, 0},
            {0, 1, 1},
            {1, 1, 0}
        };
        rotationsArray[3] = new int[][] {
            {1, 0, 0},
            {1, 1, 0},
            {0, 1, 0}
        };
    }
}
