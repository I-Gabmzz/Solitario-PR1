package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Pila;

import java.util.ArrayList;

/**
 * Modela un mazo de cartas de solitario.
 * @author Cecilia Curlango
 * @version 2025
 */
public class DrawPile {
    private Pila<CartaInglesa> cartas;
    private int cuantasCartasSeEntregan = 3;

    public DrawPile() {
        DeckOfCards.Mazo mazo = new DeckOfCards.Mazo();
        ArrayList<CartaInglesa> cartasTotales = mazo.getCartas();
        cartas = new Pila<>(cartasTotales.size());
        for (CartaInglesa carta : cartasTotales) {
            cartas.push(carta);
        }
        setCuantasCartasSeEntregan(3);
    }

    /**
     * Establece cuantas cartas se sacan cada vez.
     * Puede ser 1 o 3 normalmente.
     * @param cuantasCartasSeEntregan
     */
    public void setCuantasCartasSeEntregan(int cuantasCartasSeEntregan) {
        this.cuantasCartasSeEntregan = cuantasCartasSeEntregan;
    }

    /**
     * Regresa la cantidad de cartas que se sacan cada vez.
     * @return cantidad de cartas que se entregan
     */
    public int getCuantasCartasSeEntregan() {
        return cuantasCartasSeEntregan;
    }

    /**
     * Retirar una cantidad de cartas. Este método se utiliza al inicio
     * de una partida para cargar las cartas de los tableaus.
     * Si se tratan de remover más cartas de las que hay,
     * se provocará un error.
     * @param cantidad de cartas que se quieren a retirar
     * @return cartas retiradas
     */
    public ArrayList<CartaInglesa> getCartas(int cantidad) {
        ArrayList<CartaInglesa> retiradas = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            retiradas.add(cartas.pop());
        }
        return retiradas;
    }

    /**
     * Retira y entrega las cartas del monton. La cantidad que retira
     * depende de cuántas cartas quedan en el montón y serán hasta el máximo
     * que se configuró inicialmente.
     * @return Cartas retiradas.
     */
    public ArrayList<CartaInglesa> retirarCartas() {
        ArrayList<CartaInglesa> retiradas = new ArrayList<>();
        int maximoARetirar = Math.min(cartas.tamañoDePila(), cuantasCartasSeEntregan);

        for (int i = 0; i < maximoARetirar; i++) {
            CartaInglesa retirada = cartas.pop();
            retirada.makeFaceUp();
            retiradas.add(retirada);
        }
        return retiradas;
    }

    /**
     * Indica si aún quedan cartas para entregar.
     * @return true si hay cartas, false si no.
     */
    public boolean hayCartas() {
        return cartas.tamañoDePila() > 0;
    }

    public CartaInglesa verCarta() {
        return cartas.peek();
    }

    /**
     * Agrega las cartas recibidas al monton y las voltea
     * para que no se vean las caras.
     * @param cartasAgregar cartas que se agregan
     */
    public void recargar(ArrayList<CartaInglesa> cartasAgregar) {
        cartas = new Pila<>(cartasAgregar.size());
        for (int i = 0; i < cartasAgregar.size(); i++) {
            CartaInglesa aCarta = cartasAgregar.get(i);
            aCarta.makeFaceDown();
            cartas.push(aCarta);
        }
    }


    @Override
    public String toString() {
        if (cartas.estaVacia()) {
            return "-E-";
        }
        return "@";
    }

    // Devuelve n cartas desde el tope de draw hacia waste
    public ArrayList<CartaInglesa> sacarParaWaste(int n) {
        ArrayList<CartaInglesa> sacadas = new ArrayList<>();
        int max = Math.min(n, cartas.tamañoDePila());
        for (int i = 0; i < max; i++) {
            CartaInglesa carta = cartas.pop();
            carta.makeFaceUp();
            sacadas.add(carta);
        }
        return sacadas;
    }

    // Se encarga de regresar las cartas del waste, se acomodan en inverso y se voltean, conservando su estado
    public void regresarDesdeWaste(ArrayList<CartaInglesa> devueltas) {
        for (int i = devueltas.size() - 1; i >= 0; i--) {
            CartaInglesa carta = devueltas.get(i);
            carta.makeFaceDown();
            cartas.push(carta);
        }
    }

}
