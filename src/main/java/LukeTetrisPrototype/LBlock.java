package LukeTetrisPrototype;

import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

public class LBlock extends Tetrimino {
    public LBlock() {
        paint = Color.ORANGE;
        rotationState[0] = new int[][] {
            {0, 0, 1},
            {1, 1, 1},
            {0, 0, 0}
        };
        rotationState[1] = new int[][] {
            {0, 1, 0},
            {0, 1, 0},
            {0, 1, 1}
        };
        rotationState[2] = new int[][] {
            {0, 0, 0},
            {1, 1, 1},
            {1, 0, 0}
        };
        rotationState[3] = new int[][] {
            {1, 1, 0},
            {0, 1, 0},
            {0, 1, 0}
        };
    }
}
