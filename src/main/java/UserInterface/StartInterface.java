package UserInterface;

import TetrisGame.*;
import FileManagement.FileManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

public class StartInterface extends Application {
    Stage window;
    TetrisBoard board = new TetrisBoard();
    FileManager fileManager = new FileManager();
    final double MIN_WIDTH = 400;
    final double MIN_HEIGHT = 500;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setMinWidth(MIN_WIDTH);
        //root.setMaxWidth(250);
        root.setMinHeight(MIN_WIDTH);
        //root.setMaxHeight(250);
        root.setVgap(20);

        Button soloBtn = new Button("SOLO");
        soloBtn.setAlignment(Pos.CENTER);
        soloBtn.setPrefWidth(100);
        soloBtn.setPrefHeight(50);
        root.add(soloBtn, 0, 0);


        Button multiBtn = new Button("MULTIPLAYER");
        multiBtn.setAlignment(Pos.CENTER);
        multiBtn.setPrefWidth(100);
        multiBtn.setPrefHeight(50);
        root.add(multiBtn, 0, 1);


        GridPane root2 = new GridPane();
        root2.setAlignment(Pos.CENTER);
        root2.setMinWidth(MIN_WIDTH);
        //root2.setMaxWidth(250);
        root2.setMinHeight(MIN_WIDTH);
        //root2.setMaxHeight(250);
        root2.setVgap(20);


        Button createBtn = new Button("CREATE GAME");
        createBtn.setAlignment(Pos.CENTER);
        createBtn.setPrefWidth(100);
        createBtn.setPrefHeight(50);
        root2.add(createBtn, 0, 0);

        createBtn.setOnAction(event -> {
            window.setScene(new CreateServerScene(window).getScene());
        });

        Button joinBtn = new Button("JOIN GAME");
        joinBtn.setAlignment(Pos.CENTER);
        joinBtn.setPrefWidth(100);
        joinBtn.setPrefHeight(50);
        root2.add(joinBtn, 0, 1);

        joinBtn.setOnAction(event -> {
            window.setScene(new JoinServerScene(window).getScene());
        });

        Button backBtn = new Button("BACK");
        backBtn.setAlignment(Pos.CENTER);
        backBtn.setPrefWidth(100);
        backBtn.setPrefHeight(50);
        root2.add(backBtn, 0, 2);


        Scene firstScene = new Scene(root);
        //Scene secondScene = new Scene(root2);
        window.setTitle("Group 7 Tetris");
        window.setScene(firstScene);
        window.show();

        // Action handler for when button is clicked
        soloBtn.setOnAction(event -> {
            fileManager.incrementSinglePlayer();

            VBox gameVbox = new VBox(10);
            gameVbox.setAlignment(Pos.CENTER);
            firstScene.setRoot(gameVbox);

            Button quitBtn = new Button("Quit");
    		quitBtn.setOnAction(e -> {
                board.gameOver = true;
                firstScene.setRoot(root);
    		});

            HBox boardHbox = new HBox();
            boardHbox.setAlignment(Pos.CENTER);
            VBox boardVbox = new VBox();
            boardVbox.setAlignment(Pos.CENTER);
            boardVbox.getChildren().addAll(board.pane, new Label("Player"));
            boardHbox.getChildren().add(boardVbox);
            gameVbox.getChildren().addAll(quitBtn, boardHbox);
            gameVbox.requestFocus();

            firstScene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.LEFT) {
                    board.moveTetrimino("left");
                }
                else if (e.getCode() == KeyCode.RIGHT) {
                    board.moveTetrimino("right");
                }
                else if (e.getCode() == KeyCode.DOWN) {
                    board.moveTetrimino("down");
                }
                else if (e.getCode() == KeyCode.SPACE) {
                    board.rotateTetrimino();
                }
            });

            board.startGame();
        });

        multiBtn.setOnAction(e -> {
            firstScene.setRoot(root2);
        });

        backBtn.setOnAction(e -> {
            firstScene.setRoot(root);
        });
    }

    @Override
    public void stop() {
        System.out.println("Stage is closing");
        board.stageClosed = true;
    }

    public static void main(String[] args) {
        launch();
    }
}
