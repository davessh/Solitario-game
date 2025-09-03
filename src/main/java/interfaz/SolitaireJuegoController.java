package interfaz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import solitaire.SolitaireGame;
import solitaire.TableauDeck;
import solitaire.FoundationDeck;
import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;

import java.util.ArrayList;

public class SolitaireJuegoController {

    @FXML private Button btnNuevoJuego;
    @FXML private Button btnMenu;

    @FXML private Pane drawPilePane;
    @FXML private Pane wastePilePane;

    @FXML private Pane foundationSpades;
    @FXML private Pane foundationHearts;
    @FXML private Pane foundationDiamonds;
    @FXML private Pane foundationClubs;

    @FXML private VBox tableau1;
    @FXML private VBox tableau2;
    @FXML private VBox tableau3;
    @FXML private VBox tableau4;
    @FXML private VBox tableau5;
    @FXML private VBox tableau6;
    @FXML private VBox tableau7;

    @FXML private Label lblEstado;
    @FXML private Label lblMovimientos;
    @FXML private Label lblTiempo;

    private SolitaireGame solitaireGame;
    private ArrayList<VBox> tableauxPanes;
    private ArrayList<Pane> foundationPanes;
    private int movimientos = 0;

    @FXML
    private void initialize() {
        solitaireGame = new SolitaireGame();

        tableauxPanes = new ArrayList<>();
        tableauxPanes.add(tableau1);
        tableauxPanes.add(tableau2);
        tableauxPanes.add(tableau3);
        tableauxPanes.add(tableau4);
        tableauxPanes.add(tableau5);
        tableauxPanes.add(tableau6);
        tableauxPanes.add(tableau7);

        foundationPanes = new ArrayList<>();
        foundationPanes.add(foundationSpades);
        foundationPanes.add(foundationHearts);
        foundationPanes.add(foundationDiamonds);
        foundationPanes.add(foundationClubs);

        configurarEventos();

        actualizarInterfaz();
    }

    @FXML
    private void handleNuevoJuego() {
        solitaireGame = new SolitaireGame();
        movimientos = 0;
        actualizarInterfaz();
        lblEstado.setText("Nuevo juego iniciado");
        lblMovimientos.setText("Movimientos: 0");
    }

    @FXML
    private void handleVolverMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/solitaire/bienvenida-solitario.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnMenu.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDrawPileClick() {
        if (solitaireGame.getDrawPile().hayCartas()) {
            solitaireGame.drawCards();
        } else {
            solitaireGame.reloadDrawPile();
        }
        movimientos++;
        actualizarInterfaz();
        lblMovimientos.setText("Movimientos: " + movimientos);
    }

    private void configurarEventos() {
        configurarWastePile();

        for (int i = 0; i < tableauxPanes.size(); i++) {
            configurarTableau(tableauxPanes.get(i), i);
        }

        for (int i = 0; i < foundationPanes.size(); i++) {
            configurarFoundation(foundationPanes.get(i), i);
        }
    }

