package DeckOfCards;


public class Pila<T> {
    T[] pila;
    int tamaño;
    int tope;

    public Pila(int tamaño) {
        this.tamaño = tamaño;
        this.pila = (T[]) new Object[tamaño];
        this.tope = -1;
    }

    public void push(T dato) {
        if(tope == tamaño -1) {
            throw new IllegalStateException("Desbordamiento");
        }
        tope++;
        pila[tope] = dato;
    }

    public T pop() {
        if(tope == -1) {
            throw new IllegalStateException("Subdesbordamiento");
        }
        T dato = pila[tope];
        tope--;
        return dato;
    }

    public boolean pilaVacia() {
        if (tope == -1){
            return true;
        }
        return false;
    }

    public boolean pilaLlena() {
        if (tope == tamaño -1){
            return true;
        }
        return false;
    }

    public int getTamaño(){
        return tamaño;
    }

    public int getTope(){
        return tope;
    }

    public void setTope(int tope) {
        this.tope = tope;
    }

    public String toString() {
        if(pilaVacia()) {
            return "Pila vacia";
        }
        return "Valor: " + pila[tope].toString();
    }
}
