package utils;

import mnist.UtilsMNIST;

public class StrategieClassification implements EvaluationStrategy {

    @Override
    public boolean estCorrect(double[] sortieCalculee, double[] sortieAttendue) {
        return UtilsMNIST.tabToLabel(sortieCalculee) == UtilsMNIST.tabToLabel(sortieAttendue);
    }
}