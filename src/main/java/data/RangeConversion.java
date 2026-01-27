package data;

public enum RangeConversion {
    // sigmoide entre 0 et 1
    ZERO_ONE(0, 1),
    // tanh entre -1 et 1
    MINUS_ONE_ONE(-1, 1);

    public final double min;
    public final double max;

    RangeConversion(double min, double max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Convertit les 0 ou 1 en fonction de la fonction que on utilise
     * par ex pour tanh 0 devient -1, 1 devient 1.
     */
    public double convert(double rawValue) {
        return (rawValue > 0.5) ? max : min;
    }
}