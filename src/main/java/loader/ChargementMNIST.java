package loader;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class ChargementMNIST {

    /**
     * donne les noms de fichier, retourne un tableau d'imagettes
     *
     * @param imageFile nom du fichier avec imagettes
     * @param labelFile nom du fichier avec les labels
     * @param max       nombre de valeurs maximales a charger
     * @return tableau des imagettes avec leurs labels
     * @throws IOException probleme de lecture
     */
    public Imagette[] charger(String imageFile, String labelFile, int max) throws IOException {
        // ouverture du fichier image
        DataInputStream di = new DataInputStream(new FileInputStream(imageFile));
        // ouverture du fichier label
        DataInputStream dLabel = new DataInputStream(new FileInputStream(labelFile));

        // ##########################################
        // ouverture des images

        // lecture donnees initiales
        int magicNumber = di.readInt();
        if (magicNumber != 2051)
            throw new Error("pas le bon fichier = magic number" + magicNumber);

        // lecture nb image
        int nbImage = di.readInt();

        // lecture taille des imagettes
        int lignes = di.readInt();
        int cols = di.readInt();

        // ##########################################
        // ouverture des labels

        // lecture donnees initiales
        int magicNumberLabel = dLabel.readInt();
        if (magicNumberLabel != 2049)
            throw new Error("pas le bon fichier = magic number" + magicNumber);

        // lecture nb label
        int nbLabel = dLabel.readInt();

        // si different= erreur
        if (nbLabel != nbImage)
            throw new Error("pas le meme nombre d'elements");

        // ##########################################
        // lecture des imagettes

        // gere max d'images à charger (si max demandé est plus petit que nombre d'images)
        if ((max > 0) && (max < nbImage)) {
            nbImage = max;
        }

        // construction du tableau imagette
        Imagette[] imagettes = new Imagette[nbImage];

        // pour chaque imagette
        for (int i = 0; i < nbImage; i++) {

            // lecture du label avec readUnsignedByte
            int label = dLabel.readUnsignedByte();
            // creation imagette
            Imagette imagette = new Imagette(lignes, cols, label);


            // construction imagette pixel par pixel (avec readUnsignedByte)
            for (int l = 0; l < lignes; l++) {
                for (int c = 0; c < cols; c++) {
                    int val = di.readUnsignedByte();
                    imagette.modifierValeur(l, c, val);

                }
            }

            //  ajout au tableau d'imagettes
            imagettes[i] = imagette;
        }


        // fermeture fichier image
        // fermeture fichier label
        di.close();
        dLabel.close();

        // retourne tableau imagettes
        return imagettes;
    }


}
