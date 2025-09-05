package interfaz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import solitaire.SolitaireGame;
import solitaire.TableauDeck;
import solitaire.FoundationDeck;
import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.effect.Glow;

import java.util.ArrayList;

public class SolitaireJuegoController {

    @FXML
    private Button btnNuevoJuego;
    @FXML
    private Button btnMenu;
    @FXML
    private Pane drawPilePane;
    @FXML
    private Pane wastePilePane;

    @FXML
    private Pane foundationSpades;
    @FXML
    private Pane foundationHearts;
    @FXML
    private Pane foundationDiamonds;
    @FXML
    private Pane foundationClubs;
    @FXML
    private Pane tableau1;
    @FXML
    private Pane tableau2;
    @FXML
    private Pane tableau3;
    @FXML
    private Pane tableau4;
    @FXML
    private Pane tableau5;
    @FXML
    private Pane tableau6;
    @FXML
    private Pane tableau7;
    @FXML
    private Label lblEstado;
    @FXML
    private Label lblMovimientos;
    @FXML
    private Label lblTiempo;
    private SolitaireGame solitaireGame;
    private ArrayList<Pane> tableauxPanes;
    private ArrayList<Pane> foundationPanes;
    private int movimientos = 0;
    private Timeline timer;
    private int segundos = 0;
    //drag and drop
    private Label cartaArrastrada = null;
    private int sourceTableauIndex = -1;
    private boolean isFromWaste = false;

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
        foundationPanes.add(foundationClubs);    // TREBOL es ordinal 0
        foundationPanes.add(foundationDiamonds); // DIAMANTE es ordinal 1
        foundationPanes.add(foundationHearts);   // CORAZON es ordinal 2
        foundationPanes.add(foundationSpades);   // PICA es ordinal 3

        configurarEventos();

        inicializarTimer();

