package de.lab4inf.plotter.Functions;

public class Sinus implements Function {
    @Override
    public double f(final double x) {
        return Math.sin(x);
    }
}
