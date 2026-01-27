package utils.evaluation;

public interface EvaluationStrategy {

    boolean isCorrect(double[] predicted, double[] expected);

    /**
     * Nom de la stratégie (pour l'affichage)
     */
    String getName();
}