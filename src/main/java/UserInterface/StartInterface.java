package UserInterface;

import LukeTetrisPrototype.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

public class StartInterface extends Application {

  Stage window;
  Scene firstScene, secondScene;
  TetrisBoard board = new TetrisBoard();

	@Override
	public void start(Stage primaryStage) throws Exception {

    window = primaryStage;

    //First Scene Code below
    GridPane root = new GridPane();
		root.setAlignment(Pos.CENTER);
    root.setMinWidth(250);
    root.setMaxWidth(250);
    root.setMinHeight(250);
    root.setMaxHeight(250);
    root.setVgap(20);

    Button soloBtn = new Button("       SOLO       ");
    soloBtn.setAlignment(Pos.CENTER);
    soloBtn.setPrefWidth(100);
    soloBtn.setPrefHeight(50);
    root.add(soloBtn, 0, 0);

    Button multiBtn = new Button("MULTIPLAYER");
    multiBtn.setAlignment(Pos.CENTER);
    multiBtn.setPrefWidth(100);
    multiBtn.setPrefHeight(50);
    root.add(multiBtn, 0, 1);

    //Second Scene Code below
    GridPane root2 = new GridPane();
    root2.setAlignment(Pos.CENTER);
    root2.setMinWidth(250);
    root2.setMaxWidth(250);
    root2.setMinHeight(250);
    root2.setMaxHeight(250);
    root2.setVgap(20);

    Button createBtn = new Button("CREATE GAME");
    createBtn.setAlignment(Pos.CENTER);
    createBtn.setPrefWidth(100);
    createBtn.setPrefHeight(50);
    root2.add(createBtn, 0, 0);

    Button joinBtn = new Button("JOIN GAME");
    joinBtn.setAlignment(Pos.CENTER);
    joinBtn.setPrefWidth(100);
    joinBtn.setPrefHeight(50);
    root2.add(joinBtn, 0, 1);

    Button backBtn = new Button("BACK");
    backBtn.setAlignment(Pos.CENTER);
    backBtn.setPrefWidth(100);
    backBtn.setPrefHeight(50);
    root2.add(backBtn, 0, 2);

    Scene firstScene = new Scene(root);
    Scene secondScene = new Scene(root2);
    window.setTitle("MultiTetris7");
    window.setScene(firstScene);
    window.show();

		//action handler for when button is clicked
		soloBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
        board = new TetrisBoard();

        Stage gameStage = new Stage();

        Pane pane = new Pane();
        Scene scene = new Scene(pane);
        pane.setPrefSize(500, 500);

        pane.getChildren().add(board.pane);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                int[][] movingTiles = setNewBlockLocation("left");
                board.moveTetrimino(movingTiles, "horizontal");
            }
            if (e.getCode() == KeyCode.RIGHT) {
                int[][] movingTiles = setNewBlockLocation("right");
                board.moveTetrimino(movingTiles, "horizontal");
            }
            if (e.getCode() == KeyCode.DOWN) {
                int[][] movingTiles = setNewBlockLocation("down");
                board.moveTetrimino(movingTiles, "vertical");
            }
            if (e.getCode() == KeyCode.SPACE) {
                board.rotateTetrimino();
            }
        });

        gameStage.setTitle("Tetris Prototype");
        gameStage.setScene(scene);
        gameStage.show();

			}
		});

    multiBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        window.setScene(secondScene);
      }
    });

    backBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        window.setScene(firstScene);
      }
    });

	}

  public int[][] setNewBlockLocation(String direction) {
      int dir = 0;
      int xory = 0;
      if (direction.equals("left")) {
          xory = 1;
          dir = -1;
      }
      else if (direction.equals("right")) {
          xory = 1;
          dir = 1;
      }
      else if (direction.equals("down")) {
          xory = 0;
          dir = 1;
      }

      int[][] movingTiles = new int[4][2];
      for (int pair = 0; pair < board.occupiedTiles.length; pair++) {
          for (int xy = 0; xy < board.occupiedTiles[pair].length; xy++) {
              movingTiles[pair][xy] = board.occupiedTiles[pair][xy];
              if (xy == xory) {
                  movingTiles[pair][xy] += dir;
              }
          }
      }
      return movingTiles;
  }

	public static void main(String[] args) {
		launch();
	}

}
