package LukeTetrisPrototype;

import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

public class OBlock extends Tetrimino {
    public OBlock() {
        paint = Color.YELLOW;
        rotationState[0] = new int[][] {
            {1, 1},
            {1, 1}
        };
        rotationState[1] = new int[][] {
            {1, 1},
            {1, 1}
        };
        rotationState[2] = new int[][] {
            {1, 1},
            {1, 1}
        };
        rotationState[3] = new int[][] {
            {1, 1},
            {1, 1}
        };
    }
}
