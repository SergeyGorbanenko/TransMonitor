import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStager) {
        primaryStage = primaryStager;
        primaryStage.setResizable(false);
        primaryStage.setTitle("Монитор транспорта");
        initAuthorizationLayout();
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void initAuthorizationLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Main.fxml"));
            BorderPane loginLayout = (BorderPane) loader.load();
            Scene loginScene = new Scene(loginLayout);
            primaryStage.setScene(loginScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initMonitorLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Monitor.fxml"));
            BorderPane monitorLayout = (BorderPane) loader.load();
            Scene monitorScene = new Scene(monitorLayout);
            primaryStage.setScene(monitorScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}