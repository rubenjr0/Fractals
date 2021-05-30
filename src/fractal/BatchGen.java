package fractal;

public class BatchGen {
    private FractalHandler fh;

    public BatchGen() {
        fh = new FractalHandler();
    }

    private void progression(int frames, int targetZoom) {
        double zoomDelta = Math.pow(10, (float) targetZoom / frames);
        double iterIncrement = 500 * Math.pow(10, targetZoom - 12) / frames; //(52.6229508196722 + 8.045127287716 * Math.log(Math.pow(10, targetZoom))) / frames;
        for (int i = 1; i <= frames; i++) {
            System.out.println(i + " / " + frames);
            fh.setPrefix(i + "");
            fh.f.setMaxIter((int) (fh.f.getMaxIter() + iterIncrement));
            fh.f.setZoom(fh.f.getZoom() / zoomDelta);
            fh.saveImage(fh.generate(null));
        }
    }


    public static void main(String[] args) {
        BatchGen bg = new BatchGen();
        bg.progression(2, 9);
    }
}
