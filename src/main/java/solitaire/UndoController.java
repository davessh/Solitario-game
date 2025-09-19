package solitaire;

import DeckOfCards.Pila;

public class UndoController {
    private Pila<EstadoDeJuego> estadosGuardados;
    private static final int MAX_ESTADOS = 50; // Máximo número de estados a guardar

    public UndoController() {
        estadosGuardados = new Pila<>(MAX_ESTADOS);
    }

    public void guardarEstado(SolitaireGame game, int movimientos) {
        try {
            if (!estadosGuardados.pilaLlena()) {
                EstadoDeJuego estado = EstadoDeJuego.createFromGame(game, movimientos);
                estadosGuardados.push(estado);
            } else {
                System.out.println("Error, se alcanzo el maximo de estados posible.");
            }
        } catch (Exception e) {
            System.err.println("Error al guardar estado: " + e.getMessage());
        }
    }

    public EstadoDeJuego deshacerUltimoEstado() {
        if (!estadosGuardados.pilaVacia()) {
            return estadosGuardados.pop();
        }
        return null;
    }

    public boolean hayEstadosParaDeshacer() {
        return !estadosGuardados.pilaVacia();
    }

    public void limpiarEstados() {
        estadosGuardados.limpiarPila();
    }


    public int getNumeroEstadosGuardados() {
        return estadosGuardados.tamaño();
    }
}