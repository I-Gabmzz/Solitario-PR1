package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import solitaire.FoundationDeck;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ControladorDeTablero {
    private solitaire.SolitaireGame juego;
    private Label[] marcadoresFundacion;

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
    private java.util.List<javafx.scene.layout.Pane> columnasPanes;
    private java.util.List<java.util.List<GUI.CartaView>> vistasColumnas;


    private enum Origen { TABLERO, DESCARTE }
    private static final int MAX_CARTAS = 13;
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
    private static String encode(DragInfo d) {
        return d.origen + ";" + d.col + ";" + d.valor + ";" + (d.esTope ? "1" : "0");
    }
    private static DragInfo decode(String s) {
        String[] p = s.split(";");
        DragInfo d = new DragInfo();
        d.origen = Origen.valueOf(p[0]);
        d.col = Integer.parseInt(p[1]);
        d.valor = Integer.parseInt(p[2]);
        d.esTope = "1".equals(p[3]);
        return d;
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
        columnasPanes = java.util.List.of(col1, col2, col3, col4, col5, col6, col7);


        java.util.List<GUI.CartaView> c1 = new java.util.ArrayList<>();
        c1.add(col1c1Controller);
        java.util.List<GUI.CartaView> c2 = new java.util.ArrayList<>();
        c2.add(col2c1Controller); c2.add(col2c2Controller);
        java.util.List<GUI.CartaView> c3 = new java.util.ArrayList<>();
        c3.add(col3c1Controller); c3.add(col3c2Controller); c3.add(col3c3Controller);
        java.util.List<GUI.CartaView> c4 = new java.util.ArrayList<>();
        c4.add(col4c1Controller); c4.add(col4c2Controller); c4.add(col4c3Controller); c4.add(col4c4Controller);
        java.util.List<GUI.CartaView> c5 = new java.util.ArrayList<>();
        c5.add(col5c1Controller); c5.add(col5c2Controller); c5.add(col5c3Controller); c5.add(col5c4Controller); c5.add(col5c5Controller);
        java.util.List<GUI.CartaView> c6 = new java.util.ArrayList<>();
        c6.add(col6c1Controller); c6.add(col6c2Controller); c6.add(col6c3Controller); c6.add(col6c4Controller); c6.add(col6c5Controller); c6.add(col6c6Controller);
        java.util.List<GUI.CartaView> c7 = new java.util.ArrayList<>();
        c7.add(col7c1Controller); c7.add(col7c2Controller); c7.add(col7c3Controller); c7.add(col7c4Controller); c7.add(col7c5Controller); c7.add(col7c6Controller); c7.add(col7c7Controller);

        java.util.function.Function<java.util.List<GUI.CartaView>, java.util.List<GUI.CartaView>> limpia = lista -> {
            lista.removeIf(java.util.Objects::isNull);
            return lista;
        };

        vistasColumnas = new java.util.ArrayList<>(java.util.List.of(
                limpia.apply(c1), limpia.apply(c2), limpia.apply(c3), limpia.apply(c4),
                limpia.apply(c5), limpia.apply(c6), limpia.apply(c7)
        ));

        for (int i = 0; i < columnasPanes.size(); i++) {
            asegurarSlots(columnasPanes.get(i), vistasColumnas.get(i), MAX_CARTAS);
        }

        configurarInteracciones();
        actualizarZonaSuperior(juego);
        actualizarTablero(juego);
    }

    private void asegurarSlots(javafx.scene.layout.Pane contenedor,
                               java.util.List<GUI.CartaView> lista, int requeridos) {
        while (lista.size() < requeridos) {
            try {
                var loader = new javafx.fxml.FXMLLoader(getClass().getResource("/GUI/CartaView.fxml"));
                javafx.scene.layout.StackPane root = loader.load();
                GUI.CartaView cartaView = loader.getController();

                cartaView.mostrarSlot(false);
                cartaView.setVisible(false);

                contenedor.getChildren().add(root);
                lista.add(cartaView);
            } catch (Exception ex) {
                ex.printStackTrace();
                break;
            }
        }
    }


    private void actualizarTablero(solitaire.SolitaireGame juego) {
        var tabs = juego.getTableau();
        if (tabs == null || tabs.size() < 7) return;

        for (int i = 0; i < 7; i++) {
            var td = tabs.get(i);
            var cartas = td.getCards();
            var vistas = vistasColumnas.get(i);

            asegurarSlots(columnasPanes.get(i), vistas, MAX_CARTAS);

            int n = cartas.size();
            int mostrar = Math.min(MAX_CARTAS, n);
            int start = Math.max(0, n - MAX_CARTAS);

            for (int j = 0; j < mostrar; j++) {
                var carta = cartas.get(start + j);
                var cartaView = vistas.get(j);

                String tipo = tipoDesdePalo(carta.getPalo());
                cartaView.establecerPorNumero(carta.getValor(), tipo);
                cartaView.establecerBocaArriba(carta.isFaceup());
                cartaView.mostrarSlot(false);
                cartaView.setVisible(true);
            }

            for (int j = mostrar; j < MAX_CARTAS; j++) {
                var cv = vistas.get(j);
                cv.mostrarSlot(true);
                cv.setVisible(false);
            }
        }

        acomodarColumna(col1, MAX_CARTAS);
        acomodarColumna(col2, MAX_CARTAS);
        acomodarColumna(col3, MAX_CARTAS);
        acomodarColumna(col4, MAX_CARTAS);
        acomodarColumna(col5, MAX_CARTAS);
        acomodarColumna(col6, MAX_CARTAS);
        acomodarColumna(col7, MAX_CARTAS);
    }


    private void acomodarColumna(javafx.scene.layout.Pane col, int cuantosMax) {
        var hijos = col.getChildren();
        int espacio = 0;
        int activos = 0;

        for (var node : hijos) {
            node.setLayoutX(0);
            node.setLayoutY(espacio);
            espacio += pasoY;
            activos++;
            if (activos >= cuantosMax) break;
        }
    }

    public void alDarNuevoJuego(javafx.event.ActionEvent actionEvent) {

    }

    public void alDarMenu(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent raizMenu = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GUI/InterfazInicial.fxml")));
        Stage ventanaTablero = (Stage) botonMenu.getScene().getWindow();
        Stage vetananMenu = new Stage();
        vetananMenu.setTitle("Solitario – Menú");
        vetananMenu.setScene(new Scene(raizMenu, ventanaTablero.getWidth(), ventanaTablero.getHeight()));
        vetananMenu.setMaximized(ventanaTablero.isMaximized());
        vetananMenu.setFullScreen(ventanaTablero.isFullScreen());

        ventanaTablero.close();
        vetananMenu.show();
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

    private void dibujarSlot(javafx.scene.layout.StackPane cont, double ancho, double alto) {
        cont.getChildren().clear();
        var espacioCarta = new javafx.scene.shape.Rectangle(ancho, alto);
        espacioCarta.setArcWidth(14); espacioCarta.setArcHeight(14);
        espacioCarta.setFill(javafx.scene.paint.Color.color(1,1,1, 0.10));
        espacioCarta.setStroke(javafx.scene.paint.Color.WHITE);
        espacioCarta.setStrokeWidth(2);
        cont.getChildren().add(espacioCarta);
    }

    private void mostrarCubierta(javafx.scene.layout.StackPane cont, double ancho, double alto) {
        cont.getChildren().clear();
        var url = getClass().getResource("/Cartas/Cubierta Carta.png");
        if (url == null) { dibujarSlot(cont, ancho, alto); return; }
        var imageView = new javafx.scene.image.ImageView(new javafx.scene.image.Image(url.toExternalForm()));
        imageView.setFitWidth(ancho); imageView.setFitHeight(alto); imageView.setPreserveRatio(false);
        cont.getChildren().add(imageView);
    }

    private void mostrarCarta(javafx.scene.layout.StackPane cont,
                              DeckOfCards.CartaInglesa c, double ancho, double alto) {
        cont.getChildren().clear();
        String tipo = tipoDesdePalo(c.getPalo());
        String nombre  = nombreDesdeValor(c.getValor());
        String ruta = "/Cartas/" + tipo + "/" + nombre + ".png";
        var url = getClass().getResource(ruta);
        if (url == null) { dibujarSlot(cont, ancho, alto); return; }
        var iv = new javafx.scene.image.ImageView(new javafx.scene.image.Image(url.toExternalForm()));
        iv.setFitWidth(ancho); iv.setFitHeight(alto); iv.setPreserveRatio(false);
        cont.getChildren().add(iv);
    }

    private void actualizarZonaSuperior(solitaire.SolitaireGame juego) {
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

        var foundation = juego.getFoundation();
        var bases = new javafx.scene.layout.StackPane[]{ bas1, bas2, bas3, bas4 };
        for (int i = 0; i < bases.length; i++) {
            var base = bases[i];
            DeckOfCards.CartaInglesa top = (foundation != null && i < foundation.size() && foundation.get(i) != null)
                    ? foundation.get(i).getUltimaCarta() : null;
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

    private void configurarInteracciones() {
        contMazo.setOnMouseClicked(e -> {
            if (juego.getDrawPile() != null && juego.getDrawPile().hayCartas()) {
                juego.drawCards();
            } else {
                juego.reloadDrawPile();
            }
            actualizarZonaSuperior(juego);
        });
        Pane[] columnas = new Pane[]{ col1, col2, col3, col4, col5, col6, col7 };
        for (int i = 0; i < columnas.length; i++) {
            final int destinoCol = i;
            Pane colPane = columnas[i];

            colPane.setOnDragOver(evento -> {
                var arrastre = evento.getDragboard();
                if (arrastre.hasString()) {
                    DragInfo info = decode(arrastre.getString());
                    if (puedeSoltarseEnTablero(info, destinoCol)) {
                        evento.acceptTransferModes(javafx.scene.input.TransferMode.MOVE);
                    }
                }
                evento.consume();
            });

            colPane.setOnDragDropped(evento -> {
                var arrastre = evento.getDragboard();
                boolean ok = false;
                if (arrastre.hasString()) {
                    DragInfo info = decode(arrastre.getString());
                    ok = aplicarDropEnTablero(info, destinoCol);
                }
                evento.setDropCompleted(ok);
                evento.consume();

                actualizarZonaSuperior(juego);
                actualizarTablero(juego);
            });
        }

        javafx.scene.layout.StackPane[] bases = new javafx.scene.layout.StackPane[]{ bas1, bas2, bas3, bas4 };
        for (int i = 0; i < bases.length; i++) {
            final int Baseref = i;
            javafx.scene.layout.StackPane basePane = bases[i];

            basePane.setOnDragOver(evento -> {
                var arrastre = evento.getDragboard();
                if (arrastre.hasString()) {
                    DragInfo info = decode(arrastre.getString());
                    if (puedeSoltarseEnFoundation(info, Baseref)) {
                        evento.acceptTransferModes(javafx.scene.input.TransferMode.MOVE);
                    }
                }
                evento.consume();
            });

            basePane.setOnDragDropped(evento -> {
                var arrastre = evento.getDragboard();
                boolean ok = false;
                if (arrastre.hasString()) {
                    DragInfo info = decode(arrastre.getString());
                    ok = aplicarDropEnFoundation(info, Baseref);
                }
                evento.setDropCompleted(ok);
                evento.consume();
                actualizarZonaSuperior(juego);
                actualizarTablero(juego);
            });
        }

        for (int col = 0; col < vistasColumnas.size(); col++) {
            var lista = vistasColumnas.get(col);
            for (int slot = 0; slot < lista.size(); slot++) {
                GUI.CartaView cartaView = lista.get(slot);
                if (cartaView == null) continue;

                cartaView.getRoot().setOnDragDetected(null);
                final int colFinal = col, slotFinal = slot;

                cartaView.getRoot().setOnDragDetected(evento -> {
                    var tablero = juego.getTableau();
                    if (tablero == null || colFinal >= tablero.size()) return;

                    var cartas = tablero.get(colFinal).getCards();

                    int mostrar = Math.min(lista.size(), cartas.size());
                    int start   = cartas.size() - mostrar;

                    if (slotFinal >= mostrar) return;

                    int comprobacion = start + slotFinal;
                    DeckOfCards.CartaInglesa carta = cartas.get(comprobacion);
                    if (!carta.isFaceup()) return;

                    boolean esTope = (comprobacion == cartas.size() - 1);

                    DragInfo info = new DragInfo();
                    info.origen = Origen.TABLERO;
                    info.col    = colFinal;
                    info.valor  = carta.getValor();
                    info.esTope = esTope;

                    var arrastre = cartaView.getRoot().startDragAndDrop(javafx.scene.input.TransferMode.MOVE);
                    var content = new javafx.scene.input.ClipboardContent();
                    content.putString(encode(info));
                    arrastre.setContent(content);

                    var img = cartaView.getRoot().snapshot(null, null);
                    arrastre.setDragView(img, img.getWidth()/2, img.getHeight()/2);

                    evento.consume();
                });
            }
        }

        contDescarte.setOnDragDetected(evento -> {
            if (juego.getWastePile() != null && juego.getWastePile().hayCartas()) {
                DeckOfCards.CartaInglesa cima = juego.getWastePile().verCarta();
                if (cima != null) {
                    DragInfo info = new DragInfo();
                    info.origen = Origen.DESCARTE;
                    info.col = -1;
                    info.valor = cima.getValor();
                    info.esTope = true;

                    var arrastre = contDescarte.startDragAndDrop(javafx.scene.input.TransferMode.MOVE);
                    var content = new javafx.scene.input.ClipboardContent();
                    content.putString(encode(info));
                    arrastre.setContent(content);

                    // Usa la imagen del descarte como dragView
                    javafx.scene.image.Image arrastreConImagen = contDescarte.snapshot(null, null);
                    arrastre.setDragView(arrastreConImagen, arrastreConImagen.getWidth()/2, arrastreConImagen.getHeight()/2);
                }
            }
            evento.consume();
        });
    }

    private boolean puedeSoltarseEnTablero(DragInfo info, int destinoCol) {
        var tabs = juego.getTableau();
        if (tabs == null) return false;

        DeckOfCards.CartaInglesa primera;
        if (info.origen == Origen.TABLERO) {
            var infoTablero = tabs.get(info.col);
            primera = infoTablero.viewCardStartingAt(info.valor);
            if (primera == null) return false;
            if (info.col == destinoCol) return false;
        } else {
            var descarte = juego.getWastePile();
            primera = (descarte != null) ? descarte.verCarta() : null;
            if (primera == null) return false;
        }

        var finalComprobacion = tabs.get(destinoCol);
        return finalComprobacion.sePuedeAgregarCarta(primera);
    }

    private boolean aplicarDropEnTablero(DragInfo info, int destinoCol) {
        var tablero = juego.getTableau();
        if (tablero == null) return false;

        var comprobacion = tablero.get(destinoCol);

        if (info.origen == Origen.TABLERO) {
            var infoTablero = tablero.get(info.col);
            DeckOfCards.CartaInglesa primera = infoTablero.viewCardStartingAt(info.valor);
            if (primera == null) return false;
            if (!comprobacion.sePuedeAgregarCarta(primera)) return false;
            java.util.ArrayList<DeckOfCards.CartaInglesa> bloque = infoTablero.removeStartingAt(info.valor);
            return comprobacion.agregarBloqueDeCartas(bloque);
        } else {
            var descarte = juego.getWastePile();
            DeckOfCards.CartaInglesa carta = (descarte != null) ? descarte.verCarta() : null;
            if (carta == null) return false;
            if (!comprobacion.sePuedeAgregarCarta(carta)) return false;
            carta = descarte.getCarta();
            return comprobacion.agregarCarta(carta);
        }
    }

    private boolean puedeSoltarseEnFoundation(DragInfo info, int idxBase) {
        var bases = juego.getFoundation();
        if (bases == null || idxBase >= bases.size()) return false;
        var base = bases.get(idxBase);

        DeckOfCards.CartaInglesa carta;
        if (info.origen == Origen.TABLERO) {
            if (!info.esTope) return false;
            var tablero = juego.getTableau();
            var tableroInfo = tablero.get(info.col);
            carta = tableroInfo.getUltimaCarta();
        } else {
            var descarte = juego.getWastePile();
            carta = (descarte != null) ? descarte.verCarta() : null;
        }
        if (carta == null) return false;

        if (base.estaVacio()) {
            return carta.getValorBajo() == 1;
        } else {
            DeckOfCards.CartaInglesa ultima = base.getUltimaCarta();
            return (ultima.tieneElMismoPalo(carta.getPalo()) &&
                    ultima.getValorBajo() + 1 == carta.getValorBajo());
        }
    }

    private boolean aplicarDropEnFoundation(DragInfo info, int idxBase) {
        var bases = juego.getFoundation();
        if (bases == null || idxBase >= bases.size()) return false;
        var base = bases.get(idxBase);

        if (info.origen == Origen.TABLERO) {
            if (!info.esTope) return false;
            var tableroInfo = juego.getTableau().get(info.col);
            DeckOfCards.CartaInglesa top = tableroInfo.getUltimaCarta();
            if (top == null) return false;
            if (base.agregarCarta(top)) {
                tableroInfo.removerUltimaCarta();
                return true;
            }
            return false;
        } else {
            var descarte = juego.getWastePile();
            DeckOfCards.CartaInglesa top = (descarte != null) ? descarte.verCarta() : null;
            if (top == null) return false;
            if (base.agregarCarta(top)) {
                descarte.getCarta();
                return true;
            }
            return false;
        }
    }

}
