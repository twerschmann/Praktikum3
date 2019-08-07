package de.lab4inf.plotter.Functions;

public class Tangens implements Function {
    @Override
    public double f(double x) {
        return Math.sin(x) / Math.cos(x);
    }
}