    private void configurarWastePile() {
        wastePilePane.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                if (solitaireGame.moveWasteToFoundation()) {
                    movimientos++;
                    actualizarInterfaz();
                    lblMovimientos.setText("Movimientos: " + movimientos);
                }
            }
        });
    }

    private void configurarTableau(VBox tableau, int index) {
        tableau.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                if (solitaireGame.moveTableauToFoundation(index + 1)) {
                    movimientos++;
                    actualizarInterfaz();
                    lblMovimientos.setText("Movimientos: " + movimientos);
                }
            }
        });
    }

    private void configurarFoundation(Pane foundation, int index) {
    }

    private void actualizarInterfaz() {
        actualizarDrawPile();

        actualizarWastePile();

        actualizarTableaux();

        actualizarFoundations();

        if (solitaireGame.isGameOver()) {
            lblEstado.setText("Juego Terminado, Ganaste");
        }
    }

    private void actualizarDrawPile() {
        drawPilePane.getChildren().clear();

        if (solitaireGame.getDrawPile().hayCartas()) {
            Label cartaLabel = new Label("🂠");
            cartaLabel.setStyle("-fx-font-size: 48px; -fx-text-fill: blue;");
            cartaLabel.setLayoutX(15);
            cartaLabel.setLayoutY(35);
            drawPilePane.getChildren().add(cartaLabel);
        } else {
            Label recargaLabel = new Label("↻");
            recargaLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: white;");
            recargaLabel.setLayoutX(25);
            recargaLabel.setLayoutY(40);
            drawPilePane.getChildren().add(recargaLabel);
        }
    }

    private void actualizarWastePile() {
        wastePilePane.getChildren().clear();

        CartaInglesa carta = solitaireGame.getWastePile().verCarta();
        if (carta != null) {
            Label cartaLabel = crearLabelCarta(carta);
            cartaLabel.setLayoutX(15);
            cartaLabel.setLayoutY(35);
            wastePilePane.getChildren().add(cartaLabel);
        }
    }

    private void actualizarTableaux() {
        ArrayList<TableauDeck> tableaux = solitaireGame.getTableau();

        for (int i = 0; i < tableaux.size(); i++) {
            VBox tableauPane = tableauxPanes.get(i);
            if (tableauPane.getChildren().size() > 1) {
                tableauPane.getChildren().subList(1, tableauPane.getChildren().size()).clear();
            }

            TableauDeck tableau = tableaux.get(i);
            ArrayList<CartaInglesa> cartas = tableau.getCards();

            for (int j = 0; j < cartas.size(); j++) {
                CartaInglesa carta = cartas.get(j);
                Label cartaLabel;

                if (carta.isFaceup()) {
                    cartaLabel = crearLabelCarta(carta);
                } else {
                    cartaLabel = crearLabelCartaReverso();
                }

                if (j > 0) {
                    cartaLabel.setTranslateY(-15 * j);
                }

                tableauPane.getChildren().add(cartaLabel);
            }
        }
    }

    private void actualizarFoundations() {
        Palo[] palos = Palo.values();

        for (int i = 0; i < palos.length; i++) {
            Pane foundationPane = foundationPanes.get(i);
            foundationPane.getChildren().clear();

        }
    }

    private Label crearLabelCarta(CartaInglesa carta) {
        String simbolo = obtenerSimboloCarta(carta);
        Label label = new Label(simbolo);

        // Estilo según el color de la carta
        if (carta.getColor().equals("rojo")) {
            label.setStyle("-fx-font-size: 16px; -fx-text-fill: red; -fx-background-color: white; " +
                    "-fx-border-color: black; -fx-border-width: 1; -fx-padding: 8 12; " +
                    "-fx-background-radius: 5; -fx-border-radius: 5; " +
                    "-fx-min-width: 60; -fx-min-height: 80; -fx-alignment: center;");
        } else {
            label.setStyle("-fx-font-size: 16px; -fx-text-fill: black; -fx-background-color: white; " +
                    "-fx-border-color: black; -fx-border-width: 1; -fx-padding: 8 12; " +
                    "-fx-background-radius: 5; -fx-border-radius: 5; " +
                    "-fx-min-width: 60; -fx-min-height: 80; -fx-alignment: center;");
        }

        return label;
    }

    private Label crearLabelCartaReverso() {
        Label label = new Label("🂠");
        label.setStyle("-fx-font-size: 48px; -fx-text-fill: darkblue; -fx-background-color: darkblue; " +
                "-fx-border-color: black; -fx-border-width: 1; -fx-padding: 8 12; " +
                "-fx-background-radius: 5; -fx-border-radius: 5; " +
                "-fx-min-width: 60; -fx-min-height: 80; -fx-alignment: center;");
        return label;
    }

    private String obtenerSimboloCarta(CartaInglesa carta) {
        // Convertir carta a representación visual
        String valor;
        switch (carta.getValor()) {
            case 1: valor = "A"; break;
            case 11: valor = "J"; break;
            case 12: valor = "Q"; break;
            case 13: valor = "K"; break;
            default: valor = String.valueOf(carta.getValor());
        }

        String palo;
        switch (carta.getPalo()) {
            case PICA: palo = "♠"; break;
            case CORAZON: palo = "♥"; break;
            case DIAMANTE: palo = "♦"; break;
            case TREBOL: palo = "♣"; break;
            default: palo = "?";
        }

        return valor + palo;
    }
}