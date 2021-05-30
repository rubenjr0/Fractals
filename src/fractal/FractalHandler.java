package fractal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Scanner;

public class FractalHandler {
    protected Fractal f;
    private String pref;

    public FractalHandler() {
        f = new Fractal();
        pref = "";
        loadSettings();
    }

    public void reset() {
        f.setCX(0);
        f.setCY(0);
        f.setZoom(1);
        f.setMaxIter(40);
    }

    public void setPrefix(String p) {
        pref = p;
    }

    private Scanner getFragmentScanner(Scanner sc) {
        String line = sc.nextLine();
        Scanner fragment = new Scanner(line).useDelimiter(":");
        fragment.next();
        return new Scanner(fragment.next()).useLocale(Locale.US);
    }

    private void loadSettings() {
        try (Scanner sc = new Scanner(new File("settings.cfg"))) {
            f.setCX(getFragmentScanner(sc).nextFloat());
            f.setCY(getFragmentScanner(sc).nextFloat());
            f.setZoom(getFragmentScanner(sc).nextFloat());
            f.setMaxIter(getFragmentScanner(sc).nextInt());
            f.setSize(getFragmentScanner(sc).nextInt());
            f.setCross(getFragmentScanner(sc).nextBoolean());
        } catch (FileNotFoundException e) {
            saveSettings(true);
            loadSettings();
        }
    }

    public void saveSettings(boolean saveDefault) {
        String name = saveDefault ? "settings.cfg" : ("SAVE_1E" + Math.log10(f.getZoom()) + "_" + f.getMaxIter() + "_.cfg");
        try (
                PrintWriter pw = new PrintWriter(new File(name))
        ) {
            pw.println("Center r: " + (saveDefault ? 0 : f.getCX()));
            pw.println("Center i: " + (saveDefault ? 0 : f.getCY()));
            pw.println("Zoom: " + (saveDefault ? 1 : f.getZoom()));
            pw.println("Max iters: " + (saveDefault ? 40 : f.getMaxIter()));
            pw.println("Size: " + (saveDefault ? 800 : f.getSize()));
            pw.println("cross: " + f.getCross());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage generate(JProgressBar pb) {
        return f.gen(pb);
    }

    public void saveImage(BufferedImage img) {
        File file = new File(pref + (pref.equals("") ? "" : "_") + "mandelbrot" + (f.getCross() ? "_aim" : "") + ".png");
        try {
            ImageIO.write(img, "png", file);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
