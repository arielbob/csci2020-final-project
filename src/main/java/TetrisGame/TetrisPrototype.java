package TetrisGame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class TetrisPrototype extends Application {
    TetrisBoard board = new TetrisBoard();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(board.pane);
        vbox.getChildren().add(hbox);
        Scene scene = new Scene(vbox);
        vbox.setPrefSize(400, 400);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                board.moveTetrimino("left");
            }
            if (e.getCode() == KeyCode.RIGHT) {
                board.moveTetrimino("right");
            }
            if (e.getCode() == KeyCode.DOWN) {
                board.moveTetrimino("down");
            }
            else if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.X) {
				board.rotateTetrimino("cw");
			}
			else if (e.getCode() == KeyCode.Z) {
				board.rotateTetrimino("ccw");
			}
        });
		board.startGame();

        primaryStage.setTitle("Tetris Prototype");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.out.println("Stage is closing");
        board.stageClosed = true;
    }
}
