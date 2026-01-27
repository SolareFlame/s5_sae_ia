package data.loaders;

import data.RangeConversion;
import data.Dataset;
import loader.ChargementMNIST;
import loader.Imagette;

import java.io.IOException;

public class MnistDataset implements Dataset {

    private final double[][] trainInputs;
    private final double[][] trainOutputs;

    private final double[][] testInputs;
    private final double[][] testOutputs;

    public MnistDataset(String trainImgPath, String trainLblPath,
                        String testImgPath, String testLblPath,
                        int maxTrainItems, int maxTestItems, RangeConversion range) throws IOException {

        ChargementMNIST loader = new ChargementMNIST();

        System.out.println("Chargement des donnes d'entrainements");
        Imagette[] rawTrain = loader.charger(trainImgPath, trainLblPath, maxTrainItems);
        this.trainInputs = new double[rawTrain.length][784];
        this.trainOutputs = new double[rawTrain.length][10];
        convertData(rawTrain, this.trainInputs, this.trainOutputs, range);

        System.out.println("Chargement des données de tests");
        Imagette[] rawTest = loader.charger(testImgPath, testLblPath, maxTestItems);
        this.testInputs = new double[rawTest.length][784];
        this.testOutputs = new double[rawTest.length][10];
        convertData(rawTest, this.testInputs, this.testOutputs, range);
    }

    /**
     * Convertit un tableau d'Imagettes en tableau pour les input et output
     */
    private void convertData(Imagette[] raw, double[][] inputs, double[][] outputs, RangeConversion range) {
        for (int i = 0; i < raw.length; i++) {
            Imagette img = raw[i];

            int index = 0;
            for (int l = 0; l < img.getLignes(); l++) {
                for (int c = 0; c < img.getColonnes(); c++) {
                    double val = img.getValeur(l, c) / 255.0;
                    if (range.min < 0) val = (val * 2) - 1; // Mode Tanh
                    inputs[i][index++] = val;
                }
            }

            int label = img.getLabel();
            for (int j = 0; j < 10; j++) {
                outputs[i][j] = (j == label) ? range.max : range.min;
            }
        }
    }

    @Override
    public double[][] getTrainInputs() {
        return trainInputs;
    }

    @Override
    public double[][] getTrainOutputs() {
        return trainOutputs;
    }

    @Override
    public double[][] getTestInputs() {
        return testInputs;
    }

    @Override
    public double[][] getTestOutputs() {
        return testOutputs;
    }

    @Override
    public String getName() {
        return "MNIST";
    }
}