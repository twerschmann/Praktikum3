package de.lab4inf.plotter.Functions;

public class Cube implements Function{
    @Override
    public double f(final double x) {
        return Math.pow(x, 3);
    }
}
