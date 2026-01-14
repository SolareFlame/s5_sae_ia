package utils;

import neuronal_network.MLP;
import java.util.List;

public class EntraineurMLP {
    private final MLP mlp;

    public EntraineurMLP(MLP mlp) {
        this.mlp = mlp;
    }


    public void entrainer(List<DonneeApprentissage> donnees, int nbEpoques) {

        for (int passage = 0; passage < nbEpoques; passage++) {
            double erreurCumulee = 0;

            for (DonneeApprentissage donnee : donnees) {
                erreurCumulee += mlp.backPropagate(donnee.entree(), donnee.sortieAttendue());
            }

            double erreurMoyenne = erreurCumulee / donnees.size();
            System.out.println("Passage " + (passage + 1) + "/" + nbEpoques + " - Erreur moyenne : " + erreurMoyenne);

        }
    }
}