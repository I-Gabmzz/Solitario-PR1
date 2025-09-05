package Ejecutable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class SolitarioAPP extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GUI/InterfazInicial.fxml"))), 1280, 720);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }
    public static void main(String[] args) { launch(args); }
}