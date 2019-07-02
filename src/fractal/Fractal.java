package fractal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Fractal {
    protected static float center_x = 0, center_y = 0, center; // Cambiar el centro
    protected static float zoom = 5; // Cambiar el tamaño
    protected static String name = "mandel", prefix = "",
            pathImg = "D:\\CODE\\Java\\Fractals\\src\\files\\imgs\\",
            pathStg = "D:\\CODE\\Java\\Fractals\\src\\files\\cfg\\",
            pathSave = "D:\\CODE\\Java\\Fractals\\src\\files\\cfg\\";
    protected static int max_iter = 34;
    protected static int size = 800;
    protected static boolean autoMaxIte, info, cross, nameSize, nameIter, nameZoom;

    public static void init() {
        try (Scanner sc = new Scanner(new File(pathStg + "settings.cfg"))) {
            center_x = getScanner(sc).nextFloat();
            center_y = getScanner(sc).nextFloat();
            zoom = getScanner(sc).nextFloat();
            max_iter = getScanner(sc).nextInt();
            size = getScanner(sc).nextInt();
            center = size/2;
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
                        pathSave + "SAVE_1E" + Math.log10(zoom) + "_" + max_iter + "_.txt"
                )
        ) {
            pw.println("Center x: " + center_x);
            pw.println("Center y: " + center_y);
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

    public static void moveX(float dir) {
        center_x += dir / (zoom * 2);
    }

    public static void moveY(float dir) {
        center_y += dir / (zoom * 2);
    }

    private static float f(int x, int y) {
        float offset_x = center_x + ((float) x / size - 0.5f) * (4 / zoom);
        float offset_y = -center_y + ((float) y / size - 0.5f) * (4 / zoom);
        Complex c = new Complex(offset_x, offset_y);
        Complex z = new Complex();
        int n = 0;
        while (z.modulus() <= 2 && n < max_iter) {
            z = z.pow(2).add(c);
            n++;
        }
        // return (float) (n - Math.log(Math.log(z.modulus()) / Math.log(2)));
        return n == max_iter ? 0 : n;
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

    private static int getColor(int x, int y) {
        int rgb;
        if (cross && (y == center || x == center)) {
            rgb = new Color(255, 0, 0, 1).getRGB();
        } else {
            float m = f(x, y);
            rgb = Color.getHSBColor(m/max_iter, 1f, m).getRGB();
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
