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

import TetrisPrototype.TetrisBoard;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.input.KeyCode;

public class ClientTest extends Application {
	private static TetrisClient client;
	private static TextArea ta;

	TetrisBoard player1Board = new TetrisBoard(this);
	TetrisBoard player2Board = new TetrisBoard(this);
	Stage playerWindow;
	Scene scene, gameScene;

	public static void main(String[] args) {
		ta = new TextArea();
		ta.setEditable(false);

		try {
			client = new TetrisClient(InetAddress.getByName("localhost"), 61616);
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
		VBox gameVbox = new VBox(10);
		gameVbox.setPadding(new Insets(20));
		Button quitBtn = new Button("Quit");
		quitBtn.setOnAction(e -> {
			client.stopClient();
			playerWindow.setScene(scene);
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

	public void sendBoardState(int[][] boardState) {
		try {
			client.sendBoard(boardState);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void receiveBoardState(int[][] boardState) {
		player2Board.setBoardState(boardState);
	}
}
