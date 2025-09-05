package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.Objects;

public class ControladorDeTablero {
    private solitaire.SolitaireGame juego;
    @FXML private ImageView fondo;
    @FXML private javafx.scene.layout.HBox centro;
    @FXML private javafx.scene.layout.StackPane contMazo;
    @FXML private javafx.scene.layout.StackPane contDescarte;
    @FXML private javafx.scene.layout.StackPane bas1, bas2, bas3, bas4;
    @FXML private Pane col1, col2, col3, col4, col5, col6, col7;
    @FXML private Button botonNuevo;
    @FXML private Button botonMenu;
    @FXML private GUI.CartaView col1c1Controller;
    @FXML private GUI.CartaView col2c1Controller, col2c2Controller;
    @FXML private GUI.CartaView col3c1Controller, col3c2Controller, col3c3Controller;
    @FXML private GUI.CartaView col4c1Controller, col4c2Controller, col4c3Controller, col4c4Controller;
    @FXML private GUI.CartaView col5c1Controller, col5c2Controller, col5c3Controller, col5c4Controller, col5c5Controller;
    @FXML private GUI.CartaView col6c1Controller, col6c2Controller, col6c3Controller, col6c4Controller, col6c5Controller, col6c6Controller;
    @FXML private GUI.CartaView col7c1Controller, col7c2Controller, col7c3Controller, col7c4Controller, col7c5Controller, col7c6Controller, col7c7Controller;
    private enum Origen { TABLEAU, WASTE }


    private double pasoY = 40;
    private double anchoCarta = 150;
    private double altoCarta  = 200;
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

    private static final class DragInfo {
        Origen origen;
        int col;
        int valor;
        boolean esTope;
    }

    @FXML
    private void initialize() {
        juego = new solitaire.SolitaireGame();
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
        refrescarZonaSuperior(juego);
        refrescarTableau(juego);
    }

    private void refrescarTableau(solitaire.SolitaireGame juego) {
        java.util.ArrayList<solitaire.TableauDeck> tabs = juego.getTableau();
        if (tabs == null || tabs.size() < 7) return;

        java.util.List<GUI.CartaView[]> vistas = java.util.List.of(
                new GUI.CartaView[]{ col1c1Controller },
                new GUI.CartaView[]{ col2c1Controller, col2c2Controller },
                new GUI.CartaView[]{ col3c1Controller, col3c2Controller, col3c3Controller },
                new GUI.CartaView[]{ col4c1Controller, col4c2Controller, col4c3Controller, col4c4Controller },
                new GUI.CartaView[]{ col5c1Controller, col5c2Controller, col5c3Controller, col5c4Controller, col5c5Controller },
                new GUI.CartaView[]{ col6c1Controller, col6c2Controller, col6c3Controller, col6c4Controller, col6c5Controller, col6c6Controller },
                new GUI.CartaView[]{ col7c1Controller, col7c2Controller, col7c3Controller, col7c4Controller, col7c5Controller, col7c6Controller, col7c7Controller }
        );

        for (int i = 0; i < 7; i++) {
            solitaire.TableauDeck td = tabs.get(i);
            java.util.ArrayList<DeckOfCards.CartaInglesa> cartas = (td != null) ? td.getCards() : new java.util.ArrayList<>();
            GUI.CartaView[] ranuras = vistas.get(i);

            for (GUI.CartaView cv : ranuras) if (cv != null) cv.setVisible(false);

            for (int j = 0; j < cartas.size() && j < ranuras.length; j++) {
                DeckOfCards.CartaInglesa c = cartas.get(j);
                GUI.CartaView cv = ranuras[j];
                if (cv == null) continue;

                String tipo = tipoDesdePalo(c.getPalo());
                cv.establecerPorNumero(c.getValor(), tipo);

                cv.establecerBocaArriba(c.isFaceup());

                cv.mostrarSlot(false);
                cv.setVisible(true);
            }
        }

        acomodarColumna(col1, 1);
        acomodarColumna(col2, 2);
        acomodarColumna(col3, 3);
        acomodarColumna(col4, 4);
        acomodarColumna(col5, 5);
        acomodarColumna(col6, 6);
        acomodarColumna(col7, 7);
    }


