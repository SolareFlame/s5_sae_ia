package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadData {

    public static List<DonneeApprentissage> load(String nomFichier) {
        List<DonneeApprentissage> dataset = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (ligne.trim().isEmpty()) continue;

                String[] parties = ligne.split(":");
                if (parties.length == 2) {
                    double[] entree = parseDoubles(parties[0].trim());
                    double[] sortie = parseDoubles(parties[1].trim());

                    dataset.add(new DonneeApprentissage(entree, sortie));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataset;
    }

    private static double[] parseDoubles(String s) {
        String[] valeurs = s.split("\\s+");
        double[] result = new double[valeurs.length];
        for (int i = 0; i < valeurs.length; i++) {
            try {
                result[i] = Double.parseDouble(valeurs[i]);
            } catch (NumberFormatException e) {
                result[i] = 0.0;
            }
        }
        return result;
    }
}