        actualizarInterfaz();
    }

    private void inicializarTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            segundos++;
            int minutos = segundos / 60;
            int segs = segundos % 60;
            lblTiempo.setText(String.format("Tiempo: %02d:%02d", minutos, segs));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    @FXML
    private void handleNuevoJuego() {
        solitaireGame = new SolitaireGame();
        movimientos = 0;
        segundos = 0;
        if (timer != null) {
            timer.stop();
        }
        inicializarTimer();
        actualizarInterfaz();
        lblEstado.setText("Nuevo juego iniciado");
        lblMovimientos.setText("Movimientos: 0");
    }

    @FXML
    private void handleVolverMenu() {
        if (timer != null) {
            timer.stop();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/bienvenida-solitario.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnMenu.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Solitaire - Bienvenida");
            stage.setResizable(true);
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
        // ELIMINAR completamente el doble-click, solo drag

        // Configurar drag desde waste pile
        wastePilePane.setOnDragDetected(e -> {
            CartaInglesa carta = solitaireGame.getWastePile().verCarta();
            if (carta != null) {
                Dragboard db = wastePilePane.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString("waste");
                db.setContent(content);
                isFromWaste = true;
                e.consume();
            }
        });
    }

    private void configurarTableau(Pane tableau, int index) {
        tableau.setOnDragOver(e -> {
            if (e.getGestureSource() != tableau && e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });

        tableau.setOnDragEntered(e -> {
            if (e.getGestureSource() != tableau && e.getDragboard().hasString()) {
                tableau.setEffect(new Glow(0.5));
            }
            e.consume();
        });

        tableau.setOnDragExited(e -> {
            tableau.setEffect(null);
            e.consume();
        });

        tableau.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String source = db.getString();
                if (source.equals("waste")) {
                    success = solitaireGame.moveWasteToTableau(index + 1);
                    isFromWaste = false;
                } else if (source.startsWith("tableau")) {
                    int sourceIndex = Integer.parseInt(source.split("-")[1]);
                    success = solitaireGame.moveTableauToTableau(sourceIndex + 1, index + 1);
                }

                if (success) {
                    movimientos++;
                    actualizarInterfaz();
                    lblMovimientos.setText("Movimientos: " + movimientos);
                }
            }

            tableau.setEffect(null);
            e.setDropCompleted(success);
            e.consume();
        });
    }

    private void configurarFoundation(Pane foundation, int index) {
        foundation.setOnDragOver(e -> {
            if (e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });

        foundation.setOnDragEntered(e -> {
            if (e.getDragboard().hasString()) {
                Glow glow = new Glow(0.8);
                foundation.setEffect(glow);
                String currentStyle = foundation.getStyle();
                foundation.setStyle(currentStyle + "-fx-border-color: #FFD700; -fx-border-width: 4;");
            }
            e.consume();
        });

        foundation.setOnDragExited(e -> {
            foundation.setEffect(null);
            // Restaurar estilo original
            String currentStyle = foundation.getStyle();
            foundation.setStyle(currentStyle.replaceAll("-fx-border-color: #FFD700; -fx-border-width: 4;", ""));
            e.consume();
        });

        foundation.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String source = db.getString();
                if (source.equals("waste")) {
                    success = solitaireGame.moveWasteToFoundation();
                    isFromWaste = false;
                } else if (source.startsWith("tableau")) {
                    int sourceIndex = Integer.parseInt(source.split("-")[1]);
                    success = solitaireGame.moveTableauToFoundation(sourceIndex + 1);
                }

                if (success) {
                    movimientos++;
                    actualizarInterfaz();
                    lblMovimientos.setText("Movimientos: " + movimientos);
                    verificarVictoria();
                }
            }

            foundation.setEffect(null);
            // Restaurar estilo original
            String currentStyle = foundation.getStyle();
            foundation.setStyle(currentStyle.replaceAll("-fx-border-color: #FFD700; -fx-border-width: 4;", ""));
            e.setDropCompleted(success);
            e.consume();
        });
    }

    private void actualizarInterfaz() {
        actualizarDrawPile();
        actualizarWastePile();
        actualizarTableaux();
        actualizarFoundations();
    }

    private void verificarVictoria() {
        if (solitaireGame.isGameOver()) {
            if (timer != null) {
                timer.stop();
            }
            lblEstado.setText("GANASTE, JUEGO TERMINADO");
            lblEstado.setStyle(lblEstado.getStyle() + "-fx-text-fill: #00FF00;");
        }
    }

    private void actualizarDrawPile() {
        drawPilePane.getChildren().clear();

        if (solitaireGame.getDrawPile().hayCartas()) {
            Label cartaLabel = new Label("🂠");
            cartaLabel.setStyle("-fx-font-size: 120px; -fx-text-fill: #4169E1; -fx-alignment: center;"); // Aumenta el tamaño aquí
            cartaLabel.setLayoutX(10); // ajustar x position
            cartaLabel.setLayoutY(-15); // ajustar y position
            drawPilePane.getChildren().add(cartaLabel);
        } else {
            Label recargaLabel = new Label("↻");
            recargaLabel.setStyle("-fx-font-size: 60px; -fx-text-fill: #FFD700; -fx-alignment: center; -fx-font-weight: bold;");
            recargaLabel.setLayoutX(25);
            recargaLabel.setLayoutY(35);
            drawPilePane.getChildren().add(recargaLabel);
        }
    }

    private void actualizarWastePile() {
        wastePilePane.getChildren().clear();

        CartaInglesa carta = solitaireGame.getWastePile().verCarta();
        if (carta != null) {
            Label cartaLabel = crearLabelCarta(carta);
            cartaLabel.setLayoutX(10);
            cartaLabel.setLayoutY(10);
            wastePilePane.getChildren().add(cartaLabel);
        }
    }

    private void actualizarTableaux() {
        ArrayList<TableauDeck> tableaux = solitaireGame.getTableau();

        for (int i = 0; i < tableaux.size(); i++) {
            Pane tableauPane = tableauxPanes.get(i);
            tableauPane.getChildren().clear();

            TableauDeck tableau = tableaux.get(i);
            ArrayList<CartaInglesa> cartas = tableau.getCards();

            double yOffset = 10;
            double cardSpacing = 25;
            double xCentered = 10;

            for (int j = 0; j < cartas.size(); j++) {
                CartaInglesa carta = cartas.get(j);
                Label cartaLabel;

                if (carta.isFaceup()) {
                    cartaLabel = crearLabelCarta(carta);
                } else {
                    cartaLabel = crearLabelCartaReverso();
                }

                cartaLabel.setLayoutX(xCentered);
                cartaLabel.setLayoutY(yOffset);

                yOffset += cardSpacing;

                if (j == cartas.size() - 1 && carta.isFaceup()) {
                    configurarDragCarta(cartaLabel, i);
                }

                configurarEventosCarta(cartaLabel, carta, i, j);
                tableauPane.getChildren().add(cartaLabel);
            }

            if (cartas.isEmpty()) {
                Label placeholderLabel = new Label("K");
                placeholderLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: rgba(255,255,255,0.3); " +
                        "-fx-background-color: transparent; -fx-padding: 20;");
                placeholderLabel.setLayoutX(30);
                placeholderLabel.setLayoutY(50);
                tableauPane.getChildren().add(placeholderLabel);
            }
        }
    }

    private void configurarDragCarta(Label cartaLabel, int tableauIndex) {
        cartaLabel.setOnDragDetected(e -> {
            Dragboard db = cartaLabel.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("tableau-" + tableauIndex);
            db.setContent(content);
            sourceTableauIndex = tableauIndex;
            e.consume();
        });
    }

    private void configurarEventosCarta(Label cartaLabel, CartaInglesa carta, int tableauIndex, int cardIndex) {
        // SOLO efectos visuales, SIN doble-click
        cartaLabel.setOnMouseEntered(e -> {
            TableauDeck tableau = solitaireGame.getTableau().get(tableauIndex);
            ArrayList<CartaInglesa> cartas = tableau.getCards();
            boolean esCartaSuperior = (cardIndex == cartas.size() - 1);

            if (carta.isFaceup() && esCartaSuperior) {
                cartaLabel.setEffect(new Glow(0.3));
            }
        });

        cartaLabel.setOnMouseExited(e -> {
            cartaLabel.setEffect(null);
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
                cartaLabel.setLayoutX(10);
                cartaLabel.setLayoutY(10);
                foundationPane.getChildren().add(cartaLabel);
            }
        }
    }

    private Label crearLabelCarta(CartaInglesa carta) {
        String valor;
        switch (carta.getValor()) {
            case 14:
                valor = "A";
                break;
            case 11:
                valor = "J";
                break;
            case 12:
                valor = "Q";
                break;
            case 13:
                valor = "K";
                break;
            default:
                valor = String.valueOf(carta.getValor());
        }

        String palo;
        switch (carta.getPalo()) {
            case PICA:
                palo = "♠";
                break;
            case CORAZON:
                palo = "♥";
                break;
            case DIAMANTE:
                palo = "♦";
                break;
            case TREBOL:
                palo = "♣";
                break;
            default:
                palo = "?";
        }

        // StackPane control de layout
        javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane();
        stackPane.setMinSize(80, 110);
        stackPane.setMaxSize(80, 110);
        stackPane.setPrefSize(80, 110);

        //Carta grafica (fondo)
        String backgroundColor = carta.getColor().equals("rojo") ? "white" : "white";
        String textColor = carta.getColor().equals("rojo") ? "#DC143C" : "#000000";

        stackPane.setStyle("-fx-background-color: " + backgroundColor + "; " +
                "-fx-border-color: #333333; -fx-border-width: 2; " +
                "-fx-background-radius: 8; -fx-border-radius: 8; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0.5, 1, 1);");

        Label valorSuperior = new Label(valor);
        valorSuperior.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + textColor + ";");
        javafx.scene.layout.StackPane.setAlignment(valorSuperior, javafx.geometry.Pos.TOP_RIGHT);
        javafx.geometry.Insets marginSuperior = new javafx.geometry.Insets(5, 5, 0, 0);
        javafx.scene.layout.StackPane.setMargin(valorSuperior, marginSuperior);

        Label paloCentro = new Label(palo);
        paloCentro.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + textColor + ";");
        javafx.scene.layout.StackPane.setAlignment(paloCentro, javafx.geometry.Pos.CENTER);

        Label valorInferior = new Label(valor);
        valorInferior.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + textColor + "; -fx-rotate: 180;");
        javafx.scene.layout.StackPane.setAlignment(valorInferior, javafx.geometry.Pos.BOTTOM_LEFT);
        javafx.geometry.Insets marginInferior = new javafx.geometry.Insets(0, 0, 5, 5);
        javafx.scene.layout.StackPane.setMargin(valorInferior, marginInferior);

        stackPane.getChildren().addAll(valorSuperior, paloCentro, valorInferior);

        Label labelContenedor = new Label();
        labelContenedor.setGraphic(stackPane);
        labelContenedor.setStyle("-fx-padding: 0;");

        return labelContenedor;
    }

    private Label crearLabelCartaReverso() {
        Label label = new Label("🂠");
        label.setStyle("-fx-font-size: 48px; -fx-text-fill: #4169E1; -fx-background-color: #4169E1; " +  // Aumentado de 40px a 48px
                "-fx-border-color: #000080; -fx-border-width: 2; -fx-padding: 8 12; " +  // Aumentado padding
                "-fx-background-radius: 10; -fx-border-radius: 10; " +
                "-fx-min-width: 80; -fx-min-height: 110; -fx-alignment: center; " +  // Aumentado tamaño
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0.5, 1, 1);");
        return label;
    }
}
