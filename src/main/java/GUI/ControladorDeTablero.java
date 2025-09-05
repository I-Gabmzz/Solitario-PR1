package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.Objects;

public class ControladorDeTablero {
    @FXML private ImageView fondo;
    @FXML private javafx.scene.layout.HBox centro;
    @FXML private javafx.scene.layout.StackPane contMazo, contDescarte, bas1, bas2, bas3, bas4;
    @FXML private Pane col1, col2, col3, col4, col5, col6, col7;
    @FXML private Button botonNuevo;
    @FXML private Button botonMenu;
    @FXML private GUI.CartaView col1c1Controlador;
    @FXML private GUI.CartaView col2c2Controlador;
    @FXML private GUI.CartaView col3c3Controlador;
    @FXML private GUI.CartaView col4c4Controlador;
    @FXML private GUI.CartaView col5c5Controlador;
    @FXML private GUI.CartaView col6c6Controlador;
    @FXML private GUI.CartaView col7c7Controlador;

    private double pasoY = 40;
    private double anchoCarta = 150;
    private double altoCarta  = 200;
    private boolean mazoVacio = false;
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

    @FXML
    private void initialize() {
        centro.setSpacing(32);
        botonNuevo.setPrefSize(240, 60);
        botonMenu.setPrefSize(200, 60);

        var url = getClass().getResource("/GUI/Tablero.jpg");
        if (url != null) {
            fondo.setImage(new Image(url.toExternalForm()));
            fondo.setPreserveRatio(true);
            fondo.setSmooth(true);
            fondo.sceneProperty().addListener((obs, ant, esc) -> {
                if (esc != null) {
                    fondo.fitWidthProperty().bind(esc.widthProperty());
                    fondo.fitHeightProperty().bind(esc.heightProperty());
                }
            });
        }
        darEstiloAlBoton(botonNuevo);
        darEstiloAlBoton(botonMenu);
        dibujarSlot(bas1);
        dibujarSlot(bas2);
        dibujarSlot(bas3);
        dibujarSlot(bas4);
        limpiarDescarte();
        refrescarMazoVisual();
        acomodarCol(col1);
        acomodarCol(col2);
        acomodarCol(col3);
        acomodarCol(col4);
        acomodarCol(col5);
        acomodarCol(col6);
        acomodarCol(col7);
        if (col1c1Controlador != null) col1c1Controlador.establecerBocaArriba(true);
        if (col2c2Controlador != null) col2c2Controlador.establecerBocaArriba(true);
        if (col3c3Controlador != null) col3c3Controlador.establecerBocaArriba(true);
        if (col4c4Controlador != null) col4c4Controlador.establecerBocaArriba(true);
        if (col5c5Controlador != null) col5c5Controlador.establecerBocaArriba(true);
        if (col6c6Controlador != null) col6c6Controlador.establecerBocaArriba(true);
        if (col7c7Controlador != null) col7c7Controlador.establecerBocaArriba(true);
    }

    private void acomodarCol(Pane col) {
        var hijos = col.getChildren();
        for (int i = 0; i < hijos.size(); i++) {
            Node carta = hijos.get(i);
            carta.setTranslateY(i * pasoY);
        }
        double altoNecesario = (hijos.size() - 1) * pasoY + altoCarta;
        col.setMinWidth(anchoCarta);
        col.setPrefWidth(anchoCarta);
        col.setMinHeight(altoNecesario);
    }

    public void alDarNuevoJuego(javafx.event.ActionEvent actionEvent) {
        acomodarCol(col1);
        acomodarCol(col2);
        acomodarCol(col3);
        acomodarCol(col4);
        acomodarCol(col5);
        acomodarCol(col6);
        acomodarCol(col7);
    }

    public void alDarMenu(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent raizMenu = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GUI/InterfazInicial.fxml")));
        Stage venTablero = (Stage) botonMenu.getScene().getWindow();
        Stage venMenu = new Stage();
        venMenu.setTitle("Solitario – Menú");
        venMenu.setScene(new Scene(raizMenu, venTablero.getWidth(), venTablero.getHeight()));
        venMenu.setMaximized(venTablero.isMaximized());
        venMenu.setFullScreen(venTablero.isFullScreen());

        venTablero.close();
        venMenu.show();
    }

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

    private void dibujarSlot(StackPane cont) {
        cont.getChildren().clear();
        var r = new javafx.scene.shape.Rectangle(anchoCarta, altoCarta);
        r.setArcWidth(14); r.setArcHeight(14);
        r.setFill(javafx.scene.paint.Color.color(1,1,1, 0.10));
        r.setStroke(javafx.scene.paint.Color.WHITE);
        r.setStrokeWidth(2);
        cont.getChildren().add(r);
    }

    private void limpiarDescarte() { dibujarSlot(contDescarte); }

    private void refrescarMazoVisual() {
        contMazo.getChildren().clear();
        if (mazoVacio) {
            dibujarSlot(contMazo);
        } else {
            var url = getClass().getResource("/Cartas/Cubierta Carta.png");
            if (url != null) {
                var iv = new javafx.scene.image.ImageView(new javafx.scene.image.Image(url.toExternalForm()));
                iv.setFitWidth(anchoCarta);
                iv.setFitHeight(altoCarta);
                iv.setPreserveRatio(false);
                contMazo.getChildren().add(iv);
            } else {
                dibujarSlot(contMazo);
            }
        }
    }

    public void setMazoVacio(boolean vacio) {
        this.mazoVacio = vacio;
        refrescarMazoVisual();
    }

}
