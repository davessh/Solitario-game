package DeckOfCards;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Write a description of class Carta here.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
public abstract class Carta implements Comparable<Carta> {
    private int valor;
    protected Palo palo;
    private String color;
    private int valorBajo;
    private boolean faceup;

    /**
     * Crea una carta que no se ve su contenido.
     * @param valor
     * @param palo
     * @param color
     */
    public Carta(int valor, Palo palo, String color) {
        this.valor = valor;
        this.palo = palo;
        this.color = color;
        if (valor == 14) {
            valorBajo = 1;
        } else {
            valorBajo = valor;
        }

        faceup = false;
    }

    /**
     * Voltea una carta para que NO se vea su contenido.
     */
    public void makeFaceDown() {
        faceup = false;
    }
    /**
     * Voltea una carta para que se vea su contenido.
     */
    public void makeFaceUp() {
        faceup = true;
    }
    /**
     * Indica si se ve la cara de la carta
     * @return true si se ve, false si está volteada
     */
    public boolean isFaceup() {
        return faceup;
    }
    public String toString() {
        if (!faceup) {
            return "back";
        }
        return switch (valor) {
            case 14 -> "A" + palo.getCodigo();
            case 11 -> "J"+ palo.getCodigo();
            case 12 -> "Q" + palo.getCodigo();
            case 13 -> "K" + palo.getCodigo();
            case 15-> "Joker";
            default -> valor + palo.getCodigo();
        };
    }

    public boolean tieneElMismoValor(Carta carta) {
        return valor == carta.valor;
    }
    public boolean tieneElMismoPalo(Palo palo) {
        return this.palo.equals(palo);
    }
    public boolean esLaSiguiente(Carta carta) {
        if (valor+1 == carta.valor) {
            return true;
        }
        // verificar si es un As y un 2
        if (valor == 14 && carta.valor == 2) {
            return true;
        }
        return false;
    }

    public ImageView getImageView(){
     String rutaCarta;
     if (!isFaceup()) {
         rutaCarta = "CaraAbajo.png";
     } else {
         rutaCarta = toString() + ".png";
     }
     Image imagen = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cartas/" + rutaCarta)));
     ImageView imageView = new ImageView(imagen);
     imageView.setFitWidth(80);
     imageView.setPreserveRatio(true);
     return imageView;
    }

    public int getValor() {
        return valor;
    }
    public Palo getPalo() {
        return palo;
    }
    public String getColor() {
        return color;
    }
    public int getValorBajo() {
        return valorBajo;
    }
}
