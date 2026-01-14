package mnist;

import utils.DonneeApprentissage;
import utils.EntraineurMLP;
import loader.ChargementMNIST;
import loader.Imagette;
import neuronal_network.MLP;
import neuronal_network.Sigmoide;
import neuronal_network.TransferFunction;

import java.io.IOException;
import java.util.List;

public class Main {

    private static final String FICHIER_IMAGES = "Ensemble des fichiers MNIST-20251007/train-images-idx3-ubyte/train-images.idx3-ubyte";
    private static final String FICHIER_LABELS = "Ensemble des fichiers MNIST-20251007/train-labels-idx1-ubyte/train-labels.idx1-ubyte";

    static void main(String[] args) throws IOException {
        int nbImages = 1000;
        int nbEpoques = 10;
        double learningRate = 0.1;

        // 784 entré et 10 sortie
        int[] couches = {784, 10};

        ChargementMNIST loader = new ChargementMNIST();
        Imagette[] imagettes = loader.charger(FICHIER_IMAGES, FICHIER_LABELS, nbImages);

        // marche que pour sigmoide pour l'instant les val sont entre 0 et 1
        List<DonneeApprentissage> dataset = UtilsMNIST.convertirDonnees(imagettes, 10);

        TransferFunction fonctionActivation = new Sigmoide();
        MLP modele = new MLP(couches, learningRate, fonctionActivation);

        EntraineurMLP entraineur = new EntraineurMLP(modele);
        entraineur.entrainer(dataset, nbEpoques);

        evaluer(modele, imagettes);

    }

    private static void evaluer(MLP mlp, Imagette[] imagettes) {
        int correct = 0;
        for (Imagette img : imagettes) {
            double[] entree = UtilsMNIST.imagetteVersEntree(img);
            double[] sortie = mlp.execute(entree);

            int prediction = UtilsMNIST.predire(sortie);

            if (prediction == img.getLabel()) {
                correct++;
            }
        }
        double taux = (double) correct / imagettes.length * 100;
        System.out.println("Précision : " + correct + "/" + imagettes.length + " (" + taux + "%)");
    }
}