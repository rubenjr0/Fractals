package fractal;

public class Complex {
    protected double r, i;

    public Complex() {
        this(0, 0);
    }

    public Complex(double real, double imaginary) {
        r = real;
        i = imaginary;
    }

    public double modulus() {
        return Math.sqrt(r * r + i * i);
    }

    public Complex add(Complex c) {
        return new Complex(r + c.r, i + c.i);
    }

    public Complex multiply(Complex c) {
        return new Complex(r * c.r - i * c.i, r * c.i + i * c.r);
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
        nx = r / r * r + i * i;
        ny = -i / r * r + i * i;
        return new Complex(nx, ny);
    }

    public String toString() {
        return "{" + r + " + " + i + "i} -> " + modulus();
    }
}
