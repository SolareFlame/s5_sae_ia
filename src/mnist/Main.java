package mnist;

import loader.ChargementMNIST;
import loader.Imagette;
import neuronal_network.MLP;
import neuronal_network.Sigmoide;
import neuronal_network.TransferFunction;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String fichierImages = "Ensemble des fichiers MNIST-20251007/train-images-idx3-ubyte/train-images.idx3-ubyte";
        String fichierLabels = "Ensemble des fichiers MNIST-20251007/train-labels-idx1-ubyte/train-labels.idx1-ubyte";

        int nbImagesEntrainement = 1000;
        int nbPassages = 10;
        double learningRate = 0.1;

        // 784 entré et 10 sortie
        int[] layers = {784, 10};
        TransferFunction fonctionActivation = new Sigmoide();
        MLP mlp = new MLP(layers, learningRate, fonctionActivation);

        ChargementMNIST loader = new ChargementMNIST();
        Imagette[] imagettes = loader.charger(fichierImages, fichierLabels, nbImagesEntrainement);

        System.out.println("Début de l'apprentissage...");

        for (int passage = 0; passage < nbPassages; passage++) {
            double erreurCumulee = 0;

            for (Imagette img : imagettes) {
                double[] input = preparerEntree(img);

                double[] target = creerObjectif(img.getLabel(), 10);

                erreurCumulee += mlp.backPropagate(input, target);
            }
            System.out.println("Passage " + passage + " - Erreur moyenne : " + (erreurCumulee / nbImagesEntrainement));
        }

        score(mlp, imagettes);
    }

    private static double[] preparerEntree(Imagette img) {
        double[] entree = new double[784];
        int index = 0;
        for (int l = 0; l < img.getLignes(); l++) {
            for (int c = 0; c < img.getColonnes(); c++) {
                // pour valeur entre 0 et 1 (que pour sigmoide pour l'instant)
                entree[index++] = img.getValeur(l, c) / 255.0;
            }
        }
        return entree;
    }

    private static double[] creerObjectif(int label, int taille) {
        double[] cible = new double[taille];
        for (int i = 0; i < taille; i++) {
            cible[i] = (i == label) ? 1.0 : 0.0;
        }
        return cible;
    }

    private static void score(MLP mlp, Imagette[] imagettes) {
        int correct = 0;
        for (Imagette img : imagettes) {
            double[] sortie = mlp.execute(preparerEntree(img));

            int prediction = 0;
            for (int i = 1; i < sortie.length; i++) {
                if (sortie[i] > sortie[prediction]) prediction = i;
            }

            if (prediction == img.getLabel()) correct++;
        }
        double taux = (double) correct / imagettes.length * 100;
        System.out.println("Précision : " + correct + "/" + imagettes.length + " (" + taux + "%)");
    }
}