package interfaz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import solitaire.SolitaireGame;
import solitaire.TableauDeck;
import solitaire.FoundationDeck;
import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;

import java.util.ArrayList;

public class SolitaireJuegoController {

    // Controles del header
    @FXML private Button btnNuevoJuego;
    @FXML private Button btnMenu;

    // Draw pile y Waste pile
    @FXML private Pane drawPilePane;
    @FXML private Pane wastePilePane;

    // Foundations (4 pilas por palo)
    @FXML private Pane foundationSpades;
    @FXML private Pane foundationHearts;
    @FXML private Pane foundationDiamonds;
    @FXML private Pane foundationClubs;

    // Tableaux (7 columnas) - Cambiado a Pane para coincidir con el FXML
    @FXML private Pane tableau1;
    @FXML private Pane tableau2;
    @FXML private Pane tableau3;
    @FXML private Pane tableau4;
    @FXML private Pane tableau5;
    @FXML private Pane tableau6;
    @FXML private Pane tableau7;

    // Labels de información
    @FXML private Label lblEstado;
    @FXML private Label lblMovimientos;
    @FXML private Label lblTiempo;

    // Lógica del juego
    private SolitaireGame solitaireGame;
    private ArrayList<Pane> tableauxPanes;
    private ArrayList<Pane> foundationPanes;
    private int movimientos = 0;

    @FXML
    private void initialize() {
        // Inicializar el juego
        solitaireGame = new SolitaireGame();

        // Crear listas para manejo fácil de los contenedores
        tableauxPanes = new ArrayList<>();
        tableauxPanes.add(tableau1);
        tableauxPanes.add(tableau2);
        tableauxPanes.add(tableau3);
        tableauxPanes.add(tableau4);
        tableauxPanes.add(tableau5);
        tableauxPanes.add(tableau6);
        tableauxPanes.add(tableau7);

        foundationPanes = new ArrayList<>();
        foundationPanes.add(foundationClubs);    // TREBOL es ordinal 0
        foundationPanes.add(foundationDiamonds); // DIAMANTE es ordinal 1
        foundationPanes.add(foundationHearts);   // CORAZON es ordinal 2
        foundationPanes.add(foundationSpades);   // PICA es ordinal 3

        // Configurar eventos de arrastrar y soltar (drag and drop)
        configurarEventos();

        // Actualizar la interfaz
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/bienvenida-solitario.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnMenu.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Solitaire - Bienvenida");
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
        // Configurar waste pile para permitir arrastrar cartas
        configurarWastePile();

        // Configurar tableaux para permitir drag and drop
        for (int i = 0; i < tableauxPanes.size(); i++) {
            configurarTableau(tableauxPanes.get(i), i);
        }

        // Configurar foundations para permitir drop
        for (int i = 0; i < foundationPanes.size(); i++) {
            configurarFoundation(foundationPanes.get(i), i);
        }
    }

    private void configurarWastePile() {
        // Configurar eventos para el waste pile
        wastePilePane.setOnMouseClicked(e -> {
            // Manejar doble click para mover automáticamente a foundation
            if (e.getClickCount() == 2) {
                if (solitaireGame.moveWasteToFoundation()) {
                    movimientos++;
                    actualizarInterfaz();
                    lblMovimientos.setText("Movimientos: " + movimientos);
                }
            }
        });
    }

