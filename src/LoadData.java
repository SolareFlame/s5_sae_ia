import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadData {

    public double[][][] loadData(String nomFichier) {
        List<double[]> inputs = new ArrayList<>();
        List<double[]> outputs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parties = ligne.split(":");
                if (parties.length == 2) {
                    inputs.add(parseDoubles(parties[0].trim()));
                    outputs.add(parseDoubles(parties[1].trim()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        double[][] inputArray = inputs.toArray(new double[0][]);
        double[][] outputArray = outputs.toArray(new double[0][]);

        return new double[][][]{inputArray, outputArray};
    }

    private double[] parseDoubles(String s) {
        String[] valeurs = s.split("\\s+");
        double[] result = new double[valeurs.length];
        for (int i = 0; i < valeurs.length; i++) {
            result[i] = Double.parseDouble(valeurs[i]);
        }
        return result;
    }
}