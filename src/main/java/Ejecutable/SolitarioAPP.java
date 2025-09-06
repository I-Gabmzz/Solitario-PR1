package Ejecutable;

// Importaciones necesarias para usar JavaFX y cargar la interfaz
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

// Clase principal que extiende Application, sirve como punto de inicio
public class SolitarioAPP extends Application {

    // Metodo que carga la interfaz inicial, crea la escena y abre la ventana en pantalla completa
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(
                FXMLLoader.load(Objects.requireNonNull(
                        getClass().getResource("/GUI/InterfazInicial.fxml"))),
                1280, 720
        );
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    // Metodo main, aquí se lanza la aplicación
    public static void main(String[] args) {
        launch(args);
    }
}
