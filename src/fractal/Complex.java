package fractal;

public class Complex {
    private final double x, y;

    public Complex() {
        this(0, 0);
    }

    public Complex(double real, double imaginary) {
        x = real;
        y = imaginary;
    }

    public double modulus() {
        return Math.sqrt(x * x + y * y);
    }

    public Complex add(Complex c) {
        return new Complex(x + c.x, y + c.y);
    }

    public Complex multiply(Complex c) {
        return new Complex(x * c.x - y * c.y, x * c.y + y * c.x);
    }

    // TODO WIP
    public Complex pow(int i) {
        Complex p = this;
        for (int c = 1; c < i; c++)
            p = p.multiply(p);
        return p;
    }

    public Complex inverse() {
        double nx, ny;
        nx = x / (x * x + y * y);
        ny = -y / (x * x + y * y);
        return new Complex(nx, ny);
    }

    public String toString() {
        return "{" + x + " + " + y + "i} -> " + modulus();
    }
}
