package fractal;

public class BatchGen {

    private static void progression(int frames, float targetZoom, boolean iterP, boolean zoomIP, boolean zoomOP) {
        double zoomDelta = Math.pow(10, targetZoom / frames);
        double iterIncrement = (4 * Math.pow(10, 2)) / frames;
        for (int i = 1; i <= frames; i++) {
            System.out.println(i + " / " + frames);
            Fractal.prefix = i + "-";
            if (iterP)
                Fractal.max_iter += iterIncrement;
            if (zoomIP)
                Fractal.zoomIn(zoomDelta);
            else if (zoomOP)
                Fractal.zoomOut(zoomDelta);
            Fractal.gen(null);
        }
    }


    public static void main(String[] args) {
        Fractal.init();
        progression(900, 10, true, true, false);
    }
}
