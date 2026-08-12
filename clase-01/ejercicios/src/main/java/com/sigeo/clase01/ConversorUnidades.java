package com.sigeo.clase01;

public class ConversorUnidades {

    /**
     * Convierte grados Celsius a Fahrenheit.
     * Fórmula: (C * 9/5) + 32
     */
    public static double celsiusAFahrenheit(double celsius) {
        return (celsius * 9/5) + 32;
    }

    /**
     * Convierte kilómetros a millas.
     * Fórmula: km * 0.621371
     */
    public static double kilometrosAMillas(double kilometros) {
        return (kilometros * 0.621371);
    }
}
