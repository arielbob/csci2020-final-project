package TetrisGame.Tetriminos;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;

public class Tetrimino {
    Paint paint;
    int movingNum;
    int placedNum;
    int spawnPointY = -2;
    int spawnPointX = 3;
    int[][][] rotationsArray = new int[4][][];
    private int rotationState = 0;

    public Paint getPaint() {
        return paint;
    }

    public int getMovingNum() {
        return movingNum;
    }

    public int getPlacedNum() {
        return placedNum;
    }

    public int getSpawnPointY() {
        return spawnPointY;
    }

    public int getSpawnPointX() {
        return spawnPointX;
    }

    public int getRotationState() {
        return rotationState;
    }

    public int[][] getRotationsArray(int i) {
        return rotationsArray[i];
    }

    public void setRotationState(int newState) {
        rotationState = newState % 4;
    }
}
