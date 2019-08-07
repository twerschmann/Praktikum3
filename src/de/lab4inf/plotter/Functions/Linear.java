package de.lab4inf.plotter.Functions;

public class Linear implements Function {
    private double m, n;

    public Linear (double pM, double pN) {
        this.m = pM;
        this.n = pN;
    }

    @Override
    public double f(double x) {
        return x * this.m + this.n;
    }
}
