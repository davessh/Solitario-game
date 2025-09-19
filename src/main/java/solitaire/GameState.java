package solitaire;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;

/**
 * Representa un estado del juego de Solitario que puede ser guardado y restaurado.
 * @author Cecilia Curlango
 * @version 2025
 */
public class GameState {
    private ArrayList<ArrayList<CartaInglesa>> tableauStates;
    private ArrayList<ArrayList<CartaInglesa>> foundationStates;
    private ArrayList<CartaInglesa> drawPileState;
    private ArrayList<CartaInglesa> wastePileState;
    private int movimientos;

    public GameState() {
        tableauStates = new ArrayList<>();
        foundationStates = new ArrayList<>();
        drawPileState = new ArrayList<>();
        wastePileState = new ArrayList<>();
    }

    public static GameState createFromGame(SolitaireGame game, int movimientos) {
        GameState state = new GameState();
        state.movimientos = movimientos;

        // Copiar estados de tableaux
        for (TableauDeck tableau : game.getTableau()) {
            ArrayList<CartaInglesa> tableauCopy = new ArrayList<>();
            for (CartaInglesa carta : tableau.getCards()) {
                CartaInglesa cartaCopy = new CartaInglesa(carta.getValor(), carta.getPalo(), carta.getColor());
                if (carta.isFaceup()) {
                    cartaCopy.makeFaceUp();
                } else {
                    cartaCopy.makeFaceDown();
                }
                tableauCopy.add(cartaCopy);
            }
            state.tableauStates.add(tableauCopy);
        }

        // Copiar estados de foundations
        for (FoundationDeck foundation : game.getFoundations()) {
            ArrayList<CartaInglesa> foundationCopy = new ArrayList<>();
            // Extraer todas las cartas del foundation para copiarlas
            ArrayList<CartaInglesa> tempCards = new ArrayList<>();
            while (!foundation.estaVacio()) {
                CartaInglesa carta = foundation.removerUltimaCarta();
                tempCards.add(0, carta); // Insertar al inicio para mantener orden
            }

            // Copiar y restaurar las cartas
            for (CartaInglesa carta : tempCards) {
                CartaInglesa cartaCopy = new CartaInglesa(carta.getValor(), carta.getPalo(), carta.getColor());
                cartaCopy.makeFaceUp();
                foundationCopy.add(cartaCopy);
                foundation.agregarCarta(carta); // Restaurar al foundation original
            }
            state.foundationStates.add(foundationCopy);
        }

        // CORRECCIÓN: Copiar draw pile sin modificar el original
        DrawPile drawPile = game.getDrawPile();
        ArrayList<CartaInglesa> drawCards = new ArrayList<>();

        // Extraer todas las cartas para copiarlas
        while (drawPile.hayCartas()) {
            drawCards.add(0, drawPile.getCartas(1).get(0)); // Insertar al inicio
        }

        // Copiar las cartas para el estado
        for (CartaInglesa carta : drawCards) {
            CartaInglesa cartaCopy = new CartaInglesa(carta.getValor(), carta.getPalo(), carta.getColor());
            cartaCopy.makeFaceDown();
            state.drawPileState.add(cartaCopy);
        }

        // IMPORTANTE: Restaurar el draw pile original
        if (!drawCards.isEmpty()) {
            drawPile.recargar(drawCards);
        }

        // Copiar waste pile
        WastePile wastePile = game.getWastePile();
        ArrayList<CartaInglesa> wasteCards = wastePile.emptyPile();
        for (CartaInglesa carta : wasteCards) {
            CartaInglesa cartaCopy = new CartaInglesa(carta.getValor(), carta.getPalo(), carta.getColor());
            cartaCopy.makeFaceUp();
            state.wastePileState.add(cartaCopy);
        }
        // Restaurar waste pile original
        ArrayList<CartaInglesa> wasteCardsReversed = new ArrayList<>();
        for (int i = wasteCards.size() - 1; i >= 0; i--) {
            wasteCardsReversed.add(wasteCards.get(i));
        }
        wastePile.addCartas(wasteCardsReversed);

        return state;
    }

    // Getters
    public ArrayList<ArrayList<CartaInglesa>> getTableauStates() {
        return tableauStates;
    }

    public ArrayList<ArrayList<CartaInglesa>> getFoundationStates() {
        return foundationStates;
    }

    public ArrayList<CartaInglesa> getDrawPileState() {
        return drawPileState;
    }

    public ArrayList<CartaInglesa> getWastePileState() {
        return wastePileState;
    }

    public int getMovimientos() {
        return movimientos;
    }
}
