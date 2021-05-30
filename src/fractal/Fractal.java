package fractal;

import javax.swing.*;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Fractal {
    private final double log2 = 1 / Math.log(2);

    private double cx, cy;
    private double zoom;
    private int size, maxIter;
    private boolean cross;

    public Fractal() {
        cx = 0;
        cy = 0;
        zoom = 1;
    }

    private double moveK(int direction) {
        return direction * zoom / 2;
    }

    public double getCX() {
        return cx;
    }

    public void setCX(double x) {
        cx = x;
    }

    public void moveCX(int direction) {
        cx += moveK(direction);
    }

    public double getCY() {
        return cy;
    }

    public void setCY(double y) {
        cy = y;
    }

    public void moveCY(int direction) {
        cy += moveK(direction);
    }

    public int getMaxIter() {
        return maxIter;
    }

    public void setMaxIter(int mi) {
        maxIter = mi;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double z) {
        zoom = z;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean getCross() {
        return cross;
    }

    public void setCross(boolean cross) {
        this.cross = cross;
    }

    private double escalate(double point) {
        return (point / size - 0.5f) * 4 * zoom;
    }

    private double f(int x, int y) {
        int n = 0;
        double res = 0;
        double x0 = cx + escalate(x);
        double y0 = -cy + escalate(y);
        double zr = 0, zi = 0;
        double zrsqr = zr * zr;
        double zisqr = zi * zi;
        while ((zrsqr + zisqr <= 16) && (n < maxIter)) {
            double zitemp = zr * zi;
            zitemp += zitemp;
            zitemp += y0;
            double zrtemp = zrsqr - zisqr + x0;
            if (zr == zrtemp && zi == zitemp) {
                n = maxIter;
                break;
            }
            zr = zrtemp;
            zi = zitemp;
            zrsqr = zr * zr;
            zisqr = zi * zi;
            n++;
        }
        if (n < maxIter) {
            float log_zn = (float) Math.log(zrsqr + zisqr) / 2;
            float nu = (float) (Math.log(log_zn / Math.log(2)) / Math.log(2));
            n += 1 - nu;
            res = n + 4 - Math.log(Math.log(zrsqr + zisqr) / 2 * log2) * log2;
        }
        return res;
    }

    public BufferedImage gen(JProgressBar progressBar) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        double progress;
        for (int im = 0; im < size; im++) {
            progress = (double) im / size * 100;
            if (progressBar != null)
                progressBar.setValue((int) progress);
            for (int re = 0; re < size; re++) {
                img.setRGB(re, im, computeColor(re, im));
            }
        }
        if (progressBar != null)
            progressBar.setValue(0);
        return img;
    }

    private double computePalette(float a, float b, float c, float d, double m) {
        return a + b * Math.cos(2 * Math.PI * (c * m + d));
    }

    private int computeColor(int re, int im) {
        int rgb;
        if (cross && (im == size / 2 || im == size / 4 || im == 3 * size / 4 || re == size / 2 || re == size / 4 || re == 3 * size / 4)) {
            rgb = Color.red.getRGB();
        } else {
            double m = f(re, im) / maxIter; //Preset M: -0.5*f(re, im);
            int r = (int) (0xFF * computePalette(0.5f, 0.5f, 1f, 0f, m));
            int g = (int) (0xFF * computePalette(0.5f, 0.5f, 0.7f, 0.15f, m));
            int b = (int) (0xFF * computePalette(0.5f, 0.5f, 0.4f, 0.2f, m));
            rgb = (new Color(r, g, b, 0xFF).getRGB());
        }
        return rgb;
    }
}
