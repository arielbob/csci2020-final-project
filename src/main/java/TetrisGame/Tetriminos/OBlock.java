package TetrisGame.Tetriminos;

import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

public class OBlock extends Tetrimino {
    public OBlock() {
        paint = Color.YELLOW;
        movingNum = 4;
        placedNum = 8*movingNum;
        spawnPointY = -2;
        spawnPointX = 4;
        rotationsArray[0] = new int[][] {
            {1, 1},
            {1, 1}
        };
        rotationsArray[1] = new int[][] {
            {1, 1},
            {1, 1}
        };
        rotationsArray[2] = new int[][] {
            {1, 1},
            {1, 1}
        };
        rotationsArray[3] = new int[][] {
            {1, 1},
            {1, 1}
        };
    }
}
