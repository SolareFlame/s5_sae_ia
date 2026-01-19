package loader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        //TODO  fichier pour rappeler comment utilisé a sup plus tard
        ChargementMNIST chargementMNIST =  new ChargementMNIST();

        String nomFichier = "Ensemble des fichiers MNIST-20251007/t10k-images-idx3-ubyte/t10k-images.idx3-ubyte";
        String nomLabel = "Ensemble des fichiers MNIST-20251007/t10k-labels-idx1-ubyte/t10k-labels.idx1-ubyte";
        Imagette[] imagettes = chargementMNIST.charger(nomFichier,nomLabel,100);

        imagettes[0].sauverImage("gen/image1.png");
        imagettes[99].sauverImage("gen/image2.png");

        System.out.println(imagettes[0].getLabel());
        System.out.println(imagettes[99].getLabel());

    }
}