    private void acomodarColumna(Pane col, int cuantosMax) {
        var hijos = col.getChildren();

        double y = 0.0;
        int activos = 0;

        for (javafx.scene.Node n : hijos) {
            if (!n.isManaged() || !n.isVisible()) continue; // ignora placeholders ocultos
            n.setLayoutY(y);                                // mejor que translateY en Pane
            y += pasoY;
            activos++;
        }
        int usados = Math.max(activos, cuantosMax);
        double altoNecesario = (usados > 0 ? (usados - 1) * pasoY + altoCarta : altoCarta);

        col.setMinWidth(anchoCarta);
        col.setPrefWidth(anchoCarta);
        col.setMinHeight(altoNecesario);
        col.setPrefHeight(altoNecesario);
    }

    public void alDarNuevoJuego(javafx.event.ActionEvent actionEvent) {

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

    private void dibujarSlot(javafx.scene.layout.StackPane cont, double w, double h) {
        cont.getChildren().clear();
        var r = new javafx.scene.shape.Rectangle(w, h);
        r.setArcWidth(14); r.setArcHeight(14);
        r.setFill(javafx.scene.paint.Color.color(1,1,1, 0.10));
        r.setStroke(javafx.scene.paint.Color.WHITE);
        r.setStrokeWidth(2);
        cont.getChildren().add(r);
    }

    private void mostrarCubierta(javafx.scene.layout.StackPane cont, double w, double h) {
        cont.getChildren().clear();
        var url = getClass().getResource("/Cartas/Cubierta Carta.png");
        if (url == null) { dibujarSlot(cont, w, h); return; }
        var iv = new javafx.scene.image.ImageView(new javafx.scene.image.Image(url.toExternalForm()));
        iv.setFitWidth(w); iv.setFitHeight(h); iv.setPreserveRatio(false);
        cont.getChildren().add(iv);
    }

    private void mostrarCarta(javafx.scene.layout.StackPane cont,
                              DeckOfCards.CartaInglesa c, double w, double h) {
        cont.getChildren().clear();
        String tipo = tipoDesdePalo(c.getPalo());
        String nom  = nombreDesdeValor(c.getValor());
        String ruta = "/Cartas/" + tipo + "/" + nom + ".png";
        var url = getClass().getResource(ruta);
        if (url == null) { dibujarSlot(cont, w, h); return; }
        var iv = new javafx.scene.image.ImageView(new javafx.scene.image.Image(url.toExternalForm()));
        iv.setFitWidth(w); iv.setFitHeight(h); iv.setPreserveRatio(false);
        cont.getChildren().add(iv);
    }

    private void refrescarZonaSuperior(solitaire.SolitaireGame juego) {
        if (juego.getDrawPile() != null && juego.getDrawPile().hayCartas()) {
            mostrarCubierta(contMazo, anchoCarta, altoCarta);
        } else {
            dibujarSlot(contMazo, anchoCarta, altoCarta);
        }

        var topWaste = (juego.getWastePile() != null) ? juego.getWastePile().verCarta() : null;
        if (topWaste == null) {
            dibujarSlot(contDescarte, anchoCarta, altoCarta);
        } else {
            mostrarCarta(contDescarte, topWaste, anchoCarta, altoCarta);
        }

        var fnds = juego.getFoundation();
        var bases = new javafx.scene.layout.StackPane[]{ bas1, bas2, bas3, bas4 };
        for (int i = 0; i < bases.length; i++) {
            var base = bases[i];
            DeckOfCards.CartaInglesa top = (fnds != null && i < fnds.size() && fnds.get(i) != null)
                    ? fnds.get(i).getUltimaCarta() : null;
            if (top == null) {
                dibujarSlot(base, anchoCarta, altoCarta);
            } else {
                mostrarCarta(base, top, anchoCarta, altoCarta);
            }
        }
    }

    private String tipoDesdePalo(DeckOfCards.Palo p) {
        if (p == null) return "Pica";
        switch (p) {
            case PICA:     return "Pica";
            case CORAZON:  return "Corazon";
            case DIAMANTE: return "Diamante";
            case TREBOL:   return "Trebol";
            default:       return "Pica";
        }
    }

    private String nombreDesdeValor(int n) {
        switch (n) {
            case 1:  return "As";
            case 2:  return "Dos";
            case 3:  return "Tres";
            case 4:  return "Cuatro";
            case 5:  return "Cinco";
            case 6:  return "Seis";
            case 7:  return "Siete";
            case 8:  return "Ocho";
            case 9:  return "Nueve";
            case 10: return "Diez";
            case 11: return "Joto";
            case 12: return "Queen";
            case 13: return "King";
            case 14: return "As";
            default: return "As";
        }
    }
}
