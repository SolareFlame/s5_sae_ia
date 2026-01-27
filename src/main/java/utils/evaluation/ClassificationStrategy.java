package utils.evaluation;

public class ClassificationStrategy implements EvaluationStrategy {
    @Override
    public boolean isCorrect(double[] predicted, double[] expected) {
        return getArgMax(predicted) == getArgMax(expected);
    }

    private int getArgMax(double[] vector) {
        int index = 0;
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] > max) {
                max = vector[i];
                index = i;
            }
        }
        return index;
    }

    @Override
    public String getName() {
        return "Classification";
    }
}