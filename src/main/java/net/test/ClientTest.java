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

public class ClientTest extends Application {
	private static TetrisClient client;
	private static TextArea ta;

	TetrisBoardMultiplayer player1Board = new TetrisBoardMultiplayer();
	TetrisBoardMultiplayer player2Board = new TetrisBoardMultiplayer();

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

		Scene scene = new Scene(pane);

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

		Button joinBtn = new Button("Join Game");
		joinBtn.setOnAction(event -> {
			try {
				client.joinGame();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		Label messagesLabel = new Label("Messages");

		pane.getChildren().addAll(label, tf, btn, messagesLabel, ta, joinBtn);

		HBox hbox = new HBox();
		//TetrisBoardMultiplayer player1Board = new TetrisBoardMultiplayer();
		StackPane stackPane1 = new StackPane();
		stackPane1.getChildren().add(player1Board.pane);
		//TetrisBoardMultiplayer player2Board = new TetrisBoardMultiplayer();
		StackPane stackPane2 = new StackPane();
		stackPane2.getChildren().add(player2Board.pane);
		hbox.getChildren().addAll(stackPane1, stackPane2);
		pane.getChildren().add(hbox);

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
}
