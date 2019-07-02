package fractal;

public class BatchGen {
    private static Fractal mb = new Fractal();
    private static int framesPerZoom = 0;

    public static void main(String[] args) {
        for (int i = 1; i <= 200; i++) {
            Fractal.prefix = Integer.toString(i);
            Fractal.max_iter = i;
            Fractal.gen(null);
        }
    }
}
