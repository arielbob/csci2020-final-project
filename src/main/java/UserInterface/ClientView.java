package UserInterface;

import net.client.TetrisClient;
import net.server.TetrisServer;
import FileManagement.FileManager;
import java.io.IOException;

import TetrisGame.TetrisBoard;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.geometry.Pos;

public class ClientView {
	private static TetrisClient client;
	private static TetrisServer server;
	private static TextArea ta;

	private static TetrisBoard player1Board;
	private static TetrisBoard player2Board;
	Stage primaryStage;
	Scene scene;
	FileManager fileManager = new FileManager();
	Button startButton;
	HBox gameHbox;

	public ClientView(Stage primaryStage) {
		this.primaryStage = primaryStage;
		ta = new TextArea();
		ta.setEditable(false);
		startButton = new Button("Start Game");
		startButton.setVisible(false);
	}

	public void setClient(TetrisClient client) {
		this.client = client;
	}

	public void setServer(TetrisServer server) {
		this.server = server;
		startButton.setVisible(true);
	}

	public void init() {
		player1Board = new TetrisBoard(client);
		player2Board = new TetrisBoard(client);

		HBox gamePane = new HBox();
		gamePane.setAlignment(Pos.CENTER);
		VBox gameVbox = new VBox(10);
		gameVbox.setPadding(new Insets(20));
		Button quitBtn = new Button("Quit");
		quitBtn.setOnAction(e -> {
			close();
//			primaryStage.setScene(scene);
		});

		gameHbox = new HBox(100);
		VBox vbox1 = new VBox();
		vbox1.setAlignment(Pos.CENTER);
		vbox1.getChildren().addAll(player1Board.pane, new Label("Player"));
		VBox vbox2 = new VBox();
		vbox2.setAlignment(Pos.CENTER);
		vbox2.getChildren().addAll(player2Board.pane, new Label("Opponent"));
		gameHbox.getChildren().addAll(vbox1, vbox2);
		gameHbox.requestFocus();

		gameVbox.getChildren().addAll(quitBtn, gameHbox, startButton);
		gamePane.getChildren().add(gameVbox);
		scene = new Scene(gamePane);

		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.LEFT) {
				player1Board.moveTetrimino("left");
			}
			else if (e.getCode() == KeyCode.RIGHT) {
				player1Board.moveTetrimino("right");
			}
			else if (e.getCode() == KeyCode.DOWN) {
				player1Board.moveTetrimino("down");
			}
			else if (e.getCode() == KeyCode.SPACE) {
				player1Board.rotateTetrimino();
			}
		});

		startButton.setOnAction(event -> {
			server.startGame();
			gameHbox.requestFocus();
		});

//		Button joinBtn = new Button("Join Game");
//		joinBtn.setOnAction(event -> {
//			try {
//				client.joinGame();
//				primaryStage.setScene(gameScene);
//				gameHbox.requestFocus();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		});

//		pane.getChildren().addAll(ta, joinBtn);

		this.scene = scene;
//		primaryStage.setScene(scene);
//		primaryStage.setOnCloseRequest(event -> {
//			client.stopClient();
//			player1Board.stageClosed = true;
//			player2Board.stageClosed = true;
//		});
	}

	public Scene getScene() {
		return scene;
	}

	// when we receive our ID, we know we connected and so we change scenes
	public void setConnected() {
		try {
			client.joinGame();
			gameHbox.requestFocus();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Platform.runLater(() -> {
			primaryStage.setScene(scene);
		});
	}

	public void close() {
		if (client != null) client.stopClient();
		if (server != null) server.stopServer();
		player1Board.stageClosed = true;
		player2Board.stageClosed = true;
		primaryStage.setScene(new HomeScene(primaryStage).getScene());
	}

	public void serverClose() {
		player1Board.stageClosed = true;
		player2Board.stageClosed = true;

		Platform.runLater(() -> {
			primaryStage.setScene(new HomeScene(primaryStage).getScene());
		});
	}

	public void appendText(String text) {
		ta.appendText(text);
	}

	public void startGame() {
		player1Board.startGame();
		startButton.setVisible(false);
	}

	public void receiveBoardState(int[][] boardState) {
		player2Board.setBoardState(boardState);
	}

	public void setOpponentLose() {
		player2Board.setLose();
		showStartButton();
	}

	public void setOpponentWin() {
		player2Board.setWin();
		showStartButton();
		fileManager.incrementMultiplayer();
	}

	public void setClientWin() {
		player1Board.setWin();
		showStartButton();
		fileManager.incrementMultiplayer();
		fileManager.incrementGamesWon();
	}

	private void showStartButton() {
		if (server != null) startButton.setVisible(true);
	}
}
