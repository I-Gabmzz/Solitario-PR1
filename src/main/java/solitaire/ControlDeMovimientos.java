package solitaire;

// Librerías necesarias para la clase
import DeckOfCards.CartaInglesa;
import java.util.ArrayList;
import DeckOfCards.Pila;

// Se declara la clase ControlDeMovimientos que se encarga de controlar y guardar los movimientos realizados
public class ControlDeMovimientos {

    // Se declaran los atributos pertenecientes a la clase
    public String tipoMovimiento; // Movimiento de donde a donde
    public int origenTablero = -1; // Origen default
    public int destinoTablero = -1; // Destino default
    public int foundation = -1; // Foundation default
    public int cantidad = 0; // Cantidad de cartas
    public boolean volteo = false; // Estado de volteo
    public CartaInglesa carta; // La carta en si
    public ArrayList<CartaInglesa> cartas; // Bloques de cartas
    private static final Pila<ControlDeMovimientos> pila = new Pila<>(10000); // Pila que registra los movimientos

    // Metodo el cual crea un registro acerca del movimiento hecho según los datos de este mismo
    private static ControlDeMovimientos rastrearMovimiento(String tipo,
                                                           int origen, int destino, int foundation,
                                                           int cantidad, boolean volteo,
                                                           CartaInglesa carta) {
        ControlDeMovimientos movimientoDeCarta = new ControlDeMovimientos();
        movimientoDeCarta.tipoMovimiento = tipo;
        movimientoDeCarta.origenTablero = origen;
        movimientoDeCarta.destinoTablero = destino;
        movimientoDeCarta.foundation = foundation;
        movimientoDeCarta.cantidad = cantidad;
        movimientoDeCarta.volteo = volteo;
        movimientoDeCarta.carta = carta;
        return movimientoDeCarta;
    }

    // Registra el robo del mazo al area de waste
    public static ControlDeMovimientos draw(int totalCartas) {
        return rastrearMovimiento("Mazo", -1, -1, -1, totalCartas, false, null);
    }
    // Registra la recarga del waste al mazo
    public static ControlDeMovimientos recargar(int totalCartas) {
        return rastrearMovimiento("Recarga", -1, -1, -1, totalCartas, false, null);
    }
    // Registro del movimiento de una carta del mazo al tablero
    public static ControlDeMovimientos mazoAlTab(CartaInglesa carta, int alTablero) {
        return rastrearMovimiento("MazoAlTab", -1, alTablero, -1, 0, false, carta);
    }
    // Registro del movimiento de una carta del mazo al foundation
    public static ControlDeMovimientos mazoAlFound(CartaInglesa carta, int foundation) {
        return rastrearMovimiento("MazoAlFound", -1, -1, foundation, 0, false, carta);
    }
    // Registro del movimiento de una carta del tablero al tablero
    public static ControlDeMovimientos tabAlTab(int origen, int destino, int n, boolean volteoAlSalir) {
        return rastrearMovimiento("TabAlTab", origen, destino, -1, n, volteoAlSalir, null);
    }
    // Registro del movimiento de una carta del tablero al foundation
    public static ControlDeMovimientos tabAlFound(int origen, int foundation, CartaInglesa carta, boolean volteoAlSalir) {
        return rastrearMovimiento("TabAlFound", origen, -1, foundation, 0, volteoAlSalir, carta);
    }

    // Metodo que guarda los datos del movimiento hecho
    public static void registrar(ControlDeMovimientos mov) {
        pila.push(mov);
    }

    // Se valida si es que se puede seguir haciendo undo
    public static boolean puedeDeshacer() {
        return !pila.estaVacia();
    }

    // Metodo el cual como tal revierte el ultimo movimiento hecho segun los datos guardados
    public static boolean deshacer(DrawPile draw,
                                   WastePile waste,
                                   FoundationDeck[] foundations,
                                   TableauDeck[] tablero) {
        if (pila.estaVacia()) return false;

        ControlDeMovimientos movimiento = pila.pop();

        switch (movimiento.tipoMovimiento) {
            case "Mazo": {
                ArrayList<CartaInglesa> devueltas = new ArrayList<>();
                for (int i = 0; i < movimiento.cantidad; i++) {
                    CartaInglesa c = waste.getCarta();
                    if (c != null) {
                        c.makeFaceDown();
                        devueltas.add(c);
                    }
                }
                draw.regresarDesdeWaste(devueltas);
                return true;
            }

            case "Recarga": {
                ArrayList<CartaInglesa> haciaWaste = draw.sacarParaWaste(movimiento.cantidad);
                if (!haciaWaste.isEmpty()) {
                    waste.addCartas(haciaWaste);
                }
                return true;
            }

            case "MazoAlTab": {
                TableauDeck t = tablero[movimiento.destinoTablero];
                CartaInglesa removida = t.removerUltimaCarta();
                if (removida != null) {
                    ArrayList<CartaInglesa> una = new ArrayList<>();
                    removida.makeFaceUp();
                    una.add(removida);
                    waste.addCartas(una);
                    return true;
                }
                return false;
            }

            case "MazoAlFound": {
                FoundationDeck found = foundations[movimiento.foundation];
                CartaInglesa removida = found.removerUltimaCarta();
                if (removida != null) {
                    ArrayList<CartaInglesa> cartas = new ArrayList<>();
                    removida.makeFaceUp();
                    cartas.add(removida);
                    waste.addCartas(cartas);
                    return true;
                }
                return false;
            }

            case "TabAlTab": {
                TableauDeck destino = tablero[movimiento.destinoTablero];
                TableauDeck origen = tablero[movimiento.origenTablero];
                ArrayList<CartaInglesa> bloque = destino.removerUltimasSinVoltear(movimiento.cantidad);
                if (movimiento.volteo) {
                    origen.voltearUltimaFaceDown();
                }
                if (!bloque.isEmpty()) {
                    origen.agregarBloqueDeCartasForzado(bloque);
                    return true;
                }
                return false;
            }


            case "TabAlFound": {
                FoundationDeck found = foundations[movimiento.foundation];
                CartaInglesa removida = found.removerUltimaCarta();
                if (removida != null) {
                    TableauDeck origen = tablero[movimiento.origenTablero];
                    if (movimiento.volteo) {
                        origen.voltearUltimaFaceDown();
                    }
                    origen.agregarCartaForzado(removida);
                    return true;
                }
                return false;
            }


        }
        return false;
    }
}
