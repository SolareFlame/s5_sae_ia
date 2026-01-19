package app;

import neuronal_network.MLP;
import neuronal_network.Sigmoide;
import utils.*;

import java.util.List;

public class MainLogique {

    public static void main(String[] args) {

        String[] fichiers = {
                "Data/AND_1_sortie",
                "Data/AND_2_sortie",
                "Data/OR_1_sortie",
                "Data/OR_2_sortie",
                "Data/XOR_1_sortie",
                "Data/XOR_2_sortie"
        };

        for (String fichier : fichiers) {
            testerFichier(fichier);
        }
    }

    private static void testerFichier(String cheminFichier) {
        System.out.println("\n=======================================================================================");
        System.out.println("Fichier : " + cheminFichier);

        List<DonneeApprentissage> donnees = LoadData.load(cheminFichier);

        if (donnees.isEmpty()) {
            System.out.println("Aucune donnée trouvée ou fichier vide.");
            return;
        }

        int tailleEntree = donnees.getFirst().entree().length;
        int tailleSortie = donnees.getFirst().sortieAttendue().length;

        System.out.println(tailleEntree + " entrées et " + tailleSortie + " sorties");

        int[] couches = {tailleEntree, 3, tailleSortie};
        MLP mlp = new MLP(couches, 0.5, new Sigmoide());

        EntraineurMLP entraineur = new EntraineurMLP(mlp);
        entraineur.train(donnees, 50000);

        Evaluateur.Resultat resultat = Evaluateur.evaluate(mlp, donnees, new StrategieSeuil(0.1));
        System.out.println(resultat);
    }
}