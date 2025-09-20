package DeckOfCards;

// Se importan las librerias necesarias para el funcionamiento de la clase.
import java.util.Iterator;

// Se declara la creacion de la clase Pila la cual esta casteada para un elemento y implementa el iterable
public class Pila<T> implements Iterable<T> {
    private T[] pila; // Arreglo base que almacena los datos de la pila
    private int tope; // Indice del elemento que esta en la cima
    private int size; // Tamaño maximo de la pila

    // Metodo constructor el cual crea una pila vacia de cierto tamaño
    public Pila(int size) {
        pila = (T[]) new Object[size];
        tope = -1;
        this.size = size;
    }

    // Metodo para insertar un dato en el tope de la pila
    public void push(T dato) {
        if (estaLlena()) {
            throw new IllegalStateException("La pila está llena");
        }
        tope += 1;
        pila[tope] = dato;
    }

    // Metodo que se encarga de eliminar y devolver el elemento que se encuentre en el tope
    public T pop() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila está vacía");
        }
        T dato = pila[tope];
        pila[tope] = null;
        tope -= 1;
        return dato;
    }

    // Metodo que devuelve sin eliminar el elemento que se encuentra en el tope
    public T peek() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila está vacía");
        }
        return pila[tope];
    }

    // Metodo que permite un iterador sobre la pila
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int indice = tope;

            @Override
            public boolean hasNext() {
                return indice >= 0;
            }

            @Override
            public T next() {
                if (estaVacia()) {
                }
                return pila[indice++];
            }
        };
    }

    // Indica si la pila no tiene elementos
    public boolean estaVacia() {
        return tope == -1;
    }

    // Indica si la pila alcanzó su capacidad máxima
    public boolean estaLlena() {
        return tope == size - 1;
    }

    // Devuelve la cantidad actual de elementos en la pila
    public int tamañoDePila() {
        return tope + 1;
    }
}