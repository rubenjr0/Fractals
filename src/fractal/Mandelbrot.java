package fractal;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Mandelbrot {
    double center_x = 0, center_y = 0; // Cambiar el centro
    double len_x = 5, len_y = 5; // Cambiar el tamaño
    private String name = "mandel";
    private int max_iter = 34;
    private int size = 800;
    private boolean info, cross;

    public Mandelbrot(boolean showCross) {
        cross = showCross;
    }

    public Mandelbrot(boolean showCross, boolean showInfo) {
        cross = showCross;
        info = showInfo;
    }

    public Mandelbrot(int max) {
        max_iter = max;
    }

    public Mandelbrot(String name) {
        this.name = name;
    }

    private int coord_to_pos(double d) {
        return (int) (d + (double) size / 2);
    }

    private int f(int x, int y) {
        double offset_x = center_x + ((double) x / size - 0.5) * len_x;
        double offset_y = center_y + ((double) y / size - 0.5) * len_y;
        Complex c = new Complex(offset_x
                , offset_y
        );
        Complex z = new Complex();
        int n = 0;
        while (z.modulus() <= 2 && n < max_iter) {
            z = z.pow(2).add(c);
            n++;
        }
        return n < max_iter ? n : 0;
    }

    // TODO mejorar los colores, separar en varias funciones o limpiar
    public void gen() {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        File file = new File("Fractals\\" + name + ", s=" + size + ", n=" + max_iter + ".png");
        for (int y = 0; y < size; y++) {
            if (info)
                System.out.println((double) y / size * 100);
            for (int x = 0; x < size; x++) {
                int m = f(x, y), val, color;
                if (cross && y == coord_to_pos(center_y) || x == coord_to_pos(center_x))
                    color = new Color(255, 0, 0, 1).getRGB();
                else {
                    val = (m * 255) / (max_iter - 1);
                    color = new Color(val, val, val, 1).getRGB();
                }
                img.setRGB(x, y, color);
            }
        }
        writeImage(img, file);
    }

    private void writeImage(BufferedImage img, File file) {
        try {
            ImageIO.write(img, "png", file);
        } catch (IOException ioe) {
            ioe.getStackTrace();
        }
    }

    public void setName(String name) {
        this.name = name;
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

    public void setMax_iter(int max_iter) {
        this.max_iter = max_iter;
    }

}
