package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.io.IOException;

public class ControladorDeInterfazInicial {
    @FXML private ImageView fondo;
    @FXML private Button botonJugar, botonCreditos, botonSalir;

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
                "Versión: 5 de septiembre de 2025");
        alert.showAndWait();
    }

    @FXML
    private void alDarSalir(ActionEvent e) {
        Stage stage = (Stage) botonSalir.getScene().getWindow();
        stage.close();
        Platform.exit();
    }
}