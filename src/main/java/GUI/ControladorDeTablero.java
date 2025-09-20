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
import solitaire.ControlDeMovimientos;

import java.io.IOException;
import java.util.Objects;
import static GUI.DragInfo.decode;
import static GUI.DragInfo.encode;

// Se declara la creacion de la clase controla el tablero de juego: columnas, bases, mazo y descarte
public class ControladorDeTablero {

    private solitaire.SolitaireGame juego; // Como atributo tiene al juego logíco
    private boolean juegoTerminadoMostrado = false; // Atributo que indíca el estado del juego
    private boolean undoEnProgreso = false; // Atributo que indica si hay algun undo en progreso


    // Elementos de la interfaz
    @FXML
    private ImageView fondo;
    @FXML
    private javafx.scene.layout.HBox centro;
    @FXML
    private javafx.scene.layout.StackPane contMazo;
    @FXML
    private javafx.scene.layout.StackPane contDescarte;
    @FXML
    private javafx.scene.layout.StackPane bas1, bas2, bas3, bas4;
    @FXML
    private Pane col1, col2, col3, col4, col5, col6, col7;
    @FXML
    private Button botonNuevo;
    @FXML
    private Button botonMenu;
    @FXML
    private Button botonUndo;

    // Controladores de las cartas en las columnas (slots visibles)
    @FXML
    private GUI.CartaView col1c1Controller;
    @FXML
    private GUI.CartaView col2c1Controller, col2c2Controller;
    @FXML
    private GUI.CartaView col3c1Controller, col3c2Controller, col3c3Controller;
    @FXML
    private GUI.CartaView col4c1Controller, col4c2Controller, col4c3Controller, col4c4Controller;
    @FXML
    private GUI.CartaView col5c1Controller, col5c2Controller, col5c3Controller, col5c4Controller, col5c5Controller;
    @FXML
    private GUI.CartaView col6c1Controller, col6c2Controller, col6c3Controller, col6c4Controller, col6c5Controller, col6c6Controller;
    @FXML
    private GUI.CartaView col7c1Controller, col7c2Controller, col7c3Controller, col7c4Controller, col7c5Controller, col7c6Controller, col7c7Controller;

    // Listas para el manejo de las columnas
    private java.util.List<javafx.scene.layout.Pane> columnasPanes;
    private java.util.List<java.util.List<GUI.CartaView>> vistasColumnas;


    private enum Origen {TABLERO, DESCARTE} // Atributo para el arrastre

    private static final int MAX_CARTAS = 13; // El numero maximo de cartas por columna
    private int espacioY = 40; // Espacio entre cada carta verticalmente
    private int anchoCarta = 150;
    private int altoCarta = 200;

    // Estilos de los botones (normal, hover y presionado)
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

