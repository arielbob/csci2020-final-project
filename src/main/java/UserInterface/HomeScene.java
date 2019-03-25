package UserInterface;

import FileManagement.FileManager;
import TetrisGame.TetrisBoard;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeScene {
	Scene firstScene;// = new Scene(root);
	TetrisBoard board = new TetrisBoard();
	FileManager fileManager = new FileManager();
	final double MIN_WIDTH = 400;
	final double MIN_HEIGHT = 500;

	public HomeScene(Stage primaryStage) {
		GridPane root = new GridPane();
		root.setAlignment(Pos.CENTER);
		root.setMinWidth(MIN_WIDTH);
		root.setMinHeight(MIN_WIDTH);
		root.setVgap(20);

		Button soloBtn = new Button("SOLO");
		soloBtn.setAlignment(Pos.CENTER);
		soloBtn.setPrefWidth(100);
		soloBtn.setPrefHeight(50);
		root.add(soloBtn, 0, 0);

		Button multiBtn = new Button("MULTIPLAYER");
		multiBtn.setAlignment(Pos.CENTER);
		multiBtn.setPrefWidth(100);
		multiBtn.setPrefHeight(50);
		root.add(multiBtn, 0, 1);

		Button statsBtn = new Button("VIEW STATS");
		statsBtn.setAlignment(Pos.CENTER);
		statsBtn.setPrefWidth(100);
		statsBtn.setPrefHeight(50);
		root.add(statsBtn, 0, 2);

		GridPane root2 = new GridPane();
		root2.setAlignment(Pos.CENTER);
		root2.setMinWidth(MIN_WIDTH);
		root2.setMinHeight(MIN_WIDTH);
		root2.setVgap(20);

		Button createBtn = new Button("CREATE GAME");
		createBtn.setAlignment(Pos.CENTER);
		createBtn.setPrefWidth(100);
		createBtn.setPrefHeight(50);
		root2.add(createBtn, 0, 0);

		createBtn.setOnAction(event -> {
			//primaryStage.setScene(new CreateServerScene(primaryStage).getScene());
			firstScene.setRoot(new CreateServerScene(primaryStage, root2).getRoot());
		});

		Button joinBtn = new Button("JOIN GAME");
		joinBtn.setAlignment(Pos.CENTER);
		joinBtn.setPrefWidth(100);
		joinBtn.setPrefHeight(50);
		root2.add(joinBtn, 0, 1);

		joinBtn.setOnAction(event -> {
			primaryStage.setScene(new JoinServerScene(primaryStage).getScene());
		});

		Button backBtn = new Button("BACK");
		backBtn.setAlignment(Pos.CENTER);
		backBtn.setPrefWidth(100);
		backBtn.setPrefHeight(50);
		root2.add(backBtn, 0, 2);


		//Scene firstScene = new Scene(root);
		firstScene = new Scene(root);
		//Scene secondScene = new Scene(root2);
		primaryStage.setTitle("Group 7 Tetris");
		primaryStage.setScene(firstScene);
		primaryStage.show();

		// Action handler for when button is clicked
		soloBtn.setOnAction(event -> {
			fileManager.incrementSinglePlayer();

			Button quitBtn = new Button("Quit");
			quitBtn.setOnAction(e -> {
				board.gameOver = true;
				firstScene.setRoot(root);
			});

			VBox boardVbox = new VBox();
			boardVbox.setAlignment(Pos.CENTER);
			boardVbox.getChildren().addAll(board.pane, new Label("Player"));

			VBox alignVbox = new VBox(10);
			alignVbox.setAlignment(Pos.CENTER_LEFT);
			alignVbox.getChildren().addAll(quitBtn, boardVbox);

			HBox boardHbox = new HBox();
			boardHbox.setAlignment(Pos.CENTER);
			boardHbox.getChildren().add(alignVbox);

			firstScene.setOnKeyPressed(e -> {
				if (e.getCode() == KeyCode.LEFT) {
					board.moveTetrimino("left");
				}
				else if (e.getCode() == KeyCode.RIGHT) {
					board.moveTetrimino("right");
				}
				else if (e.getCode() == KeyCode.DOWN) {
					board.moveTetrimino("down");
				}
				else if (e.getCode() == KeyCode.SPACE) {
					board.rotateTetrimino();
				}
			});

			VBox gameVbox = new VBox();//new VBox(10);
			gameVbox.setAlignment(Pos.CENTER);
			firstScene.setRoot(gameVbox);
			gameVbox.getChildren().add(boardHbox);
			gameVbox.requestFocus();
			board.startGame();
		});

		statsBtn.setOnAction(e -> {
			int singlePlayerPlayed = fileManager.getSinglePlayerPlayed();
			System.out.println(singlePlayerPlayed);
			int multiplayerPlayed;
			int multiplayerWon;
			int linesCleared;
		});

		multiBtn.setOnAction(e -> {
			firstScene.setRoot(root2);
		});

		backBtn.setOnAction(e -> {
			firstScene.setRoot(root);
		});
	}

	public void close() {
		board.stageClosed = true;
	}

	public Scene getScene() {
		return firstScene;
	}
}
