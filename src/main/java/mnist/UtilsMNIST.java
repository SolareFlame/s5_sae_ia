package mnist;

import utils.DonneeApprentissage;
import loader.Imagette;

import java.util.ArrayList;
import java.util.List;

public class UtilsMNIST {

    /**
     * Convertit un tableau d'Imagettes en liste DonneeApprentissage
     */
    public static List<DonneeApprentissage> convertirDonnees(Imagette[] imagettes, int tailleSortie) {
        List<DonneeApprentissage> liste = new ArrayList<>();
        for (Imagette img : imagettes) {
            liste.add(new DonneeApprentissage(imagetteVersEntree(img), labelToTab(img.getLabel(), tailleSortie)
            ));
        }
        return liste;
    }

    /**
     * Convertie les valeurs pour etre entre 0 et 1
     */
    public static double[] imagetteVersEntree(Imagette img) {
        double[] entree = new double[img.getLignes() * img.getColonnes()];
        int index = 0;
        for (int l = 0; l < img.getLignes(); l++) {
            for (int c = 0; c < img.getColonnes(); c++) {
                // marche que pour sigmoide ducoup
                entree[index++] = img.getValeur(l, c) / 255.0;
            }
        }
        return entree;
    }

    /**
     * Transforme le label en tableau (ex pour le chiffre 3 on aura [0,0,0,1,0,0,0,0,0,0])
     * ça va nous permettre de faciliter l'identification de la sortie attendue
     */
    public static double[] labelToTab(int label, int taille) {
        double[] cible = new double[taille];
        for (int i = 0; i < taille; i++) {
            cible[i] = (i == label) ? 1.0 : 0.0;
        }
        return cible;
    }

    /**
     * Retourne l'index de la valeur max
     */
    public static int tabToLabel(double[] sortie) {
        int prediction = 0;
        for (int i = 1; i < sortie.length; i++) {
            if (sortie[i] > sortie[prediction]) {
                prediction = i;
            }
        }
        return prediction;
    }
}