package utils;

import neuronal_network.MLP;
import java.util.List;

public class Evaluateur {

    // pour stoquer facilement les data
    public static class Resultat {
        public int correct;
        public int incorrect;
        public int total;

        public double getTauxReussite() {
            return (total == 0) ? 0 : (double) correct / total * 100.0;
        }

        @Override
        public String toString() {
            return String.format("Correct: %d | Incorrect: %d | Total: %d | Taux: %.2f%%", correct, incorrect, total, getTauxReussite());
        }
    }

    public static Resultat evaluate(MLP mlp, List<DonneeApprentissage> donnees, double precision) {
        Resultat res = new Resultat();
        res.total = donnees.size();

        for (DonneeApprentissage donnee : donnees) {
            double[] sortieCalculee = mlp.execute(donnee.entree());
            double[] sortieAttendue = donnee.sortieAttendue();

            boolean estCorrect = true;
            for (int i = 0; i < sortieCalculee.length; i++) {
                if (Math.abs(sortieCalculee[i] - sortieAttendue[i]) > precision) {
                    estCorrect = false;
                    break;
                }
            }

            if (estCorrect) {
                res.correct++;
            } else {
                res.incorrect++;
            }
        }
        return res;
    }
}