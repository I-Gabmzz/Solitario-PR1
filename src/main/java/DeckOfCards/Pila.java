package DeckOfCards;

import java.util.Iterator;

public class Pila<T> implements Iterable<T> {
    private T[] pila;
    private int tope;
    private int size;

    public Pila(int size) {
        pila = (T[]) new Object[size];
        tope = -1;
        this.size = size;
    }

    public void push(T dato) {
        if (estaLlena()) {
            throw new IllegalStateException("La pila está llena");
        }
        tope += 1;
        pila[tope] = dato;
    }

    public void pushTodo(Pila<T> otraPila) {
        for (int i = 0; i <= otraPila.tope; i++) {
            this.push(otraPila.pila[i]);
        }
    }

    public T pop() {
        if(estaVacia()) {
            throw new IllegalStateException("La pila está vacía");
        }
        T dato = pila[tope];
        pila[tope] = null;
        tope -= 1;
        return dato;
    }

    public T peek() {
        if(estaVacia()) {
            throw new IllegalStateException("La pila está vacía");
        }
        return pila[tope];
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int indice = tope;

            @Override
            public boolean hasNext() {
                return indice >= 0;
            }

            @Override
            public T next() {
                if(estaVacia()) {
                }
                return pila[indice++];
            }
        };
    }

    public boolean estaVacia() {
        return tope == -1;
    }
    public boolean estaLlena() {
        return tope == size - 1;
    }
    public int tamañoDePila() {
        return tope + 1;
    }

}
