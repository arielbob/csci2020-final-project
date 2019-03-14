package TetrisPrototype;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

public class Tetrimino {
    Paint paint;
    int spawnPointY = 2;
    int spawnPointX = 3;
    int[][][] rotationsArray = new int[4][][];
    private int rotationState = 0;

    public int getRotationState() {
        return rotationState;
    }

    public void setRotationState(int newState) {
        rotationState = newState % 4;
    }
}
