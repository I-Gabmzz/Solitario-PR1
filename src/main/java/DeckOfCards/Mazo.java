package DeckOfCards;
/**
 * Write a description of class Mazo here.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
import java.util.ArrayList;
import java.util.Collections;

public class Mazo {
    private ArrayList<CartaInglesa> cartas = new ArrayList<>();

    public Mazo() {
        llenar(); // crea todas las cartas, excluyendo Jokers
        mezclar();
    }

    /**
     * Obtiene todas las cartas del mazo.
     * @return
     */
    public ArrayList<CartaInglesa> getCartas() {
        return cartas;
    }

    public CartaInglesa obtenerUnaCarta() {
        if (cartas.size() > 0) {
            return cartas.remove(0);
        }
        return null;
    }
    private void mezclar() {
        Collections.shuffle(cartas);
    }

    private void llenar() {
        for (int i = 2; i <=14 ; i++) {
            for (Palo palo : Palo.values()) {
                CartaInglesa c = new CartaInglesa(i,palo, palo.getColor());
                cartas.add(c);
            }
        }
    }

    public void ordenar() {
        Collections.sort(cartas);
    }

    @Override
    public String toString() {
        return cartas.toString();
    }
}
