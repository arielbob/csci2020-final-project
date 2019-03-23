package UserInterface;

import net.server.TetrisServer;

import java.net.SocketException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class CreateServerScene {
	private Scene scene;

	public CreateServerScene(Stage primaryStage) {
		VBox pane = new VBox();
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
				TetrisServer server = new TetrisServer(port);
				System.out.println(port);
				server.start();
			} catch (SocketException e) {
				serverError.setText("Port is already in use, please enter a different port");
				serverError.setVisible(true);
			} catch (IllegalArgumentException e) {
				serverError.setText("Please enter a port between 0 and 65535 (inclusive)");
				serverError.setVisible(true);
			}
		});


		pane.getChildren().addAll(usernameField, portField, createServerBtn, serverError);

		this.scene = new Scene(pane);
	}

	public Scene getScene() {
		return scene;
	}
}
