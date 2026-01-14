import neuronal_network.MLP;
import neuronal_network.TangenteHyperbolique;
import neuronal_network.TransferFunction;

import java.util.Arrays;

public class Main {
    //TODO LEGACY A SUP
    public static void main(String[] args) {
        int[] layers = {2, 4, 4, 1};
        double learningRate = 0.1;
        TransferFunction fonctionActivation = new TangenteHyperbolique();
        MLP mlp = new MLP(layers, learningRate, fonctionActivation);
        double[][] inputs = {
                {0, 0},
                {0, 1},
                {1, 0},
                {1, 1}
        };
        double[][] outputs = {
                {0},
                {1},
                {1},
                {0}
        };
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < inputs.length; j++) {
                double error = mlp.backPropagate(inputs[j], outputs[j]);
                System.out.println("erreur à l'appelle " + i + " et l'input " + j + " : " + error);
            }
        }
        int correct = 0, incorrect = 0, total = 0;
        for (int i = 0; i < inputs.length; i++) {
            total++;
            double [] output = mlp.execute(inputs[i]);
            System.out.println(Arrays.toString(output));
            System.out.println(Arrays.toString(outputs[i]));
            if(output == outputs[i]){
                correct++;
            }else{
                incorrect++;
            }
        }
        System.out.println("Il y a " + correct + " réponse(s) correcte(s) et " + incorrect + " réponse(s) incorrecte(s) sur un total de " + total + " réponses." );
    }
}
