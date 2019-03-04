import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class HelloWorld extends Application {
    public static void main(String[] args) {
        System.out.println("Hello, world!");
        System.out.println("How do you do?");
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();
        Scene scene = new Scene(pane);
        pane.setPrefSize(100, 100);

        Label label = new Label("Hello world");
        pane.getChildren().add(label);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}