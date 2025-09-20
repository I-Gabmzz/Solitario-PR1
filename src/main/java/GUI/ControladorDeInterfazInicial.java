package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import java.io.IOException;

// Se anuncia la creacion de la clase que maneja la pantalla inicial de la aplicacion
public class ControladorDeInterfazInicial {
    // Se declaran los atributos de la clase
    @FXML private ImageView fondo; // Imagen de fondo de la interfaz
    @FXML private Button botonJugar, botonCreditos, botonSalir; // Botones principales

    // Estilos para los botones en estado normal, con hover y presionados
    private static final String BOTONES_NORMALES =
            "-fx-background-color: rgba(255,255,255,0.85);" +
                    "-fx-border-color: #d32f2f;" +
                    "-fx-border-width: 2;" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-radius: 12;";

    private static final String BOTONES_CON_HOVER =
            "-fx-background-color: #d32f2f;" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #d32f2f;" +
                    "-fx-border-width: 2;" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-radius: 12;";

    private static final String BOTONES_PRESIONADOS =
            "-fx-background-color: #b71c1c;" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #b71c1c;" +
                    "-fx-border-width: 2;" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-radius: 12;";

    // Este metodo simplemente le da estilo a un botón y configura sus efectos visuales de hover y clic
    private void darEstiloAlBoton(Button boton) {
        DropShadow normal = new DropShadow();
        normal.setRadius(12);
        normal.setOffsetY(3);
        normal.setColor(Color.rgb(0, 0, 0, 0.60));

        DropShadow hover = new DropShadow();
        hover.setRadius(18);
        hover.setOffsetY(6);
        hover.setColor(Color.rgb(0, 0, 0, 1.0));

        boton.setStyle(BOTONES_NORMALES);
        boton.setEffect(normal);

        boton.setOnMouseEntered(e -> {
            boton.setStyle(BOTONES_CON_HOVER);
            boton.setEffect(hover);
            boton.setCursor(javafx.scene.Cursor.HAND);
        });
        boton.setOnMouseExited(e -> {
            boton.setStyle(BOTONES_NORMALES);
            boton.setEffect(normal);
        });
        boton.setOnMousePressed(e -> {
            boton.setStyle(BOTONES_PRESIONADOS);
            boton.setEffect(normal);
        });
        boton.setOnMouseReleased(e -> {
            if (boton.isHover()) {
                boton.setStyle(BOTONES_CON_HOVER);
                boton.setEffect(hover);
            } else {
                boton.setStyle(BOTONES_NORMALES);
                boton.setEffect(normal);
            }
        });
    }

    // Este metodo inicializador lo que hace es ajustar el fondo al tamaño de la ventana y aplica estilos a los botones
    @FXML
    private void initialize() {
        fondo.sceneProperty().addListener((obs, oldScene, scene) -> {
            if (scene != null) {
                fondo.fitWidthProperty().bind(scene.widthProperty());
                fondo.fitHeightProperty().bind(scene.heightProperty());
            }
        });
        darEstiloAlBoton(botonJugar);
        darEstiloAlBoton(botonCreditos);
        darEstiloAlBoton(botonSalir);
    }

    // Abre la ventana del tablero al presionar "Jugar"
    @FXML
    private void alDarJugar(ActionEvent e) throws IOException {
        Parent raizTablero = FXMLLoader.load(getClass().getResource("/GUI/Tablero.fxml"));
        Stage venActual  = (Stage) botonJugar.getScene().getWindow();
        Stage venTablero = new Stage();
        venTablero.setTitle("Solitario – Tablero");
        venTablero.setScene(new Scene(raizTablero, venActual.getWidth(), venActual.getHeight()));
        venTablero.setMaximized(venActual.isMaximized());
        venTablero.setFullScreen(venActual.isFullScreen());
        venActual.close();
        venTablero.show();
    }

    // Este metodo muestra una ventana emergente con los créditos del juego
    @FXML
    private void alDarCreditos(javafx.event.ActionEvent e) {
        var stage = (javafx.stage.Stage) botonCreditos.getScene().getWindow();
        var alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.initOwner(stage);
        alert.initModality(javafx.stage.Modality.WINDOW_MODAL);
        alert.initStyle(javafx.stage.StageStyle.UTILITY);
        alert.setTitle("Créditos");
        alert.setHeaderText("Solitario Game | Créditos");
        alert.setContentText("Creador: Angel Gabriel Manjarrez Moreno \n" +
                "Matricula: 1197503 \n" +
                "Materia: Algoritmos y Estructuras de datos \n" +
                "Versión: 19 de septiembre de 2025");
        alert.showAndWait(); // Muestra la alerta y espera a que el usuario la cierre
    }

    // Cierra la aplicación al dar clic en "Salir"
    @FXML
    private void alDarSalir(ActionEvent e) {
        Stage stage = (Stage) botonSalir.getScene().getWindow();
        stage.close();
        Platform.exit();
    }
}