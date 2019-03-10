package net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientTest extends Application {
	private static Client client;

	public static void main(String[] args) {
		try {
			client = new Client(InetAddress.getByName("localhost"), 61616);
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}

		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
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

		pane.getChildren().addAll(label, tf, btn);

		primaryStage.setTitle("Client Test");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
