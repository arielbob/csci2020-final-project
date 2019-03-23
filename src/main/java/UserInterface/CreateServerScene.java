package UserInterface;

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

		Button createServerBtn = new Button("Create Server");
		createServerBtn.setPadding(new Insets(10));
		createServerBtn.setOnAction(event -> {
			usernameField.hideError();
			portField.hideError();

			String username = usernameField.getFieldText().trim();
			int port;

			try {
				port = Integer.parseInt(portField.getFieldText().trim());
			} catch (NumberFormatException e) {
				portField.setError("Please enter a valid port number.");
			}

			if (username.isEmpty()) {
				usernameField.setError("Please enter a username.");
			}
		});


		pane.getChildren().addAll(usernameField, portField, createServerBtn);

		this.scene = new Scene(pane);
	}

	public Scene getScene() {
		return scene;
	}

	class Field extends VBox {
		private TextField field;
		private Label error;
		public Field(String name) {
			this.setSpacing(5);
			this.field = new TextField();
			this.error = new Label();
			this.error.setVisible(false);
			HBox entryPane = new HBox();
			entryPane.setSpacing(5);
			error.setAlignment(Pos.CENTER);
			entryPane.getChildren().addAll(field, error);
			this.getChildren().addAll(new Label(name), entryPane);
		}

		public String getFieldText() {
			return field.getText();
		}

		public void setError(String message) {
			error.setVisible(true);
			error.setText(message);
		}

		public void hideError() {
			error.setVisible(false);
		}
	}
}
