package solitaire;

import DeckOfCards.CartaInglesa;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;


public class ControlDeMovimientos {
    public String tipoMovimiento;
    public int origenTablero = -1;
    public int destinoTablero = -1;
    public int foundation = -1;
    public int cantidad = 0;
    public boolean volteo = false;
    public CartaInglesa carta;
    public ArrayList<CartaInglesa> cartas;
    private static final Deque<ControlDeMovimientos> pila = new ArrayDeque<>();

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

    public static ControlDeMovimientos draw(int totalCartas) {
        return rastrearMovimiento("Mazo", -1, -1, -1, totalCartas, false, null);
    }
    public static ControlDeMovimientos recargar(int totalCartas) {
        return rastrearMovimiento("Recarga", -1, -1, -1, totalCartas, false, null);
    }
    public static ControlDeMovimientos mazoAlTab(CartaInglesa carta, int alTablero) {
        return rastrearMovimiento("MazoAlTab", -1, alTablero, -1, 0, false, carta);
    }
    public static ControlDeMovimientos mazoAlFound(CartaInglesa carta, int foundation) {
        return rastrearMovimiento("MazoAlFound", -1, -1, foundation, 0, false, carta);
    }
    public static ControlDeMovimientos tabAlTab(int origen, int destino, int n, boolean volteoAlSalir) {
        return rastrearMovimiento("TabAlTab", origen, destino, -1, n, volteoAlSalir, null);
    }
    public static ControlDeMovimientos tabAlFound(int origen, int foundation, CartaInglesa carta, boolean volteoAlSalir) {
        return rastrearMovimiento("TabAlFound", origen, -1, foundation, 0, volteoAlSalir, carta);
    }


    public static void registrar(ControlDeMovimientos mov) {
        pila.push(mov);
    }

    public static boolean puedeDeshacer() {
        return !pila.isEmpty();
    }

    public static boolean deshacer(DrawPile draw,
                                   WastePile waste,
                                   FoundationDeck[] foundations,
                                   TableauDeck[] tablero) {
        if (pila.isEmpty()) return false;

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
