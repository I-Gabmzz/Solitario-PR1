package solitaire;

import DeckOfCards.CartaInglesa;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Modela un montículo donde se ponen las cartas
 * de por valor, alternando el color.
 *
 * @author Cecilia M. Curlango
 * @version 2025
 */
public class TableauDeck {
    ArrayList<CartaInglesa> cartas = new ArrayList<>();

    /**
     * Carga las cartas iniciales y voltea la última.
     *
     * @param cartas iniciales
     */
    public void inicializar(ArrayList<CartaInglesa> cartas) {
        this.cartas = cartas;
        // voltear la última carta recibida
        CartaInglesa ultima = cartas.getLast();
        ultima.makeFaceUp();
    }

    /**
     * Remove cards starting from the one with a specified value.
     *
     * @param value of starting card to remove
     * @return removed cards or empty ArrayList if it is not possible to remove.
     */
    public ArrayList<CartaInglesa> removeStartingAt(int value) {
        ArrayList<CartaInglesa> removed = new ArrayList<>();
        Iterator<CartaInglesa> iterator = cartas.iterator();
        while (iterator.hasNext()) {
            CartaInglesa next = iterator.next();
            if (next.isFaceup()) {
                if (next.getValor() <= value) {
                    removed.add(next);
                    iterator.remove();
                }
            }
        }
        return removed;
    }

    public CartaInglesa viewCardStartingAt(int value) {
        CartaInglesa cartaConElValorDeseado = null;
        for (CartaInglesa next : cartas) {
            if (next.isFaceup()) {
                if (next.getValor() <= value) {
                    cartaConElValorDeseado = next;
                    break;
                }
            }
        }
        return cartaConElValorDeseado;
    }

    /**
     * Agrega una carta volteada al montículo. Sólo la agrega si:
     * A) es la siguiente carta en la secuencia
     * B) está vacio y la carta es un Rey
     *
     * @param carta que se intenta almancenar
     * @return true si se pudo guardar la carta, false si no
     */
    public boolean agregarCarta(CartaInglesa carta) {
        boolean agregado = false;

        if (sePuedeAgregarCarta(carta)) {
            carta.makeFaceUp();
            cartas.add(carta);
            agregado = true;
        }
        return agregado;
    }

    /**
     * Obtener la última carta del montículo sin removerla
     *
     * @return la carta que está al final, null si estaba vacio
     */
    CartaInglesa verUltimaCarta() {
        CartaInglesa ultimaCarta = null;
        if (!cartas.isEmpty()) {
            ultimaCarta = cartas.getLast();
        }
        return ultimaCarta;
    }

    /**
     * Remover la última carta del montículo.
     *
     * @return la carta que removió, null si estaba vacio
     */
    CartaInglesa removerUltimaCarta() {
        CartaInglesa ultimaCarta = null;
        if (!cartas.isEmpty()) {
            ultimaCarta = cartas.getLast();
            cartas.remove(ultimaCarta);
            if (!cartas.isEmpty()) {
                // voltea la siguiente carta del tableau
                cartas.getLast().makeFaceUp();
            }
        }
        return ultimaCarta;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (cartas.isEmpty()) {
            builder.append("---");
        } else {
            for (CartaInglesa carta : cartas) {
                builder.append(carta.toString());
            }
        }
        return builder.toString();
    }

    /**
     * Agrega un bloque de cartas al Tableau si la primera carta de las cartas recibidas
     * es de color alterno a la última carta del tableau y también es la siguiente.
     *
     * @param cartasRecibidas
     * @return true si se pudo agregar el bloque, false si no
     */
    public boolean agregarBloqueDeCartas(ArrayList<CartaInglesa> cartasRecibidas) {
        boolean resultado = false;

        if (!cartasRecibidas.isEmpty()) {
            CartaInglesa primera = cartasRecibidas.getFirst();
            // si la primera carta del bloque recibido se puede agregar al tableau actual
            if (sePuedeAgregarCarta(primera)) {
                // se agrega todo el bloque
                cartas.addAll(cartasRecibidas);
                resultado = true;
            }
        }
        return resultado;
    }

    /**
     * Indica si está vacío  el Tableau
     *
     * @return true si no tiene cartas restantes, false si tiene cartas.
     */
    public boolean isEmpty() {
        return cartas.isEmpty();
    }

    /**
     * Verifica si la carta que recibe puede ser la siguiente del tableau actual.
     *
     * @param cartaInicialDePrueba
     * @return true si puede ser la siguiente, false si no
     */
    public boolean sePuedeAgregarCarta(CartaInglesa cartaInicialDePrueba) {
        boolean resultado = false;
        if (!cartas.isEmpty()) {
            CartaInglesa ultima = cartas.getLast();
            if (!ultima.getColor().equals(cartaInicialDePrueba.getColor())) {
                if (ultima.getValor() == cartaInicialDePrueba.getValor() + 1) {
                    resultado = true;
                }
            }
        } else {
            // Está vacio el tableau, solo se puede agregar la cara si es rey
            if (cartaInicialDePrueba.getValor() == 13) {
                resultado = true;
            }
        }
        return resultado;
    }

    /**
     * Obtiene la última carta del Tableau sin removerla.
     * @return última carta, null si no hay cartas
     */
    public CartaInglesa getUltimaCarta() {
        CartaInglesa ultimaCarta = null;
        if (!cartas.isEmpty()) {
            ultimaCarta = cartas.getLast();
        }
        return ultimaCarta;
    }

    public ArrayList<CartaInglesa> getCards() {
        return cartas;
    }
}