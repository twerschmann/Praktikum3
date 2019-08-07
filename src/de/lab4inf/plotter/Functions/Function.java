package de.lab4inf.plotter.Functions;
@FunctionalInterface
public interface Function {
	/**
	* Calculate y = f(x) in double precision . 
	* @param x the argument
	* @return the return value y = f(x)
	*/
	double f(final double x);
}