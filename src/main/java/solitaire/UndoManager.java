package solitaire;

import DeckOfCards.Pila;

/**
 * Administra los estados del juego para permitir funcionalidad de Undo.
 * @version 2025
 */
public class UndoManager {
    private Pila<GameState> estadosGuardados;
    private static final int MAX_ESTADOS = 50; // Máximo número de estados a guardar

    public UndoManager() {
        estadosGuardados = new Pila<>(MAX_ESTADOS);
    }

    /**
     * Guarda el estado actual del juego
     */
    public void guardarEstado(SolitaireGame game, int movimientos) {
        try {
            if (!estadosGuardados.pilaLlena()) {
                GameState estado = GameState.createFromGame(game, movimientos);
                estadosGuardados.push(estado);
            } else {
                // Si la pila está llena, podríamos implementar una rotación
                // Por simplicidad, ignoramos estados adicionales
                System.out.println("Máximo de estados alcanzado");
            }
        } catch (Exception e) {
            System.err.println("Error al guardar estado: " + e.getMessage());
        }
    }

    /**
     * Restaura el último estado guardado
     */
    public GameState deshacerUltimoEstado() {
        if (!estadosGuardados.pilaVacia()) {
            return estadosGuardados.pop();
        }
        return null;
    }

    /**
     * Verifica si hay estados disponibles para deshacer
     */
    public boolean hayEstadosParaDeshacer() {
        return !estadosGuardados.pilaVacia();
    }

    /**
     * Limpia todos los estados guardados
     */
    public void limpiarEstados() {
        estadosGuardados.limpiarPila();
    }

    /**
     * Obtiene el número de estados guardados
     */
    public int getNumeroEstadosGuardados() {
        return estadosGuardados.tamaño();
    }
}