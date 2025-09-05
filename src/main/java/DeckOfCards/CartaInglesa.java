package DeckOfCards;
/**
 * Write a description of class CartaInglesa here.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
public class CartaInglesa extends Carta {

    public CartaInglesa(int valor, Palo figura, String color) {
        super(valor, figura, color);
    }

    @Override
    public int compareTo(Carta o) {
        // Ambas cartas son iguales
        if (getValor() == o.getValor() ) {
            if (getColor().equals(o.getColor())) {
                return 0;
            } else {
                // Tienen mismo valor, distinto palo
                return palo.getPeso() - o.palo.getPeso();
            }
        }
        // Ambas cartas tienen el distinto valor
        return getValor() - o.getValor();
    }
}
