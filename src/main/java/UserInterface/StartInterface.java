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
    Scene firstScene;// = new Scene(root);
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
        root.setMinHeight(MIN_WIDTH);
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

        Button statsBtn = new Button("VIEW STATS");
        statsBtn.setAlignment(Pos.CENTER);
        statsBtn.setPrefWidth(100);
        statsBtn.setPrefHeight(50);
        root.add(statsBtn, 0, 2);

        GridPane root2 = new GridPane();
        root2.setAlignment(Pos.CENTER);
        root2.setMinWidth(MIN_WIDTH);
        root2.setMinHeight(MIN_WIDTH);
        root2.setVgap(20);

        Button createBtn = new Button("CREATE GAME");
        createBtn.setAlignment(Pos.CENTER);
        createBtn.setPrefWidth(100);
        createBtn.setPrefHeight(50);
        root2.add(createBtn, 0, 0);

        createBtn.setOnAction(event -> {
            //window.setScene(new CreateServerScene(window).getScene());
            firstScene.setRoot(new CreateServerScene(window, root2).getRoot());
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


        //Scene firstScene = new Scene(root);
        firstScene = new Scene(root);
        //Scene secondScene = new Scene(root2);
        window.setTitle("Group 7 Tetris");
        window.setScene(firstScene);
        window.show();

        // Action handler for when button is clicked
        soloBtn.setOnAction(event -> {
            fileManager.incrementSinglePlayer();

            Button quitBtn = new Button("Quit");
    		quitBtn.setOnAction(e -> {
                board.gameOver = true;
                firstScene.setRoot(root);
    		});

            VBox boardVbox = new VBox();
            boardVbox.setAlignment(Pos.CENTER);
            boardVbox.getChildren().addAll(board.pane, new Label("Player"));

            VBox alignVbox = new VBox(10);
            alignVbox.setAlignment(Pos.CENTER_LEFT);
            alignVbox.getChildren().addAll(quitBtn, boardVbox);

            HBox boardHbox = new HBox();
            boardHbox.setAlignment(Pos.CENTER);
            boardHbox.getChildren().add(alignVbox);

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

            VBox gameVbox = new VBox();//new VBox(10);
            gameVbox.setAlignment(Pos.CENTER);
            firstScene.setRoot(gameVbox);
            gameVbox.getChildren().add(boardHbox);
            gameVbox.requestFocus();
            board.startGame();
        });

        statsBtn.setOnAction(e -> {
            GridPane statsPane = new GridPane();
            statsPane.setVgap(20);
            statsPane.setAlignment(Pos.CENTER);

            int singlePlayerPlayed = fileManager.getSinglePlayerPlayed();
            Label singlePlayerPlayedLbl = new Label("Single player games played: " + singlePlayerPlayed);
            singlePlayerPlayedLbl.setAlignment(Pos.CENTER);
            statsPane.add(singlePlayerPlayedLbl, 0, 0);

            int multiplayerPlayed = fileManager.getMultiplayerPlayed();
            Label multiplayerPlayedLbl = new Label("Multiplayer games played: " + multiplayerPlayed);
            multiplayerPlayedLbl.setAlignment(Pos.CENTER);
            statsPane.add(multiplayerPlayedLbl, 0, 1);

            int multiplayerWon = fileManager.getMultiplayerWon();
            Label multiplayerWonLbl = new Label("Multiplayer games won: " + multiplayerWon);
            multiplayerWonLbl.setAlignment(Pos.CENTER);
            statsPane.add(multiplayerWonLbl, 0, 2);

            int linesCleared = fileManager.getLinesCleared();
            Label linesClearedLbl = new Label("Total lines cleared: " + linesCleared);
            linesClearedLbl.setAlignment(Pos.CENTER);
            statsPane.add(linesClearedLbl, 0, 3);

            Button backToMenuBtn = new Button("BACK");
            backToMenuBtn.setAlignment(Pos.CENTER);
            backToMenuBtn.setPrefWidth(100);
            backToMenuBtn.setPrefHeight(50);
            statsPane.add(backToMenuBtn, 0, 4);
            firstScene.setRoot(statsPane);
            backToMenuBtn.setOnAction(event -> {
                firstScene.setRoot(root);
            });
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
