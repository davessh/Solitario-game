package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;
import DeckOfCards.Pila;

import java.util.ArrayList;

/**
 * Modela un monículo donde se ponen las cartas
 * de un solo palo.
 *
 * @author Cecilia M. Curlango
 * @version 2025
 */
public class FoundationDeck {
    Palo palo;
    //ArrayList<CartaInglesa> cartas = new ArrayList<>();
    Pila<CartaInglesa> cartas;

    public FoundationDeck(Palo palo) {
        this.palo = palo;
        this.cartas = new Pila<>(13);
    }

    public FoundationDeck(CartaInglesa carta) {
        palo = carta.getPalo();
        this.cartas = new Pila<>(13);
        // solo agrega la carta si es un A
        if (carta.getValorBajo() == 1) {
            cartas.push(carta);
        }
    }

    /**
     * Agrega una carta al montículo. Sólo la agrega si
     * la carta es del palo del montículo y el la siguiente
     * carta en la secuencia.
     *
     * @param carta que se intenta almancenar
     * @return true si se pudo guardar la carta, false si no
     */
    public boolean agregarCarta(CartaInglesa carta) {
        boolean agregado = false;
        if (carta != null && carta.tieneElMismoPalo(palo)) {
            if (cartas.pilaVacia()) {
                if (carta.getValorBajo() == 1) {
                    // si no hay cartas entonces la carta debe ser un A
                    cartas.push(carta);
                    agregado = true;
                }
            } else {
                // si hay cartas entonces debe haber secuencia
                CartaInglesa ultimaCarta = getUltimaCarta();
                if (ultimaCarta != null && ultimaCarta.getValorBajo() + 1 == carta.getValorBajo()) {
                    // agregar la carta si el la siguiente a la última
                    cartas.push(carta);
                    agregado = true;
                }
            }
        }
        return agregado;
    }

    /**
     * Remover la última carta del montículo.
     *
     * @return la carta que removió, null si estaba vacio
     */
    CartaInglesa removerUltimaCarta() {
       if(cartas.pilaVacia()) {
           return null;
       }
       return cartas.pop();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
       if(cartas.pilaVacia()) {
           builder.append("---");
       } else {
           Pila<CartaInglesa> auxiliar = new Pila<>(13);

           while(!cartas.pilaVacia()) {
               auxiliar.push(cartas.pop());
           }

           while(!auxiliar.pilaVacia()) {
               CartaInglesa carta = auxiliar.pop();
               builder.append(carta.toString());
               cartas.push(carta);
           }
       }
        return builder.toString();
    }

    /**
     * Determina si hay cartas en el Foundation.
     * @return true hay al menos una carta, false no hay cartas
     */
    public boolean estaVacio() {
        return cartas.pilaVacia();
    }

    /**
     * Obtiene la última carta del Foundation sin removerla.
     * @return última carta, null si no hay cartas
     */
    public CartaInglesa getUltimaCarta() {
        if (cartas.pilaVacia()) {
            return null;
        }
        CartaInglesa carta = cartas.pop();
        cartas.push(carta);
        return carta;
    }
}
