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
    protected double center_x = 0, center_y = 0; // Cambiar el centro
    protected double zoom = 5; // Cambiar el tamaño
    protected String name = "mandel", prefix = "",
            pathImg = "D:\\CODE\\Java\\Fractals\\src\\files\\Fractals\\imgs\\",
            pathStg = "D:\\CODE\\Java\\Fractals\\src\\files\\Fractals\\cfg\\",
            pathSave = "D:\\CODE\\Java\\Fractals\\src\\files\\Fractals\\imgs\\";
    protected int max_iter = 34;
    protected int size = 800;
    protected boolean autoMaxIte, info, cross, nameSize, nameIter, nameZoom;

    public Fractal() {
        try (Scanner sc = new Scanner(new File(pathStg + "settings.cfg"))) {
            center_x = getScanner(sc).nextDouble();
            center_y = getScanner(sc).nextDouble();
            zoom = getScanner(sc).nextDouble();
            max_iter = getScanner(sc).nextInt();
            size = getScanner(sc).nextInt();
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

    public void saveSettings() throws FileNotFoundException {
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

    private void updateMaxIters() {
        max_iter = (int) (
                Math.sqrt(
                        2 * Math.sqrt(
                                Math.abs(1 - Math.sqrt(5 * zoom))
                        )
                ) * 66.5
        );
    }

    public void moveX(double dir) {
        center_x += dir / (zoom * 2);
    }

    public void moveY(double dir) {
        center_y += dir / (zoom * 2);
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMax_iter(int max_iter) {
        this.max_iter = max_iter;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setInfo(boolean info) {
        this.info = info;
    }

    public void setCross(boolean cross) {
        this.cross = cross;
    }

    public void setNameSize(boolean nameSize) {
        this.nameSize = nameSize;
    }

    public void setNameIter(boolean nameIter) {
        this.nameIter = nameIter;
    }

    public void setNameZoom(boolean nameZoom) {
        this.nameZoom = nameZoom;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private Scanner getScanner(Scanner sc) {
        Scanner bit = new Scanner(sc.nextLine());
        bit.next();
        return bit;
    }

    private int coord_to_pos(double d) {
        return (int) (d + (double) size / 2);
    }

    private int f(int x, int y) {
        double offset_x = center_x + ((double) x / size - 0.5) * (1 / zoom / 0.25);
        double offset_y = -center_y + ((double) y / size - 0.5) * (1 / zoom / 0.25);
        Complex c = new Complex(offset_x, offset_y);
        Complex z = new Complex();
        int n = 0;
        while (z.modulus() <= 2 && n < max_iter) {
            z = z.pow(2).add(c);
            n++;
        }
        return n < max_iter ? n : 0;
    }

    public void gen(JProgressBar progressBar) {
        if (autoMaxIte)
            updateMaxIters();
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        File file = new File(genName());
        for (int y = 0; y < size; y++) {
            int color;
            if (info) {
                double progress = (double) y / size * 100;
                if (progressBar != null)
                    progressBar.setValue((int) progress);
                else
                    System.out.println(progress);
            }
            for (int x = 0; x < size; x++) {
                if (cross && (y == coord_to_pos(center_y) || x == coord_to_pos(center_x)))
                    color = new Color(255, 0, 0, 1).getRGB();
                else
                    color = getColor(f(x, y));
                img.setRGB(x, y, color);
            }
        }
        writeImage(img, file);
    }

    private String genName() {
        return pathImg + prefix + name +
                (nameSize ? ", s=" + size : "") +
                (nameIter ? ", n=" + max_iter : "") +
                (nameZoom ? ", z=" + "1E" + Math.log10(zoom) : "") +
                ".png";
    }

    private int getColor(int c) {
        int val = (c * 255) / (max_iter - 1);
        return new Color(val, 0, 0, 1).getRGB();
    }

    private void writeImage(BufferedImage img, File file) {
        try {
            ImageIO.write(img, "png", file);
        } catch (IOException ioe) {
            ioe.getStackTrace();
        }
    }
}
