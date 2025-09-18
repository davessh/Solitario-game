package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Pila;
import java.util.ArrayList;
/**
 * Modela el montículo donde se colocan las cartas
 * que se extraen de Draw pile.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
public class WastePile {
    //private ArrayList<CartaInglesa> cartas;
    private Pila<CartaInglesa> cartas;
    public WastePile() {
        //cartas = new ArrayList<>();
        cartas = new Pila<>(52);
    }

    public void addCartas(ArrayList<CartaInglesa> nuevas) {
        if (nuevas != null && !nuevas.isEmpty()) {
            for(CartaInglesa carta : nuevas) {
                if(!cartas.pilaLlena()) {
                    cartas.push(carta);
                }
            }
        }
    }
    public ArrayList<CartaInglesa> emptyPile() {
        ArrayList<CartaInglesa> pile = new ArrayList<>();
        while(!cartas.pilaVacia()) {
            pile.add(cartas.pop());
        }
        return pile;
    }

    /**
     * Obtener la última carta sin removerla.
     * @return Carta que está encima. Si está vacía, es null.
     */
    public CartaInglesa verCarta() {
        if (cartas.pilaVacia()) {
            return null;
        }
        CartaInglesa carta = cartas.pop();
        cartas.push(carta);
        return carta;
    }
    public CartaInglesa getCarta() {
        if (cartas.pilaVacia()) {
            return null;
        }
        return cartas.pop();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        if(cartas.pilaVacia()) {
            s.append("----");
        }else {
            CartaInglesa carta = verCarta();
            if(carta != null) {
                carta.makeFaceUp();
                s.append(carta.toString());
            }
        }
        return s.toString();
    }

    public boolean hayCartas() {
        return !cartas.pilaVacia();
    }

    public int size() {
        return cartas.getTope() + 1;
    }
}
