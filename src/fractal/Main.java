package fractal;

public class Main {
    public static void main(String[] args) {
        // Complex c = new Complex(1,2);
        // System.out.println(c.pow(2));
        Mandelbrot mb = new Mandelbrot(true);
        mb.setMax_iter(80);
        mb.setSize(600);
        mb.gen();
    }
}
