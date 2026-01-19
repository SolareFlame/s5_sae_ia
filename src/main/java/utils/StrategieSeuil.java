package utils;

public class StrategieSeuil implements EvaluationStrategy {
    private final double precision;

    public StrategieSeuil(double precision) {
        this.precision = precision;
    }

    @Override
    public boolean estCorrect(double[] sortieCalculee, double[] sortieAttendue) {
        for (int i = 0; i < sortieCalculee.length; i++) {
            if (Math.abs(sortieCalculee[i] - sortieAttendue[i]) > precision) {
                return false;
            }
        }
        return true;
    }
}