package GUI;

// Clase que guarda la info de una carta cuando se arrastra en la interfaz
public class DragInfo {
    // Enum que dice si la carta viene del tablero o del descarte
    public enum Origen { TABLERO, DESCARTE }

    // De dónde salió la carta
    public Origen origen;
    public int col;     // Columna desde la que se tomó
    public int valor;     // Valor de la carta (1 a 13)
    public boolean esTope;     // Marca si la carta es la que está arriba del montón

    // Encode convierte los datos en un texto para poder pasarlos fácilmente a las validaciones
    public static String encode(DragInfo d) {
        return d.origen + ";" + d.col + ";" + d.valor + ";" + (d.esTope ? "1" : "0");
    }

    // Este metodo toma el texto y lo vuelve a convertir en un objeto DragInfo
    public static DragInfo decode(String s) {
        String[] p = s.split(";");
        DragInfo d = new DragInfo();
        d.origen = Origen.valueOf(p[0]);
        d.col = Integer.parseInt(p[1]);
        d.valor = Integer.parseInt(p[2]);
        d.esTope = "1".equals(p[3]);
        return d;
    }
}
