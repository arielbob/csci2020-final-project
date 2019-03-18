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

import TetrisPrototype.TetrisBoardMultiplayer;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.input.KeyCode;

public class ClientTest extends Application {
	private static TetrisClient client;
	private static TextArea ta;

	TetrisBoardMultiplayer player1Board = new TetrisBoardMultiplayer(this);
	TetrisBoardMultiplayer player2Board = new TetrisBoardMultiplayer(this);
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

		//Scene scene = new Scene(pane);
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
		HBox hbox = new HBox();
		StackPane stackPane1 = new StackPane();
		stackPane1.getChildren().add(player1Board.pane);
		StackPane stackPane2 = new StackPane();
		stackPane2.getChildren().add(player2Board.pane);
		hbox.getChildren().addAll(stackPane1, stackPane2);
		gameScene = new Scene(hbox);
		//pane.getChildren().add(hbox);

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
		//hbox.requestFocus();


		Button joinBtn = new Button("Join Game");
		joinBtn.setOnAction(event -> {
			try {
				client.joinGame();
				playerWindow.setScene(gameScene);
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
		//playerWindow.setScene(gameScene);
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
