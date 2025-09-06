package GUI;

// Librerías necesarias para la clase
import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

// Se declara la clase CartaView que representa visualmente una carta
public class CartaView {

    // Se declaran los atributos pertenecientes a la clase
    @FXML private StackPane raiz; // Contenedor principal de la carta
    @FXML private ImageView imagen; // Imagen que muestra la carta

    private String tipo; // Palo de la carta
    private String nombre; // Valor de la carta
    private boolean bocaArriba = false; // Indica si la carta está visible
    private int ancho = 150, alto = 200; // Dimensiones de la carta

    private static final Image BASECARTA = new WritableImage(1, 1); // Imagen base vacía

    // Inicializa la carta aplicando tamaño, sombras y preparando la imagen inicial
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

    // Asigna un número y tipo a la carta, la voltea boca arriba y actualiza la imagen
    public void establecerPorNumero(int numero, String tipo) {
        this.tipo = tipo;
        this.nombre = nombreDesdeNumero(numero);
        this.bocaArriba = true;
        refrescar();
    }

    // Voltea la carta según el valor recibido (mostrar cara o cubierta)
    public void establecerBocaArriba(boolean bocaArriba) {
        this.bocaArriba = bocaArriba;
        refrescar();
    }

    // Ajusta el tamaño de la carta y de su contenedor
    private void aplicarTamaño() {
        imagen.setFitWidth(ancho);
        imagen.setFitHeight(alto);
        raiz.setMinSize(ancho, alto);
        raiz.setPrefSize(ancho, alto);
        raiz.setMaxSize(ancho, alto);
    }

    // Actualiza la carta: si está volteada muestra la cubierta, si no, la imagen real
    private void refrescar() {
        if (!bocaArriba || tipo == null || nombre == null) {
            imagen.setImage(cargar("/Cartas/Cubierta Carta.png"));
            return;
        }
        String ruta = "/Cartas/" + tipo + "/" + nombre + ".png";
        imagen.setImage(cargar(ruta));
    }

    // Carga una imagen desde la ruta indicada
    private Image cargar(String ruta) {
        URL url = getClass().getResource(ruta);
        return (url == null) ? BASECARTA : new Image(url.toExternalForm());
    }

    // Traduce un número al nombre de la carta correspondiente
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

    // Muestra un slot vacío con borde o restaura la carta normal
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

    // Controla la visibilidad de la carta en la interfaz
    public void setVisible(boolean visible) {
        raiz.setVisible(visible);
        raiz.setManaged(visible);
    }

    // Devuelve el contenedor raíz de la carta para integrarlo en otros layouts
    public StackPane getRoot() {
        return raiz;
    }

}
