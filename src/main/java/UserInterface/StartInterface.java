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

  Stage window;
  Scene firstScene, secondScene;

	@Override
	public void start(Stage primaryStage) throws Exception {

    window = primaryStage;

    //First Scene Code below
    GridPane root = new GridPane();
		root.setAlignment(Pos.CENTER);
    root.setMinWidth(250);
    root.setMaxWidth(250);
    root.setMinHeight(250);
    root.setMaxHeight(250);
    root.setVgap(20);

    Button soloBtn = new Button("       SOLO       ");
    soloBtn.setAlignment(Pos.CENTER);
    soloBtn.setPrefWidth(100);
    soloBtn.setPrefHeight(50);
    root.add(soloBtn, 0, 0);

    Button multiBtn = new Button("MULTIPLAYER");
    multiBtn.setAlignment(Pos.CENTER);
    multiBtn.setPrefWidth(100);
    multiBtn.setPrefHeight(50);
    root.add(multiBtn, 0, 1);

    //Second Scene Code below
    GridPane root2 = new GridPane();
    root2.setAlignment(Pos.CENTER);
    root2.setMinWidth(250);
    root2.setMaxWidth(250);
    root2.setMinHeight(250);
    root2.setMaxHeight(250);
    root2.setVgap(20);

    Button createBtn = new Button("CREATE GAME");
    createBtn.setAlignment(Pos.CENTER);
    createBtn.setPrefWidth(100);
    createBtn.setPrefHeight(50);
    root2.add(createBtn, 0, 0);

    Button joinBtn = new Button("JOIN GAME");
    joinBtn.setAlignment(Pos.CENTER);
    joinBtn.setPrefWidth(100);
    joinBtn.setPrefHeight(50);
    root2.add(joinBtn, 0, 1);

    Button backBtn = new Button("BACK");
    backBtn.setAlignment(Pos.CENTER);
    backBtn.setPrefWidth(100);
    backBtn.setPrefHeight(50);
    root2.add(backBtn, 0, 2);

    Scene firstScene = new Scene(root);
    Scene secondScene = new Scene(root2);
    window.setTitle("MultiTetris7");
    window.setScene(firstScene);
    window.show();

		//action handler for when button is clicked
		soloBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

			}
		});

    // multiBtn.setOnAction(e -> window.setScene());

    multiBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        window.setScene(secondScene);
      }
    });

    backBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        window.setScene(firstScene);
      }
    });


	}

	public static void main(String[] args) {
		launch();
	}

}
