package UserInterface;

import net.client.TetrisClient;
import net.server.TetrisServer;

import java.io.IOException;

import TetrisGame.TetrisBoard;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientView {
	private static TetrisClient client;
	private static TetrisServer server;
	private static TextArea ta;

	private static TetrisBoard player1Board;
	private static TetrisBoard player2Board;
	Stage primaryStage;
	Scene scene, gameScene;

	public ClientView(Stage primaryStage) {
		this.primaryStage = primaryStage;
		ta = new TextArea();
		ta.setEditable(false);
	}

	public void setClient(TetrisClient client) {
		this.client = client;
	}

	public void setServer(TetrisServer server) {
		this.server = server;
	}

	public void init() {
		player1Board = new TetrisBoard(client);
		player2Board = new TetrisBoard(client);

		VBox pane = new VBox();
		pane.setPadding(new Insets(10));
		pane.setSpacing(10);

		scene = new Scene(pane);

		VBox gameVbox = new VBox(10);
		gameVbox.setPadding(new Insets(20));
		Button quitBtn = new Button("Quit");
		quitBtn.setOnAction(e -> {
			client.stopClient();
			primaryStage.setScene(scene);
		});
		HBox gameHbox = new HBox(100);
		StackPane stackPane1 = new StackPane();
		stackPane1.getChildren().add(player1Board.pane);
		StackPane stackPane2 = new StackPane();
		stackPane2.getChildren().add(player2Board.pane);
		gameHbox.getChildren().addAll(stackPane1, stackPane2);
		gameVbox.getChildren().addAll(quitBtn, gameHbox);
		gameScene = new Scene(gameVbox);

		gameScene.setOnKeyPressed(e -> {
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

		Button joinBtn = new Button("Join Game");
		joinBtn.setOnAction(event -> {
			try {
				client.joinGame();
				primaryStage.setScene(gameScene);
				gameHbox.requestFocus();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		pane.getChildren().addAll(ta, joinBtn);

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

	public void close() {
		if (client != null) client.stopClient();
		if (server != null) server.stopServer();
		player1Board.stageClosed = true;
		player2Board.stageClosed = true;
	}

	public void appendText(String text) {
		ta.appendText(text);
	}

	public void startGame() {
		player1Board.startGame();
	}

	public void receiveBoardState(int[][] boardState) {
		player2Board.setBoardState(boardState);
	}

	public void setOpponentLose() {
		player2Board.setLose();
	}

	public void setOpponentWin() {
		player2Board.setWin();
	}

	public void setClientWin() {
		player1Board.setWin();
	}
}
