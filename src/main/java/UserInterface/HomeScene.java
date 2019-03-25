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
	Scene firstScene;
	TetrisBoard board = new TetrisBoard();
	FileManager fileManager = new FileManager();
	final double MIN_WIDTH = 400;

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

		Button instructionBtn = new Button("HOW TO PLAY");
		instructionBtn.setAlignment(Pos.CENTER);
		instructionBtn.setPrefWidth(100);
		instructionBtn.setPrefHeight(50);
		root.add(instructionBtn, 0, 3);

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
			firstScene.setRoot(new CreateServerScene(primaryStage, root2).getRoot());
		});

		Button joinBtn = new Button("JOIN GAME");
		joinBtn.setAlignment(Pos.CENTER);
		joinBtn.setPrefWidth(100);
		joinBtn.setPrefHeight(50);
		root2.add(joinBtn, 0, 1);

		joinBtn.setOnAction(event -> {
			firstScene.setRoot(new JoinServerScene(primaryStage, root2).getRoot());
		});

		Button backBtn = new Button("BACK");
		backBtn.setAlignment(Pos.CENTER);
		backBtn.setPrefWidth(100);
		backBtn.setPrefHeight(50);
		root2.add(backBtn, 0, 2);

		firstScene = new Scene(root);
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
				else if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.X) {
					board.rotateTetrimino("cw");
				}
				else if (e.getCode() == KeyCode.Z) {
					board.rotateTetrimino("ccw");
				}
			});

			VBox gameVbox = new VBox();
			gameVbox.setAlignment(Pos.CENTER);
			firstScene.setRoot(gameVbox);
			gameVbox.getChildren().add(boardHbox);
			gameVbox.requestFocus();
			board.startGame();
		});

		statsBtn.setOnAction(e -> {
			GridPane statsPane = new GridPane();
			statsPane.setVgap(20);
			statsPane.setAlignment(Pos.CENTER);

			int singlePlayerPlayed = fileManager.getSinglePlayerPlayed();
			Label singlePlayerPlayedLbl = new Label("Single player games played: " + singlePlayerPlayed);
			singlePlayerPlayedLbl.setAlignment(Pos.CENTER);
			statsPane.add(singlePlayerPlayedLbl, 0, 0);

			int multiplayerPlayed = fileManager.getMultiplayerPlayed();
			Label multiplayerPlayedLbl = new Label("Multiplayer games played: " + multiplayerPlayed);
			multiplayerPlayedLbl.setAlignment(Pos.CENTER);
			statsPane.add(multiplayerPlayedLbl, 0, 1);

			int multiplayerWon = fileManager.getMultiplayerWon();
			Label multiplayerWonLbl = new Label("Multiplayer games won: " + multiplayerWon);
			multiplayerWonLbl.setAlignment(Pos.CENTER);
			statsPane.add(multiplayerWonLbl, 0, 2);

			int linesCleared = fileManager.getLinesCleared();
			Label linesClearedLbl = new Label("Total lines cleared: " + linesCleared);
			linesClearedLbl.setAlignment(Pos.CENTER);
			statsPane.add(linesClearedLbl, 0, 3);

			Button backToMenuBtn = new Button("BACK");
			backToMenuBtn.setAlignment(Pos.CENTER);
			backToMenuBtn.setPrefWidth(100);
			backToMenuBtn.setPrefHeight(50);
			statsPane.add(backToMenuBtn, 0, 4);
			firstScene.setRoot(statsPane);
			backToMenuBtn.setOnAction(event -> {
				firstScene.setRoot(root);
			});
		});

		instructionBtn.setOnAction(e -> {
			GridPane instructionPane = new GridPane();
			instructionPane.setVgap(20);
			instructionPane.setAlignment(Pos.CENTER);

			Label arrowControlLbl = new Label("Control the falling block with the arrow keys");
			arrowControlLbl.setAlignment(Pos.CENTER);
			instructionPane.add(arrowControlLbl, 0, 0);

			Label arrowDownLbl = new Label("Press the down arrow key to make the block fall faster");
			arrowDownLbl.setAlignment(Pos.CENTER);
			instructionPane.add(arrowDownLbl, 0, 1);

			Label cwLbl = new Label("Press SPACE or X to rotate the block clockwise");
			cwLbl.setAlignment(Pos.CENTER);
			instructionPane.add(cwLbl, 0, 2);

			Label ccwLbl = new Label("Press Z to rotate the block counterclockwise");
			ccwLbl.setAlignment(Pos.CENTER);
			instructionPane.add(ccwLbl, 0, 3);

			Label fillRowsLbl = new Label("Fill out rows to clear them out");
			fillRowsLbl.setAlignment(Pos.CENTER);
			instructionPane.add(fillRowsLbl, 0, 4);

			Button backToMenuBtn = new Button("BACK");
			backToMenuBtn.setAlignment(Pos.CENTER);
			backToMenuBtn.setPrefWidth(100);
			backToMenuBtn.setPrefHeight(50);
			instructionPane.add(backToMenuBtn, 0, 5);
			firstScene.setRoot(instructionPane);
			backToMenuBtn.setOnAction(event -> {
				firstScene.setRoot(root);
			});
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
