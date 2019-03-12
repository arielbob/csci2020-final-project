package net.test;

import net.server.Server;

import java.net.DatagramPacket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.server.TetrisServer;

// TODO: add parsing packets
// packet parsing should be hidden away in a Server subclass i.e. TetrisServer
public class ServerTest extends Application {
	private static TetrisServer server;
	private static TextArea ta;

	public static void main(String[] args) {
		ta = new TextArea();
		ta.setEditable(false);

		try {
			server = new TetrisServer(61616, (DatagramPacket p) -> {
				String message = new String(p.getData(), StandardCharsets.US_ASCII);
				ta.appendText("[PACKET DATA]: " + message + '\n');
			});
			server.start();
		} catch (SocketException e) {
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

		Label label = new Label("Messages");

		pane.getChildren().addAll(label, ta);

		primaryStage.setTitle("Server Test");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(event -> {
			server.stopServer();
		});
	}

}
