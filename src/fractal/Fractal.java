package fractal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.zip.CheckedOutputStream;

public class Fractal {
    protected static double center_x = 0, center_y = 0; // Cambiar el centro
    protected static double zoom = 1; // Cambiar el tamaño
    protected static String name = "mandel", prefix = "",
            pathImg = "src/files/imgs/",
            pathFiles = "src/files/cfg/";
    protected static float max_iter = 18;
    protected static int size, center;
    protected static boolean info, cross, nameSize, nameIter, nameZoom;

    public static void init() {
        try (Scanner sc = new Scanner(new File(pathFiles + "settings.cfg"))) {
            center_x = getScanner(sc).nextFloat();
            center_y = getScanner(sc).nextFloat();
            zoom = getScanner(sc).nextLong();
            max_iter = getScanner(sc).nextInt();
            size = getScanner(sc).nextInt();
            center = size / 2;
            info = getScanner(sc).nextBoolean();
            cross = getScanner(sc).nextBoolean();
            nameSize = getScanner(sc).nextBoolean();
            nameIter = getScanner(sc).nextBoolean();
            nameZoom = getScanner(sc).nextBoolean();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Scanner getScanner(Scanner sc) {
        Scanner bit = new Scanner(sc.nextLine());
        bit.next();
        return bit;
    }

    public static void saveSettings() throws FileNotFoundException {
        try (
                PrintWriter pw = new PrintWriter(
                        pathFiles + "SAVE_1E" + Math.log10(zoom) + "_" + max_iter + "_.txt"
                )
        ) {
            pw.println("Center r: " + center_x);
            pw.println("Center i: " + center_y);
            pw.println("Zoom: " + zoom);
            pw.println("Max iters: " + max_iter);
            pw.println("Size: " + size);
        }
    }

    public static void moveX(int dir) {
        center_x += dir / (zoom * 2);
    }

    public static void moveY(int dir) {
        center_y += dir / (zoom * 2);
    }

    public static void zoomIn(double k) {
        zoom *= k;
    }

    public static void zoomOut(double k) {
        zoom /= k;
    }

    private static double scalate(float point, double k) {
        return (point / size - 0.5f) * k;
    }

    private static boolean cardioBulbCheck(int x, int y) {
        return Math.pow(x + 1, 2) + Math.pow(y, 2) <= 1f / 16f;
    }

    private static double f(int x, int y) {
        float n = 0;
        if (!cardioBulbCheck(x, y)) {
            double k = 4f / zoom;
            double x0 = center_x + scalate(x, k);
            double y0 = -center_y + scalate(y, k);
            double zr = 0, zi = 0;
            double zrsqr = zr * zr;
            double zisqr = zi * zi;
            while ((zrsqr + zisqr <= 16) && (n < max_iter)) {
                double zitemp = zr * zi;
                zitemp += zitemp;
                zitemp += y0;
                double zrtemp = zrsqr - zisqr + x0;
                if (zr == zrtemp && zi == zitemp) {
                    n = max_iter;
                    break;
                }
                zr = zrtemp;
                zi = zitemp;
                zrsqr = zr * zr;
                zisqr = zi * zi;
                n++;
            }
            if (n < max_iter) {
                float log_zn = (float) Math.log(zrsqr + zisqr) / 30;
                float nu = (float) (Math.log(log_zn / Math.log(2)) / Math.log(2));
                n += 1 - nu;
            }
        }
        return n / (max_iter);
    }

    public static void gen(JProgressBar progressBar) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        File file = new File(genName());
        for (int y = 0; y < size; y++) {
            if (info) {
                double progress = (double) y / size * 100;
                if (progressBar != null)
                    progressBar.setValue((int) progress);
                else
                    System.out.println(progress);
            }
            for (int x = 0; x < size; x++) {

                int color = getColor(x, y);
                img.setRGB(x, y, color);
            }
        }
        if (progressBar != null)
            progressBar.setValue(0);
        writeImage(img, file);
    }

    private static String genName() {
        return pathImg + prefix + name +
                (nameSize ? ", s=" + size : "") +
                (nameIter ? ", n=" + max_iter : "") +
                (nameZoom ? ", z=" + "1E" + Math.log10(zoom) : "") +
                ".png";
    }

    private static double eq(float a, float b, float c, float d, double k) {
        return (a + b * Math.cos(2 * Math.PI * (c * k + d)));
    }

    private static int getColor(int x, int y) {
        int rgb;
        if (cross && (y == center || x == center)) {
            rgb = new Color(255, 0, 0, 1).getRGB();
        } else {
            double m = f(x, y); //Preset M: -0.5*f(x, y);
            rgb = new Color(
                    (int) (255 * eq(0.5f, 0.5f, 1f, 0f, m)),
                    (int) (255 * eq(0.5f, 0.5f, 1f, 0.1f, m)),
                    (int) (255 * eq(0.5f, 0.5f, 1f, 0.2f, m)),
                    1
            ).getRGB();
        }
        return rgb;
    }

    private static void writeImage(BufferedImage img, File file) {
        try {
            ImageIO.write(img, "png", file);
        } catch (IOException ioe) {
            ioe.getStackTrace();
        }
    }
}
