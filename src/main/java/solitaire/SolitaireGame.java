package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;
import java.util.ArrayList;

/**
 * Juego de solitario con funcionalidad Undo.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
public class SolitaireGame {
    ArrayList<TableauDeck> tableau = new ArrayList<>();
    ArrayList<FoundationDeck> foundation = new ArrayList<>();
    FoundationDeck lastFoundationUpdated;
    DrawPile drawPile;
    WastePile wastePile;
    private UndoController undoManager;

    public SolitaireGame() {
        drawPile = new DrawPile();
        wastePile = new WastePile();
        undoManager = new UndoController();
        createTableaux();
        createFoundations();
        wastePile.addCartas(drawPile.retirarCartas());
    }

    /**
     * Guarda el estado actual antes de realizar un movimiento
     */
    public void guardarEstadoActual(int movimientos) {
        undoManager.guardarEstado(this, movimientos);
    }

    /**
     * Deshace el último movimiento
     */
    public int deshacerMovimiento() {
        EstadoDeJuego estadoAnterior = undoManager.deshacerUltimoEstado();
        if (estadoAnterior != null) {
            restaurarEstado(estadoAnterior);
            return estadoAnterior.getMovimientos();
        }
        return -1; // No hay estados para deshacer
    }

    /**
     * Restaura el juego a un estado anterior
     */
    private void restaurarEstado(EstadoDeJuego estado) {
        // Limpiar estado actual
        tableau.clear();
        foundation.clear();

        // Restaurar tableaux
        for (int i = 0; i < estado.getTableauStates().size(); i++) {
            TableauDeck nuevoTableau = new TableauDeck();
            ArrayList<CartaInglesa> cartas = estado.getTableauStates().get(i);
            if (!cartas.isEmpty()) {
                nuevoTableau.inicializar(new ArrayList<>(cartas));
                // Asegurar que las cartas mantengan su estado face up/down
                for (int j = 0; j < cartas.size(); j++) {
                    CartaInglesa cartaOriginal = cartas.get(j);
                    CartaInglesa cartaTableau = nuevoTableau.getCards().get(j);
                    if (cartaOriginal.isFaceup()) {
                        cartaTableau.makeFaceUp();
                    } else {
                        cartaTableau.makeFaceDown();
                    }
                }
            }
            tableau.add(nuevoTableau);
        }

        // Restaurar foundations
        for (int i = 0; i < 4; i++) {
            Palo palo = Palo.values()[i];
            FoundationDeck nuevoFoundation = new FoundationDeck(palo);
            ArrayList<CartaInglesa> cartas = estado.getFoundationStates().get(i);
            for (CartaInglesa carta : cartas) {
                nuevoFoundation.agregarCarta(carta);
            }
            foundation.add(nuevoFoundation);
        }

        // Restaurar draw pile
        drawPile = new DrawPile();
        // Limpiar draw pile actual completamente
        while (drawPile.hayCartas()) {
            drawPile.getCartas(1);
        }
        // Recargar SOLO si hay cartas en el estado
        ArrayList<CartaInglesa> drawPileCartas = estado.getDrawPileState();
        if (!drawPileCartas.isEmpty()) {
            // Crear nuevas copias de las cartas para evitar referencias compartidas
            ArrayList<CartaInglesa> cartasParaRecargar = new ArrayList<>();
            for (CartaInglesa carta : drawPileCartas) {
                CartaInglesa nuevaCarta = new CartaInglesa(carta.getValor(), carta.getPalo(), carta.getColor());
                nuevaCarta.makeFaceDown();
                cartasParaRecargar.add(nuevaCarta);
            }
            drawPile.recargar(cartasParaRecargar);
        }

        // Restaurar waste pile
        wastePile = new WastePile();
        if (!estado.getWastePileState().isEmpty()) {
            // Invertir el orden para mantener la estructura de pila correcta
            ArrayList<CartaInglesa> wasteReversed = new ArrayList<>();
            for (int i = estado.getWastePileState().size() - 1; i >= 0; i--) {
                wasteReversed.add(estado.getWastePileState().get(i));
            }
            wastePile.addCartas(wasteReversed);
        }
    }

    /**
     * Verifica si hay movimientos que se pueden deshacer
     */
    public boolean puedeDeshacer() {
        return undoManager.hayEstadosParaDeshacer();
    }

    /**
     * Limpia el historial de Undo
     */
    public void limpiarHistorialUndo() {
        undoManager.limpiarEstados();
    }

    /**
     * Move cards from Waste pile to Draw Pile.
     */
    public void reloadDrawPile() {
        ArrayList<CartaInglesa> cards = wastePile.emptyPile();
        drawPile.recargar(cards);
    }

    /**
     * Move cards from Draw pile to Waste Pile.
     */
    public void drawCards() {
        ArrayList<CartaInglesa> cards = drawPile.retirarCartas();
        wastePile.addCartas(cards);
    }

    /**
     * Tomar la carta del Waste pile y ponerla en el tableau
     *
     * @param tableauDestino donde se coloca la carta
     * @return true si se pudo hacer el movimiento, false si no
     */
    public boolean moveWasteToTableau(int tableauDestino) {
        boolean movimientoRealizado = false;
        TableauDeck destino = tableau.get(tableauDestino - 1);
        if (moveWasteToTableau(destino)) {
            movimientoRealizado = true;
        }
        return movimientoRealizado;
    }

    /**
     * Tomar varias cartas del Tableau fuente y colocarlas en el
     * Tableau destino.
     *
     * @param tableauFuente  de donde se toma la carta (1-7)
     * @param tableauDestino donde se coloca la carta (1-7)
     * @return true si se pudo hacer el movimiento, false si no
     */
    public boolean moveTableauToTableau(int tableauFuente, int tableauDestino) {
        boolean movimientoRealizado = false;
        TableauDeck fuente = tableau.get(tableauFuente - 1);
        if (!fuente.isEmpty()) {
            TableauDeck destino = tableau.get(tableauDestino - 1);

            int valorQueDebeTenerLaCartaInicialDeLaFuente;
            CartaInglesa cartaUltimaDelDestino; // aqui se coloca la fuente
            if (!destino.isEmpty()) {
                // si hay cartas en el destino, la ultima y primer debe concordar
                cartaUltimaDelDestino = destino.verUltimaCarta();
                valorQueDebeTenerLaCartaInicialDeLaFuente = cartaUltimaDelDestino.getValor() - 1;
            } else {
                // si el destino está vacío, solo puede colocar rey
                valorQueDebeTenerLaCartaInicialDeLaFuente = 13;
            }
            // ver que carta es la del inicio del bloque
            CartaInglesa cartaInicialDePrueba = fuente.viewCardStartingAt(valorQueDebeTenerLaCartaInicialDeLaFuente);
            if (cartaInicialDePrueba != null && destino.sePuedeAgregarCarta(cartaInicialDePrueba)) {
                ArrayList<CartaInglesa> cartas = fuente.removeStartingAt(valorQueDebeTenerLaCartaInicialDeLaFuente);
                if (destino.agregarBloqueDeCartas(cartas)) {
                    if (!fuente.isEmpty()) {
                        // Voltear la carta que se destapa en el Tableau
                        fuente.verUltimaCarta().makeFaceUp();
                    }
                    movimientoRealizado = true;
                }
            }
        }
        return movimientoRealizado;
    }

    /**
     * Tomar la carta de Tableau y colocarla en el Foundation.
     *
     * @param numero de tableau donde se moverá la carta (1-7)
     * @return true si se pudo move la carta, false si no
     */
    public boolean moveTableauToFoundation(int numero) {
        boolean movimientoRealizado = false;

        TableauDeck fuente = tableau.get(numero - 1);
        if (!fuente.isEmpty()) {
            CartaInglesa ultimaCarta = fuente.verUltimaCarta();
            if (ultimaCarta != null && ultimaCarta.isFaceup()) {
                CartaInglesa carta = fuente.removerUltimaCarta();
                if (moveCartaToFoundation(carta)) {
                    movimientoRealizado = true;
                } else {
                    fuente.agregarCarta(carta);
                }
            }
        }
        return movimientoRealizado;
    }

    /**
     * Tomar la carta de Waste y colocarla en el Tableau.
     *
     * @param tableau donde se moverá la carta
     * @return true si se pudo move la carta, false si no
     */
    public boolean moveWasteToTableau(TableauDeck tableau) {
        boolean movimientoRealizado = false;

        CartaInglesa carta = wastePile.verCarta();
        if (moveCartaToTableau(carta, tableau)) {
            carta = wastePile.getCarta();
            movimientoRealizado = true;
        }
        return movimientoRealizado;
    }

    /**
     * Tomar una carta de Waste y ponerla en una de las Foundations.
     *
     * @return true si se pudo hacer el movimiento.
     */
    public boolean moveWasteToFoundation() {
        boolean movimientoRealizado = false;

        CartaInglesa carta = wastePile.verCarta();
        if (moveCartaToFoundation(carta)) {
            carta = wastePile.getCarta();
            movimientoRealizado = true;
        }
        return movimientoRealizado;
    }

    /**
     * Coloca la carta recibida en el Tableau recibido.
     *
     * @param carta   a colocar
     * @param destino Tableau que recibe la carta.
     * @return true si se pudo hacer el movimiento, false si no
     */
    private boolean moveCartaToTableau(CartaInglesa carta, TableauDeck destino) {
        return destino.agregarCarta(carta);
    }

    /**
     * Coloca la carta recibida en el Foundation correspondiente.
     *
     * @param carta a colocar
     * @return true si se pudo hacer el movimiento, false si no.
     */
    private boolean moveCartaToFoundation(CartaInglesa carta) {
        int cualFoundation = carta.getPalo().ordinal();
        FoundationDeck destino = foundation.get(cualFoundation);
        lastFoundationUpdated = destino;
        return destino.agregarCarta(carta);
    }

    /**
     * Determina si se terminó el juego. El juego se
     * termina cuando todas las cartas están en Foundation
     *
     * @return true si se terminó el juego
     */
    public boolean isGameOver() {
        boolean gameOver = true;
        for (FoundationDeck foundation : foundation) {
            if (foundation.estaVacio()) {
                gameOver = false;
            } else {
                CartaInglesa ultimaCarta = foundation.getUltimaCarta();
                // si la última carta no es rey, no se ha terminado
                if (ultimaCarta.getValor() != 13) {
                    gameOver = false;
                }
            }
        }
        return gameOver;
    }

    private void createFoundations() {
        for (Palo palo : Palo.values()) {
            foundation.add(new FoundationDeck(palo));
        }
    }

    private void createTableaux() {
        for (int i = 0; i < 7; i++) {
            TableauDeck tableauDeck = new TableauDeck();
            tableauDeck.inicializar(drawPile.getCartas(i + 1));
            tableau.add(tableauDeck);
        }
    }

    public DrawPile getDrawPile() {
        return drawPile;
    }

    public ArrayList<TableauDeck> getTableau() {
        return tableau;
    }

    public WastePile getWastePile() {
        return wastePile;
    }

    public FoundationDeck getLastFoundationUpdated() {
        return lastFoundationUpdated;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        // add foundations
        str.append("Foundation\n");
        for (FoundationDeck foundationDeck : foundation) {
            str.append(foundationDeck);
            str.append("\n");
        }

        // add tableaux
        str.append("\nTableaux\n");
        int tableauNumber = 1;
        for (TableauDeck tableauDeck : tableau) {
            str.append(tableauNumber + " ");
            str.append(tableauDeck);
            str.append("\n");
            tableauNumber++;
        }
        str.append("Waste\n");
        str.append(wastePile);
        str.append("\nDraw\n");
        str.append(drawPile);
        return str.toString();
    }

    public ArrayList<FoundationDeck> getFoundations() {
        return foundation;
    }
}