    private void configurarTableau(Pane tableau, int index) {
        // Configurar eventos para cada tableau
        tableau.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                // Doble click para mover a foundation
                if (solitaireGame.moveTableauToFoundation(index + 1)) {
                    movimientos++;
                    actualizarInterfaz();
                    lblMovimientos.setText("Movimientos: " + movimientos);
                }
            }
        });
    }

    private void configurarFoundation(Pane foundation, int index) {
        // Configurar eventos para las foundations
        // Principalmente para recibir cartas arrastradas
    }

    private void actualizarInterfaz() {
        // Actualizar draw pile
        actualizarDrawPile();

        // Actualizar waste pile
        actualizarWastePile();

        // Actualizar tableaux
        actualizarTableaux();

        // Actualizar foundations
        actualizarFoundations();

        // Verificar si el juego terminó
        if (solitaireGame.isGameOver()) {
            lblEstado.setText("¡Felicitaciones! ¡Ganaste!");
        }
    }

    private void actualizarDrawPile() {
        // Limpiar contenido previo
        drawPilePane.getChildren().clear();

        if (solitaireGame.getDrawPile().hayCartas()) {
            // Mostrar reverso de carta
            Label cartaLabel = new Label("🂠");
            cartaLabel.setStyle("-fx-font-size: 48px; -fx-text-fill: blue;");
            cartaLabel.setLayoutX(15);
            cartaLabel.setLayoutY(35);
            drawPilePane.getChildren().add(cartaLabel);
        } else {
            // Mostrar que está vacío con símbolo de recarga
            Label recargaLabel = new Label("↻");
            recargaLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: white;");
            recargaLabel.setLayoutX(25);
            recargaLabel.setLayoutY(40);
            drawPilePane.getChildren().add(recargaLabel);
        }
    }

    private void actualizarWastePile() {
        // Limpiar contenido previo
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
            Pane tableauPane = tableauxPanes.get(i);
            // Limpiar contenido previo
            tableauPane.getChildren().clear();

            TableauDeck tableau = tableaux.get(i);
            ArrayList<CartaInglesa> cartas = tableau.getCards();

            // Configuración para el apilamiento de cartas
            double yOffset = 0; // Posición Y inicial
            double cardSpacing = 25; // Espaciado entre cartas (solapamiento)
            double xCentered = 10; // Centrar horizontalmente las cartas

            for (int j = 0; j < cartas.size(); j++) {
                CartaInglesa carta = cartas.get(j);
                Label cartaLabel;

                if (carta.isFaceup()) {
                    cartaLabel = crearLabelCarta(carta);
                } else {
                    cartaLabel = crearLabelCartaReverso();
                }

                // Posicionar la carta en el tableau
                cartaLabel.setLayoutX(xCentered);
                cartaLabel.setLayoutY(yOffset);

                // Incrementar la posición Y para la siguiente carta (efecto apilado)
                yOffset += cardSpacing;

                // Configurar evento para cada carta individual
                configurarEventosCarta(cartaLabel, carta, i, j);

                tableauPane.getChildren().add(cartaLabel);
            }

            // Si el tableau está vacío, mostrar zona de drop visual
            if (cartas.isEmpty()) {
                Label placeholderLabel = new Label("K");
                placeholderLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: rgba(255,255,255,0.3); " +
                        "-fx-background-color: transparent; -fx-padding: 20;");
                placeholderLabel.setLayoutX(25);
                placeholderLabel.setLayoutY(30);
                tableauPane.getChildren().add(placeholderLabel);
            }
        }
    }

    private void configurarEventosCarta(Label cartaLabel, CartaInglesa carta, int tableauIndex, int cardIndex) {
        // Configurar eventos específicos para cada carta
        cartaLabel.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && carta.isFaceup()) {
                // Doble click en carta individual para mover a foundation
                if (solitaireGame.moveTableauToFoundation(tableauIndex + 1)) {
                    movimientos++;
                    actualizarInterfaz();
                    lblMovimientos.setText("Movimientos: " + movimientos);
                }
            }
        });

        // Aquí puedes agregar drag and drop para cartas individuales
        cartaLabel.setOnMouseEntered(e -> {
            if (carta.isFaceup()) {
                cartaLabel.setStyle(cartaLabel.getStyle() + "-fx-effect: dropshadow(gaussian, yellow, 10, 0.5, 0, 0);");
            }
        });

        cartaLabel.setOnMouseExited(e -> {
            // Remover el efecto hover
            String style = cartaLabel.getStyle().replace("-fx-effect: dropshadow(gaussian, yellow, 10, 0.5, 0, 0);", "");
            cartaLabel.setStyle(style);
        });
    }

    private void actualizarFoundations() {
        ArrayList<FoundationDeck> foundations = solitaireGame.getFoundations();

        for (int i = 0; i < foundations.size(); i++) {
            Pane foundationPane = foundationPanes.get(i);
            foundationPane.getChildren().clear();

            FoundationDeck foundation = foundations.get(i);
            CartaInglesa ultimaCarta = foundation.getUltimaCarta();

            if (ultimaCarta != null) {
                Label cartaLabel = crearLabelCarta(ultimaCarta);
                cartaLabel.setLayoutX(15);
                cartaLabel.setLayoutY(35);
                foundationPane.getChildren().add(cartaLabel);
            }
        }
    }

    private Label crearLabelCarta(CartaInglesa carta) {
        String simbolo = obtenerSimboloCarta(carta);
        Label label = new Label(simbolo);

        // Estilo según el color de la carta
        if (carta.getColor().equals("rojo")) {  // Tu enum usa "rojo" en minúscula
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
            case 14: valor = "A"; break; // As es 14 en tu sistema
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