package UserInterface;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class StartInterface extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

    primaryStage.setTitle("MultiTetris7");

    Pane root = new Pane();

		Button soloBtn = new Button("SOLO");
		//GridPane.setHalignment(btn, HPos.CENTRE);
		//btn.setDefaultButton(true);
    soloBtn.setLayoutX(250);
    soloBtn.setLayoutY(220);

		//action handler for when button is clicked
		soloBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

			}
		});

    root.getChildren().add(soloBtn);
    primaryStage.setScene(new Scene(root, 300, 250));
    primaryStage.show();


	}

	public static void main(String[] args) {
		launch();
	}

}
