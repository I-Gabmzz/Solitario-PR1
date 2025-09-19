package solitaire;

import DeckOfCards.CartaInglesa;

import java.util.ArrayList;
/**
 * Modela el montículo donde se colocan las cartas
 * que se extraen de Draw pile.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
public class WastePile {
    private ArrayList<CartaInglesa> cartas;

    public WastePile() {
        cartas = new ArrayList<>();
    }

    public void addCartas(ArrayList<CartaInglesa> nuevas) {
        cartas.addAll(nuevas);
    }

    public ArrayList<CartaInglesa> emptyPile() {
        ArrayList<CartaInglesa> pile = new ArrayList<>();
        if (!cartas.isEmpty()) {
            pile.addAll(cartas);
            cartas = new ArrayList<>();
        }
        return pile;
    }

    /**
     * Obtener la última carta sin removerla.
     * @return Carta que está encima. Si está vacía, es null.
     */
    public CartaInglesa verCarta() {
        CartaInglesa regresar = null;
        if (!cartas.isEmpty()) {
            regresar = cartas.getLast();
        }
        return regresar;
    }
    public CartaInglesa getCarta() {
        CartaInglesa regresar = null;
        if (!cartas.isEmpty()) {
            regresar = cartas.removeLast();
        }
        return regresar;
    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        if (cartas.isEmpty()) {
            stb.append("---");
        } else {
            CartaInglesa regresar = cartas.getLast();
            regresar.makeFaceUp();
            stb.append(regresar.toString());
        }
        return stb.toString();
    }

    public boolean hayCartas() {
        return !cartas.isEmpty();
    }
}
