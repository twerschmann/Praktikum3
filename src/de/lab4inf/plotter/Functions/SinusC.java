package de.lab4inf.plotter.Functions;

public class SinusC implements Function {
    @Override
    public double f(double x) {
        return Math.sin( Math.PI * x) / (Math.PI *x);
    }
}
