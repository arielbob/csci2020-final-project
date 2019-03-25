package UserInterface;

import net.client.TetrisClient;
import net.server.TetrisServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class CreateServerScene {
	private Scene scene;
	private VBox pane;

	//public CreateServerScene(Stage primaryStage) {
	public CreateServerScene(Stage primaryStage, Pane prevRoot) {
		//VBox pane = new VBox();
		pane = new VBox();
		pane.setPrefSize(400, 250);
		pane.setPadding(new Insets(20));
		pane.setSpacing(10);

		Field usernameField = new Field("Username");
		Field portField = new Field("Port");

		Label serverError = new Label();
		serverError.setVisible(false);

		Button createServerBtn = new Button("Create Server");
		createServerBtn.setPadding(new Insets(10));
		createServerBtn.setOnAction(event -> {
			usernameField.hideError();
			portField.hideError();
			serverError.setVisible(false);

			String username = usernameField.getFieldText().trim();
			int port = -1;
			boolean hasErrors = false;

			try {
				port = Integer.parseInt(portField.getFieldText().trim());
			} catch (NumberFormatException e) {
				portField.setError("Please enter a valid port number");
				hasErrors = true;
			}

			if (username.isEmpty()) {
				usernameField.setError("Please enter a username");
				hasErrors = true;
			}

			if (hasErrors) return;

			try {
				ClientView clientView = new ClientView(primaryStage);
				TetrisServer server = new TetrisServer(port, clientView);
				TetrisClient client = new TetrisClient(InetAddress.getByName("localhost"), port, clientView);
				clientView.setServer(server);
				clientView.setClient(client);
				clientView.init();

				primaryStage.setOnCloseRequest(event1 -> clientView.close());

				System.out.println(port);
				server.start();
				client.start();
				client.connect();
			} catch (SocketException e) {
				serverError.setText("Port is already in use, please enter a different port");
				serverError.setVisible(true);
			} catch (IllegalArgumentException e) {
				serverError.setText("Please enter a port between 0 and 65535 (inclusive)");
				serverError.setVisible(true);
			} catch (UnknownHostException e) {
				serverError.setText("Could not connect to server");
				serverError.setVisible(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		Button cancelBtn = new Button("Cancel");
		cancelBtn.setPadding(new Insets(10));
		cancelBtn.setOnAction(event -> {
			primaryStage.getScene().setRoot(prevRoot);
		});
		HBox buttonHbox = new HBox(10);
		buttonHbox.getChildren().addAll(createServerBtn, cancelBtn);

		pane.getChildren().addAll(usernameField, portField, buttonHbox, serverError);

		//this.scene = new Scene(pane);
	}

	public Scene getScene() {
		return scene;
	}

	public Pane getRoot() {
		return pane;
	}
}
