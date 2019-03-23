package UserInterface;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

class Field extends VBox {
	private TextField field;
	private Label error;
	Field(String name) {
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

	String getFieldText() {
		return field.getText();
	}

	void setError(String message) {
		error.setVisible(true);
		error.setText(message);
	}

	void hideError() {
		error.setVisible(false);
	}
}