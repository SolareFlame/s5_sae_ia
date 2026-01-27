package utils.evaluation;

public class ThresholdStrategy implements EvaluationStrategy {
    private final double tolerance;

    public ThresholdStrategy(double tolerance) {
        this.tolerance = tolerance;
    }

    @Override
    public boolean isCorrect(double[] predicted, double[] expected) {
        for (int i = 0; i < predicted.length; i++) {
            if (Math.abs(predicted[i] - expected[i]) > tolerance) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return "Seuil (tolérance " + tolerance + ")";
    }
}