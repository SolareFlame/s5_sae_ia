package app;

import neuronal_network.MLP;
import neuronal_network.TransferFunction;
import utils.DonneeApprentissage;
import utils.LoadData;

import java.util.List;
// TODO LEGACY A SUP
public class Modele {
    private MLP mlp;
    private List<DonneeApprentissage> dataset;

    public Modele(int[] layers, double learningRate, TransferFunction fonctionActivation, String dataPath) {
        this.mlp = new MLP(layers, learningRate, fonctionActivation);
        this.loadData(dataPath);
    }


    public void train(int epochs, boolean verbose) {
        if (dataset == null || dataset.isEmpty()) return;

        for (int i = 0; i < epochs; i++) {
            double totalError = 0.0;

            for (int j = 0; j < dataset.size(); j++) {
                DonneeApprentissage donnee = dataset.get(j);
                double error = mlp.backPropagate(donnee.entree(), donnee.sortieAttendue());
                totalError += error;

                if(verbose) {
                    System.out.println("erreur à l'appelle " + i + ", entrée " + j + " : " + error);
                }
            }

            //si apres un tour des données l'erreur est nulle on arrete l'entrainement
            if(totalError == 0.0) {
                if (verbose) {
                    System.out.println("Entraînement arrêté à l'époque " + i + " car l'erreur est nulle.");
                }
                break;
            }
        }
    }

    public double[] execute(double[] input) {
        //on verifie que la taille de l'input correspond au nombre de neurones d'entrée
        if(input.length != this.mlp.getInputLayerSize()) {
            throw new IllegalArgumentException("La taille de l'entrée ne correspond pas au nombre de neurones d'entrée.");
        }
        return mlp.execute(input);
    }


    public void loadData(String dataPath) {
        this.dataset = LoadData.load(dataPath);

        if (this.dataset.isEmpty()) {
            throw new IllegalArgumentException("Le fichier de données est vide ou introuvable : " + dataPath);
        }

        DonneeApprentissage premierExemple = this.dataset.getFirst();

        //on verifie si le nombre de neurones d'entrée et de sortie correspond bien a la taille des données
        if(premierExemple.entree().length != this.mlp.getInputLayerSize()) {
            throw new IllegalArgumentException("Le nombre de neurones d'entrée ne correspond pas à la taille des données d'entrée.");
        }
        if(premierExemple.sortieAttendue().length != this.mlp.getOutputLayerSize()) {
            throw new IllegalArgumentException("Le nombre de neurones de sortie ne correspond pas à la taille des données de sortie.");
        }
    }


    public double[] evaluate(double precision) {
        //on évalue le modèle sur les données chargées
        //avec un certain niveau de précision
        int correct = 0, incorrect = 0;

        for (DonneeApprentissage donnee : dataset) {
            double[] output = mlp.execute(donnee.entree());

            if(Math.abs(output[0] - donnee.sortieAttendue()[0]) <= precision) {
                correct++;
            } else {
                incorrect++;
            }
        }

        return new double[] { correct, incorrect, dataset.size() };
    }
}
