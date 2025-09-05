package GUI;

public class DragInfo {
    public enum Origen { TABLERO, DESCARTE }

    public Origen origen;
    public int col;
    public int valor;
    public boolean esTope;

    public static String encode(DragInfo d) {
        return d.origen + ";" + d.col + ";" + d.valor + ";" + (d.esTope ? "1" : "0");
    }

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