    // Se ejecuta al iniciar el tablero, basicamente crea el juego, prepara el fondo, botones, columnas y arranca la partida
    @FXML
    private void initialize() {
        juego = new solitaire.SolitaireGame();
        centro.setSpacing(32);
        botonNuevo.setPrefSize(240, 60);
        botonMenu.setPrefSize(200, 60);
        botonUndo.setPrefSize(200, 60);
        actualizarBotonUndo(); // Actualiza el boton undo para su uso de nuevo

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
        darEstiloAlBoton(botonUndo);
        columnasPanes = java.util.List.of(col1, col2, col3, col4, col5, col6, col7);


        java.util.List<GUI.CartaView> c1 = new java.util.ArrayList<>();
        c1.add(col1c1Controller);
        java.util.List<GUI.CartaView> c2 = new java.util.ArrayList<>();
        c2.add(col2c1Controller);
        c2.add(col2c2Controller);
        java.util.List<GUI.CartaView> c3 = new java.util.ArrayList<>();
        c3.add(col3c1Controller);
        c3.add(col3c2Controller);
        c3.add(col3c3Controller);
        java.util.List<GUI.CartaView> c4 = new java.util.ArrayList<>();
        c4.add(col4c1Controller);
        c4.add(col4c2Controller);
        c4.add(col4c3Controller);
        c4.add(col4c4Controller);
        java.util.List<GUI.CartaView> c5 = new java.util.ArrayList<>();
        c5.add(col5c1Controller);
        c5.add(col5c2Controller);
        c5.add(col5c3Controller);
        c5.add(col5c4Controller);
        c5.add(col5c5Controller);
        java.util.List<GUI.CartaView> c6 = new java.util.ArrayList<>();
        c6.add(col6c1Controller);
        c6.add(col6c2Controller);
        c6.add(col6c3Controller);
        c6.add(col6c4Controller);
        c6.add(col6c5Controller);
        c6.add(col6c6Controller);
        java.util.List<GUI.CartaView> c7 = new java.util.ArrayList<>();
        c7.add(col7c1Controller);
        c7.add(col7c2Controller);
        c7.add(col7c3Controller);
        c7.add(col7c4Controller);
        c7.add(col7c5Controller);
        c7.add(col7c6Controller);
        c7.add(col7c7Controller);

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

    // Metodo que crea mas slots en un contenedor hasta llegar a la cantidad pedida
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

    // Refresca todas las columnas mostrando las cartas correctas o slots vacíos
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
            // Asigna las cartas visibles
            for (int j = 0; j < mostrar; j++) {
                var carta = cartas.get(start + j);
                var cartaView = vistas.get(j);

                String tipo = tipoDesdePalo(carta.getPalo());
                cartaView.establecerPorNumero(carta.getValor(), tipo);
                cartaView.establecerBocaArriba(carta.isFaceup());
                cartaView.mostrarSlot(false);
                cartaView.setVisible(true);
            }
            // Rellena el resto con slots
            for (int j = mostrar; j < MAX_CARTAS; j++) {
                var cv = vistas.get(j);
                cv.mostrarSlot(true);
                cv.setVisible(false);
            }
        }
        // Ajusta la posición vertical en cada columna
        acomodarColumna(col1, MAX_CARTAS);
        acomodarColumna(col2, MAX_CARTAS);
        acomodarColumna(col3, MAX_CARTAS);
        acomodarColumna(col4, MAX_CARTAS);
        acomodarColumna(col5, MAX_CARTAS);
        acomodarColumna(col6, MAX_CARTAS);
        acomodarColumna(col7, MAX_CARTAS);
    }

    // Este metodo tiene como utilidad el ordenar las cartas en una columna dándoles separación vertical
    private void acomodarColumna(javafx.scene.layout.Pane col, int cuantosMax) {
        var hijos = col.getChildren();
        int espacio = 0;
        int activos = 0;

        for (var node : hijos) {
            node.setLayoutX(0);
            node.setLayoutY(espacio);
            espacio += espacioY;
            activos++;
            if (activos >= cuantosMax) break;
        }
    }

    // Reinicia el tablero cargando de nuevo la escena
    public void alDarNuevoJuego(javafx.event.ActionEvent actionEvent) throws IOException {
        Parent raizTablero = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GUI/Tablero.fxml")));

        Stage ventanaActual = (Stage) botonNuevo.getScene().getWindow();

        ventanaActual.setScene(new Scene(raizTablero, ventanaActual.getWidth(), ventanaActual.getHeight()));
        ventanaActual.setMaximized(ventanaActual.isMaximized());
        ventanaActual.setFullScreen(ventanaActual.isFullScreen());

        ventanaActual.show();
    }

    // Vuelve al menú principal cerrando el tablero
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

    // Estilo y comportamiento visual de los botones
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

    // Dibuja un rectangulo vacío para representar un slot libre
    private void dibujarSlot(javafx.scene.layout.StackPane cont, int ancho, int alto) {
        cont.getChildren().clear();
        var espacioCarta = new javafx.scene.shape.Rectangle(ancho, alto);
        espacioCarta.setArcWidth(14);
        espacioCarta.setArcHeight(14);
        espacioCarta.setFill(javafx.scene.paint.Color.color(1, 1, 1, 0.10));
        espacioCarta.setStroke(javafx.scene.paint.Color.WHITE);
        espacioCarta.setStrokeWidth(2);
        cont.getChildren().add(espacioCarta);
    }

    // Muestra la imagen de la cubierta en un contenedor
    private void mostrarCubierta(javafx.scene.layout.StackPane cont, int ancho, int alto) {
        cont.getChildren().clear();
        var url = getClass().getResource("/Cartas/Cubierta Carta.png");
        if (url == null) {
            dibujarSlot(cont, ancho, alto);
            return;
        }
        var imageView = new javafx.scene.image.ImageView(new javafx.scene.image.Image(url.toExternalForm()));
        imageView.setFitWidth(ancho);
        imageView.setFitHeight(alto);
        imageView.setPreserveRatio(false);
        cont.getChildren().add(imageView);
    }

    // Muestra una carta específica en un contenedor
    private void mostrarCarta(javafx.scene.layout.StackPane cont,
                              DeckOfCards.CartaInglesa c, int ancho, int alto) {
        cont.getChildren().clear();
        String tipo = tipoDesdePalo(c.getPalo());
        String nombre = nombreDesdeValor(c.getValor());
        String ruta = "/Cartas/" + tipo + "/" + nombre + ".png";
        var url = getClass().getResource(ruta);
        if (url == null) {
            dibujarSlot(cont, ancho, alto);
            return;
        }
        var iv = new javafx.scene.image.ImageView(new javafx.scene.image.Image(url.toExternalForm()));
        iv.setFitWidth(ancho);
        iv.setFitHeight(alto);
        iv.setPreserveRatio(false);
        cont.getChildren().add(iv);
    }

    // Actualiza de forma grafica la parte superior mazo, descarte y bases
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
        var bases = new javafx.scene.layout.StackPane[]{bas1, bas2, bas3, bas4};
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

    // Metodos que traducen palo y valor a sus nombres de imagen
    private String tipoDesdePalo(DeckOfCards.Palo p) {
        if (p == null) return "Pica";
        switch (p) {
            case PICA:
                return "Pica";
            case CORAZON:
                return "Corazon";
            case DIAMANTE:
                return "Diamante";
            case TREBOL:
                return "Trebol";
            default:
                return "Pica";
        }
    }

    private String nombreDesdeValor(int n) {
        switch (n) {
            case 1:
                return "As";
            case 2:
                return "Dos";
            case 3:
                return "Tres";
            case 4:
                return "Cuatro";
            case 5:
                return "Cinco";
            case 6:
                return "Seis";
            case 7:
                return "Siete";
            case 8:
                return "Ocho";
            case 9:
                return "Nueve";
            case 10:
                return "Diez";
            case 11:
                return "Joto";
            case 12:
                return "Queen";
            case 13:
                return "King";
            case 14:
                return "As";
            default:
                return "As";
        }
    }

    // Este metodo es algo complejo, debido a que es el que se encarga de configurar las interacciones como el:
    // clic en mazo, el arrastre a columnas y bases, y arrastre desde descarte.
    private void configurarInteracciones() {
        // Clic en el mazo: roba cartas o recarga si ya no hay
        contMazo.setOnMouseClicked(e -> {
            if (juego.getDrawPile() != null && juego.getDrawPile().hayCartas()) {
                int n = juego.drawCards();
                if (n > 0) actualizarBotonUndo();
            } else {
                int n = juego.reloadDrawPile();
                if (n > 0) actualizarBotonUndo();
            }
            actualizarZonaSuperior(juego);
        });


        // Drop en columnas del tablero
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

        // Drop en bases (foundations)
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

        // Drag desde cada CartaView dentro de las columnas
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
                    if (!carta.isFaceup()) return; // No permite arrastrar si está boca abajo

                    boolean esTope = (comprobacion == cartas.size() - 1);

                    DragInfo info = new DragInfo();
                    info.origen = GUI.DragInfo.Origen.TABLERO;
                    info.col    = colFinal;
                    info.valor  = carta.getValor();
                    info.esTope = esTope;

                    var arrastre = cartaView.getRoot().startDragAndDrop(javafx.scene.input.TransferMode.MOVE);
                    var content = new javafx.scene.input.ClipboardContent();
                    content.putString(encode(info));
                    arrastre.setContent(content);

                    // Usa un snapshot como vista de arrastre
                    var img = cartaView.getRoot().snapshot(null, null);
                    arrastre.setDragView(img, img.getWidth()/2, img.getHeight()/2);

                    evento.consume();
                });
            }
        }

        // Drag desde el descarte (waste)
        contDescarte.setOnDragDetected(evento -> {
            if (juego.getWastePile() != null && juego.getWastePile().hayCartas()) {
                DeckOfCards.CartaInglesa cima = juego.getWastePile().verCarta();
                if (cima != null) {
                    DragInfo info = new DragInfo();
                    info.origen = GUI.DragInfo.Origen.DESCARTE;
                    info.col = -1;
                    info.valor = cima.getValor();
                    info.esTope = true;

                    var arrastre = contDescarte.startDragAndDrop(javafx.scene.input.TransferMode.MOVE);
                    var content = new javafx.scene.input.ClipboardContent();
                    content.putString(encode(info));
                    arrastre.setContent(content);

                    // Imagen de arrastre tomada del nodo del descarte
                    javafx.scene.image.Image img = contDescarte.snapshot(null, null);
                    arrastre.setDragView(img, img.getWidth()/2, img.getHeight()/2);
                }
            }
            evento.consume();
        });
    }

    // Valida si una carta puede soltarse en una columna
    private boolean puedeSoltarseEnTablero(DragInfo info, int destinoCol) {
        var tabs = juego.getTableau();
        if (tabs == null) return false;

        DeckOfCards.CartaInglesa primera;
        if (info.origen == GUI.DragInfo.Origen.TABLERO) {
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

    // Aplica el movimiento de una carta o bloque en el tablero
    private boolean aplicarDropEnTablero(DragInfo info, int destinoCol) {
        var tablero = juego.getTableau();
        if (tablero == null) return false;

        var comprobacion = tablero.get(destinoCol);

        if (info.origen == GUI.DragInfo.Origen.TABLERO) {
            var origenTd = tablero.get(info.col);

            DeckOfCards.CartaInglesa primera = origenTd.viewCardStartingAt(info.valor);
            if (primera == null) return false;
            if (!comprobacion.sePuedeAgregarCarta(primera)) return false;

            DeckOfCards.CartaInglesa antes = origenTd.getUltimaCarta();

            java.util.ArrayList<DeckOfCards.CartaInglesa> bloque = origenTd.removeStartingAt(info.valor);

            boolean ok = comprobacion.agregarBloqueDeCartas(bloque);
            if (!ok) {
                origenTd.agregarBloqueDeCartas(bloque);
                return false;
            }

            DeckOfCards.CartaInglesa despues = origenTd.getUltimaCarta();
            boolean volteo = (antes != despues) && (despues != null) && despues.isFaceup();

            ControlDeMovimientos.registrar(
                    ControlDeMovimientos.tabAlTab(info.col, destinoCol, bloque.size(), volteo)
            ); // Se registra y se guarda el movimiento realizado
            actualizarBotonUndo();
            return true;

        } else {
            var descarte = juego.getWastePile();
            DeckOfCards.CartaInglesa cartaTop = (descarte != null) ? descarte.verCarta() : null;
            if (cartaTop == null) return false;
            if (!comprobacion.sePuedeAgregarCarta(cartaTop)) return false;

            cartaTop = descarte.getCarta(); // extrae de waste
            boolean ok = comprobacion.agregarCarta(cartaTop);
            if (!ok) {
                java.util.ArrayList<DeckOfCards.CartaInglesa> una = new java.util.ArrayList<>();
                una.add(cartaTop);
                descarte.addCartas(una);
                return false;
            }

            ControlDeMovimientos.registrar(
                    ControlDeMovimientos.mazoAlTab(cartaTop, destinoCol)
            );
            actualizarBotonUndo();
            return true;
        }
    }


    // Valida si una carta puede soltarse en una base
    private boolean puedeSoltarseEnFoundation(DragInfo info, int destinoCol) {
        var bases = juego.getFoundation();
        if (bases == null || destinoCol >= bases.size()) return false;
        var base = bases.get(destinoCol);

        DeckOfCards.CartaInglesa carta;
        if (info.origen == GUI.DragInfo.Origen.TABLERO) {
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

    // Aplica el movimiento de una carta en la base y verifica victoria
    private boolean aplicarDropEnFoundation(DragInfo info, int destinoCol) {
        var bases = juego.getFoundation();
        if (bases == null || destinoCol >= bases.size()) return false;
        var base = bases.get(destinoCol);

        if (info.origen == GUI.DragInfo.Origen.TABLERO) {
            if (!info.esTope) return false;

            var origenTd = juego.getTableau().get(info.col);
            DeckOfCards.CartaInglesa top = origenTd.getUltimaCarta();
            if (top == null) return false;

            if (base.agregarCarta(top)) {
                DeckOfCards.CartaInglesa antes = top;
                origenTd.removerUltimaCarta();
                DeckOfCards.CartaInglesa expuesta = origenTd.getUltimaCarta();
                boolean volteo = (expuesta != null) && expuesta.isFaceup();

                ControlDeMovimientos.registrar(
                        ControlDeMovimientos.tabAlFound(info.col, destinoCol, top, volteo)
                ); // Se registra y se guarda el movimiento realizado
                verificarVictoria();
                actualizarBotonUndo();
                return true;
            }
            return false;

        } else {
            var descarte = juego.getWastePile();
            DeckOfCards.CartaInglesa top = (descarte != null) ? descarte.verCarta() : null;
            if (top == null) return false;

            if (base.agregarCarta(top)) {
                descarte.getCarta();
                ControlDeMovimientos.registrar(
                        ControlDeMovimientos.mazoAlFound(top, destinoCol)
                );
                verificarVictoria();
                actualizarBotonUndo();
                return true;
            }
            return false;
        }
    }


    // Metodo que permite conocer si ya se cumplio la condicion de victoria
    private boolean yaSeGano() {
        return juego != null && juego.isGameOver();
    }

    // Metodo que lanza un mensaje de victoria y devuelve al menú
    private void verificarVictoria() {
        if (juegoTerminadoMostrado) return;
        if (!yaSeGano()) return;

        juegoTerminadoMostrado = true;

        javafx.application.Platform.runLater(() -> {
            var alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("¡Victoria!");
            alert.setHeaderText("Has ganado la partida");
            alert.setContentText("Todas las cartas están en las bases.");
            alert.showAndWait();
            try {
                alDarMenu(null);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Metodo que habilita el boton de undo nuevamente despues de hacer uno
    private void actualizarBotonUndo() {
        boolean habilitar = ControlDeMovimientos.puedeDeshacer();
        botonUndo.setDisable(!habilitar);
    }

    // Metodo el cual hace posible la acción de undo una vez que se presiona el boton.
    @FXML
    private void alPresionarUndo() {
        if (undoEnProgreso) return;
        if (!ControlDeMovimientos.puedeDeshacer()) return;

        undoEnProgreso = true;
        botonUndo.setDisable(true);
        try {
            boolean hecho = ControlDeMovimientos.deshacer(
                    juego.getDrawPile(),
                    juego.getWastePile(),
                    juego.getFoundation().toArray(new solitaire.FoundationDeck[0]),
                    juego.getTableau().toArray(new solitaire.TableauDeck[0])
            );
            if (hecho) {
                actualizarZonaSuperior(juego);
                actualizarTablero(juego);
            }
        } finally {
            undoEnProgreso = false;
            actualizarBotonUndo();
        }
    }




}
