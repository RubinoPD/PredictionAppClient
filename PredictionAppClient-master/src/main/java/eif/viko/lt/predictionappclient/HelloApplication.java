package eif.viko.lt.predictionappclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Pažymių prognozavimo sistema");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Isvalome tokena programos paleidimo metu
        SecureStorage.clearToken();
        launch();
    }
}