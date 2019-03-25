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

public class JoinServerScene {
	private Scene scene;
	private VBox pane;

	//public JoinServerScene(Stage primaryStage) {
	public JoinServerScene(Stage primaryStage, Pane prevRoot) {
		//VBox pane = new VBox();
		pane = new VBox();
		pane.setPrefSize(400, 250);
		pane.setPadding(new Insets(20));
		pane.setSpacing(10);

		Field usernameField = new Field("Username");
		Field addressField = new Field("Server Address");
		Field portField = new Field("Port");

		Label serverError = new Label();
		serverError.setVisible(false);

		Button joinServerBtn = new Button("Join Server");
		joinServerBtn.setPadding(new Insets(10));
		joinServerBtn.setOnAction(event -> {
			usernameField.hideError();
			addressField.hideError();
			portField.hideError();
			serverError.setVisible(false);

			String username = usernameField.getFieldText().trim();
			String addressString = addressField.getFieldText().trim();
			int port = -1;
			boolean hasErrors = false;
			InetAddress address = null;

			if (addressString.isEmpty()) {
				addressField.setError("Please enter a server address");
				hasErrors = true;
			} else {
				try {
					address = InetAddress.getByName(addressField.getFieldText().trim());
				} catch (UnknownHostException e) {
					serverError.setText("Could not connect to server");
					serverError.setVisible(true);
					hasErrors = true;
				}
			}

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
				TetrisClient client = new TetrisClient(address, port, clientView);
				clientView.setClient(client);
				clientView.init();

				System.out.println(port);
				client.start();
				client.connect(username);
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
		buttonHbox.getChildren().addAll(joinServerBtn, cancelBtn);

		pane.getChildren().addAll(usernameField, addressField, portField, buttonHbox, serverError);

		//this.scene = new Scene(pane);
	}

	public Scene getScene() {
		return scene;
	}

	public Pane getRoot() {
		return pane;
	}
}
