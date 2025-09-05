package GUI;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class CartaView {
    @FXML private StackPane raiz;
    @FXML private ImageView imagen;

    private String tipo;
    private String nombre;
    private boolean bocaArriba = false;
    private int ancho = 150, alto = 200;

    private static final Image BASECARTA = new WritableImage(1, 1);

    @FXML
    private void initialize() {
        aplicarTamaño();

        DropShadow normal = new DropShadow();
        normal.setRadius(10); normal.setOffsetY(3);
        normal.setColor(Color.rgb(0,0,0,0.5));
        DropShadow hover = new DropShadow();
        hover.setRadius(16); hover.setOffsetY(6);
        hover.setColor(Color.rgb(0,0,0,1));

        raiz.setEffect(normal);
        raiz.setOnMouseEntered(e -> { raiz.setEffect(hover); raiz.setCursor(javafx.scene.Cursor.HAND); });
        raiz.setOnMouseExited(e -> raiz.setEffect(normal));

        refrescar();
    }

    public void establecerPorNumero(int numero, String tipo) {
        this.tipo = tipo;
        this.nombre = nombreDesdeNumero(numero);
        this.bocaArriba = true;
        refrescar();
    }

    public void establecerBocaArriba(boolean bocaArriba) {
        this.bocaArriba = bocaArriba;
        refrescar();
    }

    private void aplicarTamaño() {
        imagen.setFitWidth(ancho);
        imagen.setFitHeight(alto);
        raiz.setMinSize(ancho, alto);
        raiz.setPrefSize(ancho, alto);
        raiz.setMaxSize(ancho, alto);
    }

    private void refrescar() {
        if (!bocaArriba || tipo == null || nombre == null) {
            imagen.setImage(cargar("/Cartas/Cubierta Carta.png"));
            return;
        }
        String ruta = "/Cartas/" + tipo + "/" + nombre + ".png";
        imagen.setImage(cargar(ruta));
    }

    private Image cargar(String ruta) {
        URL url = getClass().getResource(ruta);
        return (url == null) ? BASECARTA : new Image(url.toExternalForm());
    }

    private String nombreDesdeNumero(int n) {
        switch (n) {
            case 1:  return "As";
            case 2:  return "Dos";
            case 3:  return "Tres";
            case 4:  return "Cuatro";
            case 5:  return "Cinco";
            case 6:  return "Seis";
            case 7:  return "Siete";
            case 8:  return "Ocho";
            case 9:  return "Nueve";
            case 10: return "Diez";
            case 11: return "Joto";
            case 12: return "Queen";
            case 13: return "King";
            default: return "As";
        }
    }

    public void mostrarSlot(boolean on) {
        if (on) {
            imagen.setImage(null);
            raiz.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.10);" +
                            "-fx-border-color: white;" +
                            "-fx-border-width: 2;" +
                            "-fx-background-radius: 14;" +
                            "-fx-border-radius: 14;"
            );
        } else {
            raiz.setStyle("");
            refrescar();
        }
    }

    public void setVisible(boolean visible) {
        raiz.setVisible(visible);
        raiz.setManaged(visible);
    }

    public StackPane getRoot() {
        return raiz;
    }

}
