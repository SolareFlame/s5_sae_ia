package mnist;

import utils.DonneeApprentissage;
import utils.EntraineurMLP;
import loader.ChargementMNIST;
import loader.Imagette;
import neuronal_network.MLP;
import neuronal_network.Sigmoide;
import neuronal_network.TransferFunction;
import utils.Evaluateur;
import utils.StrategieClassification;

import java.io.IOException;
import java.util.List;

public class Main {

    private static final String FICHIER_IMAGES = "Ensemble des fichiers MNIST-20251007/train-images-idx3-ubyte/train-images.idx3-ubyte";
    private static final String FICHIER_LABELS = "Ensemble des fichiers MNIST-20251007/train-labels-idx1-ubyte/train-labels.idx1-ubyte";

    public static void main(String[] args) throws IOException {
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
        entraineur.train(dataset, nbEpoques);


        Evaluateur.Resultat res = Evaluateur.evaluate(modele,dataset,new StrategieClassification());
        System.out.println(res);

    }

}