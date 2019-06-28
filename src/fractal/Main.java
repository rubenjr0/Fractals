package fractal;

public class Main {
    public static void main(String[] args) {
        Fractal mb = new Fractal();
        mb.setNameZoom(false);
        mb.autoMaxIte = true;
        mb.setPrefix("GIF ");
        for (int i = 0; i <= (12*60); i++) {
            mb.setPrefix("60Gif" + i);
            mb.setZoom(Math.pow(10, (double)i/60));
            mb.gen(null);
        }
    }
}
