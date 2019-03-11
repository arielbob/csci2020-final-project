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
    root.setMinWidth(150);
    root.setMaxWidth(150);
    root.setMinHeight(150);
    root.setMaxHeight(150);
    //root.setHgap(30);
    root.setVgap(20);

    Button soloBtn = new Button("       SOLO       ");
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
