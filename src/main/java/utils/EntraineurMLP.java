package utils;

import neuronal_network.MLP;
import java.util.List;

public class EntraineurMLP {
    private final MLP mlp;

    public EntraineurMLP(MLP mlp) {
        this.mlp = mlp;
    }

    public void train(List<DonneeApprentissage> donnees, int nbEpoques) {

        for (int passage = 0; passage < nbEpoques; passage++) {
            double erreurCumulee = 0;

            for (DonneeApprentissage donnee : donnees) {
                erreurCumulee += mlp.backPropagate(donnee.entree(), donnee.sortieAttendue());
            }

            double erreurMoyenne = erreurCumulee / donnees.size();

            // modifier ici si vous voulez changer le taux d'affichage
            if (nbEpoques < 20 || passage % 10000 == 0) {
                System.out.println("Passage " + (passage + 1) + " - Erreur moyenne : " + erreurMoyenne);
            }

        }
    }
}