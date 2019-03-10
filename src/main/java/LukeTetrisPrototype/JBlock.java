package LukeTetrisPrototype;

import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

public class JBlock extends Tetrimino {
    public JBlock() {
        paint = Color.BLUE;
        rotationState[0] = new int[][] {
            {1, 0, 0},
            {1, 1, 1},
            {0, 0, 0}
        };
        rotationState[1] = new int[][] {
            {0, 1, 1},
            {0, 1, 0},
            {0, 1, 0}
        };
        rotationState[2] = new int[][] {
            {0, 0, 0},
            {1, 1, 1},
            {0, 0, 1}
        };
        rotationState[3] = new int[][] {
            {0, 1, 0},
            {0, 1, 0},
            {1, 1, 0}
        };
    }
}
