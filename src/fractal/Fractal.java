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

public class Fractal {
    protected static double center_x = 0, center_y = 0, center; // Cambiar el centro
    protected static double zoom = 1; // Cambiar el tamaño
    protected static String name = "mandel", prefix = "",
            pathImg = "src/files/imgs/",
            pathFiles = "src/files/cfg/";
    protected static float max_iter = 34;
    protected static int size = 800;
    protected static boolean autoMaxIte, info, cross, nameSize, nameIter, nameZoom;

    public static void init() {
        try (Scanner sc = new Scanner(new File(pathFiles + "settings.cfg"))) {
            center_x = getScanner(sc).nextFloat();
            center_y = getScanner(sc).nextFloat();
            zoom = getScanner(sc).nextLong();
            max_iter = getScanner(sc).nextInt();
            size = getScanner(sc).nextInt();
            center = size / 2;
            autoMaxIte = getScanner(sc).nextBoolean();
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

    private static void updateMaxIters() {
        max_iter = (int) (
                Math.sqrt(
                        2 * Math.sqrt(
                                Math.abs(1 - Math.sqrt(5 * zoom))
                        )
                ) * 66.5
        );
    }

    public static void moveLeft() {
        center_x += -1 / (zoom * 2);
    }

    public static void moveRight() {
        center_x += 1 / (zoom * 2);
    }

    public static void moveUp() {
        center_y += 1 / (zoom * 2);
    }

    public static void moveDown() {
        center_y += -1 / (zoom * 2);
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
        return Math.pow(x + 1, 2) + Math.pow(y, 2) <= 1 / 16;
    }

    private static float f(int x, int y) {
        float n = 0;
        if (!cardioBulbCheck(x, y)) {
            double k = 4f / zoom;
            double x0 = center_x + scalate(x, k);
            double y0 = -center_y + scalate(y, k);
            Complex c = new Complex(x0, y0);
            Complex z = new Complex();

            while (z.modulus() <= 4 && n < max_iter) {
                z = z.multiply(z).add(c);
                n++;
            }
            if (n < max_iter) {
                float log_zn = (float) Math.log(z.modulus()) / 2;
                float nu = (float) (Math.log(log_zn / Math.log(2)) / Math.log(2));
                n += 1 - nu;
            }
        }
        //return (float) (n +1- (Math.log(Math.log(z.modulus()))) / Math.log(2)) / max_iter;
        return n / (max_iter);
    }

    public static void gen(JProgressBar progressBar) {
        if (autoMaxIte)
            updateMaxIters();
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
                img.setRGB(x, y, getColor(x, y));
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
            float m = f(x, y);
            rgb = new Color(
                    (int) (255 * eq(0.5f, 0.5f, 1f, 0.0f, m)),
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
