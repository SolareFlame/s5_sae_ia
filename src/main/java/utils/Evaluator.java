package utils;

import data.Dataset;
import mlp.MLP;
import utils.evaluation.EvaluationStrategy;

public class Evaluator {

    private final EvaluationStrategy strategy;

    public Evaluator(EvaluationStrategy strategy) {
        this.strategy = strategy;
    }

    public double evaluateData(MLP mlp, double[][] inputs, double[][] expected) {
        int correct = 0;
        for (int i = 0; i < inputs.length; i++) {
            double[] predicted = mlp.execute(inputs[i]);
            if (strategy.isCorrect(predicted, expected[i])) {
                correct++;
            }
        }
        return (double) correct / inputs.length;
    }

    public double evaluate(MLP mlp, Dataset dataset) {
        double acc = evaluateData(mlp, dataset.getTestInputs(), dataset.getTestOutputs());
        System.out.printf("Evaluation Test [%s] : %.2f%%", strategy.getName(), acc * 100);
        return acc;
    }

    public double evaluateTrain(MLP mlp, Dataset dataset) {
        return evaluateData(mlp, dataset.getTrainInputs(), dataset.getTrainOutputs());
    }
}