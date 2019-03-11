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

    GridPane root = new GridPane();
		root.setAlignment(Pos.CENTER);
    root.setPadding(new Insets(100));
    root.setHgap(16);
    root.setVgap(8);

    Button soloBtn = new Button("      SOLO      ");
    soloBtn.setAlignment(Pos.CENTER);
    root.add(soloBtn, 0, 0);

    Button multiBtn = new Button("MULTIPLAYER");
    multiBtn.setAlignment(Pos.CENTER);
    root.add(multiBtn, 0, 1);

		//action handler for when button is clicked
		soloBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

			}
		});

    Scene firstscene = new Scene(root);
    primaryStage.setTitle("MultiTetris7");
    primaryStage.setScene(firstscene);
    primaryStage.show();


	}

	public static void main(String[] args) {
		launch();
	}

}
