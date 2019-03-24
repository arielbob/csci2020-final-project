//package net.test;
//
//import java.net.SocketException;
//
//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextArea;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import net.server.TetrisServer;
//
//public class ServerTest extends Application {
//	private static TetrisServer server;
//	private static TextArea ta;
//
//	public static void main(String[] args) {
//		ta = new TextArea();
//		ta.setEditable(false);
//
//		try {
//			server = new TetrisServer(61616);
//			server.start();
//		} catch (SocketException e) {
//			e.printStackTrace();
//		}
//
//		launch();
//	}
//
//	@Override
//	public void start(Stage primaryStage) throws Exception {
////		server.setView(this);
//
//		VBox pane = new VBox();
//		pane.setPadding(new Insets(10));
//		pane.setSpacing(10);
//
//		Scene scene = new Scene(pane);
//
//		Label label = new Label("Messages");
//
//		Button startBtn = new Button("Start Game");
//		Button endBtn = new Button("End Game");
//
//		startBtn.setOnAction(event -> {
//			server.startGame();
//		});
//		endBtn.setOnAction(event -> {
//			server.endGame();
//		});
//
//		pane.getChildren().addAll(label, ta, startBtn, endBtn);
//
//		primaryStage.setTitle("Server Test");
//		primaryStage.setScene(scene);
//		primaryStage.show();
//		primaryStage.setOnCloseRequest(event -> {
//			server.close();
//		});
//	}
//
//	public void appendText(String text) {
//		ta.appendText(text);
//	}
//}
