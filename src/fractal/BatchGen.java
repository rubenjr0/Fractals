package fractal;

public class BatchGen {
    private static Fractal mb = new Fractal();
    private static int framesPerZoom = 0;

    public static void main(String[] args) {
        // mb.setNameZoom(true);
        // mb.autoMaxIte = true;
        for (int i = 0; i <= (12*framesPerZoom); i++) {
            // mb.setPrefix("jul" + i);
            // mb.setZoom(Math.pow(10, (double)i/60));
            mb.gen(null);
        }
    }
}
