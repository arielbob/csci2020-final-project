package net.test;

import net.client.TetrisClient;
import net.packet.MessagePacket;
import net.packet.PacketType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import TetrisGame.TetrisBoard;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.input.KeyCode;

public class ClientTest extends Application {
	private static TetrisClient client;
	private static TextArea ta;

	private static TetrisBoard player1Board;
	private static TetrisBoard player2Board;
	Stage playerWindow;
	Scene scene, gameScene;

	public static void main(String[] args) {
		ta = new TextArea();
		ta.setEditable(false);

		try {
			client = new TetrisClient(InetAddress.getByName("localhost"), 61616);
			player1Board = new TetrisBoard(client);
			player2Board = new TetrisBoard(client);
			client.start();
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}

		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		client.setView(this);
		client.connect();

		VBox pane = new VBox();
		pane.setPadding(new Insets(10));
		pane.setSpacing(10);

		scene = new Scene(pane);

		Label label = new Label("Enter message:");

		TextField tf = new TextField();
		tf.setPromptText("Message");

		Button btn = new Button("Send Message");
		btn.setDefaultButton(true);
		btn.setOnAction(event -> {
			try {
				client.sendMessage(tf.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
			tf.clear();
		});


		playerWindow = primaryStage;
		HBox gamePane = new HBox();
		gamePane.setAlignment(Pos.CENTER);
		VBox gameVbox = new VBox(10);
		gameVbox.setPadding(new Insets(20));
		Button quitBtn = new Button("Quit");
		quitBtn.setOnAction(e -> {
			client.stopClient();
			playerWindow.setScene(scene);
		});
		HBox gameHbox = new HBox(100);
		//StackPane stackPane1 = new StackPane();
		VBox vbox1 = new VBox();
		vbox1.setAlignment(Pos.CENTER);
		//stackPane1.getChildren().add(player1Board.pane);
		vbox1.getChildren().addAll(player1Board.pane, new Label("Player"));
		//StackPane stackPane2 = new StackPane();
		VBox vbox2 = new VBox();
		vbox2.setAlignment(Pos.CENTER);
		//stackPane2.getChildren().add(player2Board.pane);
		vbox2.getChildren().addAll(player2Board.pane, new Label("Opponent"));
		//gameHbox.getChildren().addAll(stackPane1, stackPane2);
		gameHbox.getChildren().addAll(vbox1, vbox2);
		gameVbox.getChildren().addAll(quitBtn, gameHbox);
		gamePane.getChildren().add(gameVbox);
		gameScene = new Scene(gamePane);

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
				playerWindow.setScene(gameScene);
				gameHbox.requestFocus();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		Label messagesLabel = new Label("Messages");

		pane.getChildren().addAll(label, tf, btn, messagesLabel, ta, joinBtn);

		primaryStage.setTitle("Client Test");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(event -> {
			client.stopClient();
			player1Board.stageClosed = true;
			player2Board.stageClosed = true;
		});
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
