import neuronal_network.TangenteHyperbolique;
import neuronal_network.TransferFunction;

public class Main2 {
    public static void main(String[] args) {
        TransferFunction func = new TangenteHyperbolique();

        int[] layers = {2, 4, 4, 1};
        double learningRate = 0.1;
        String dataPath = "Data/XOR_1_sortie";

        //on cree le modele
        var modele = new Modele(layers, learningRate, func, dataPath);

        //on entraine le modele
        modele.train(1000, false);

        //on test
        double[] exec = modele.execute(new double[]{0, 0});
        System.out.println("Sortie pour l'entrée [0, 0] : " + exec[0]);

        //on evalue le modele avec un degre de precision
        //(car avec nos fonctions d'activations on n'aura jamais 1 ou 0 car c'est des valeurs exclues)
        double[] evaluation = modele.evaluate(0.1);
        System.out.println("nombre de reponse correcte : " + evaluation[0]);
        System.out.println("nombre de reponse incorrecte : " + evaluation[1]);
        System.out.println("sur un total de : " + evaluation[2]);
    }
}
