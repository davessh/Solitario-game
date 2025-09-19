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
    //private ArrayList<CartaInglesa> cartas;
    private Pila<CartaInglesa> cartas;
    private int cuantasCartasSeEntregan = 3;

    public DrawPile() {
//        DeckOfCards.Mazo mazo = new DeckOfCards.Mazo();
//        cartas = mazo.getCartas();
//        setCuantasCartasSeEntregan(3); // Cambiar a 3 en lugar de dejar el default
        cartas = new Pila<>(52);
        DeckOfCards.Mazo mazo = new DeckOfCards.Mazo();
        ArrayList<CartaInglesa> cartasDelMazo = mazo.getCartas();
                for(CartaInglesa carta : cartasDelMazo) {
                    if(!cartas.pilaLlena()){
                        cartas.push(carta);
                    }
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

        for(int i  = 0; i < cantidad && !cartas.pilaVacia(); i++) {
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
        int maximoARetirar = Math.min(cartas.getTope() + 1, cuantasCartasSeEntregan);

        for (int i = 0; i < maximoARetirar && !cartas.pilaVacia(); i++) {
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
        return !cartas.pilaVacia();
    }

    public CartaInglesa verCarta() {
        if (cartas.pilaVacia()) {
            return null;
        }

        CartaInglesa carta = cartas.pop();
        cartas.push(carta);
        return carta;
    }
    /**
     * Agrega las cartas recibidas al monton y las voltea
     * para que no se vean las caras.
     * @param cartasAgregar cartas que se agregan
     */
    public void recargar(ArrayList<CartaInglesa> cartasAgregar) {
        while(!cartas.pilaVacia()) {
            cartas.pop();
        }

        for(CartaInglesa carta : cartasAgregar) {
            carta.makeFaceDown();
            if(!cartas.pilaLlena()){
                cartas.push(carta);
        }

        }
    }

    @Override
    public String toString() {
        if (cartas.pilaVacia()) {
            return "-E-";
        }
        return "@";
    }
}
