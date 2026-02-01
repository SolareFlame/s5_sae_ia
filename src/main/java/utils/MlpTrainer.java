package utils;

import data.Dataset;
import mlp.MLP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MlpTrainer {

    private final MLP mlp;
    private final Dataset dataset;
    private final int maxEpoch;

    private CsvLogger logger;
    private Evaluator evaluator;

    public MlpTrainer(MLP mlp, Dataset dataset, int maxEpoch) {
        this.mlp = mlp;
        this.dataset = dataset;
        this.maxEpoch = maxEpoch;
    }

    public void setLogger(String filePath, Evaluator evaluator) throws IOException {
        this.evaluator = evaluator;
        this.logger = new CsvLogger(filePath, "Epoch,TrainLoss,TestLoss,TrainAcc,TestAcc");
    }

    public void train(boolean shuffle) {
        double[][] inputs = dataset.getTrainInputs();
        double[][] outputs = dataset.getTrainOutputs();
        double[][] testInputs = dataset.getTestInputs();
        double[][] testOutputs = dataset.getTestOutputs();

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < inputs.length; i++) {
            indices.add(i);
        }
        System.out.println("Début de l'entraînement...");

        for (int epoch = 0; epoch < maxEpoch; epoch++) {

            // permet de mélanger l'odre d'apprentisage
            if (shuffle) {
                Collections.shuffle(indices);
            }

            double totalError = 0;

            for (int i : indices) {
                totalError += mlp.backPropagate(inputs[i], outputs[i]);
            }
            double meanError = totalError / inputs.length;

            if (logger != null && evaluator != null) {
                double totalTestError = 0;

                for (int i = 0; i < testInputs.length; i++) {
                    double[] pred = mlp.execute(testInputs[i]);

                    double sampleError = 0;
                    for (int j = 0; j < pred.length; j++) {
                        sampleError += Math.abs(pred[j] - testOutputs[i][j]);
                    }
                    totalTestError += sampleError / pred.length;
                }

                double meanTestError = totalTestError / testInputs.length;

                double trainAcc = evaluator.evaluateTrain(mlp, dataset);
                double testAcc = evaluator.evaluateData(mlp, testInputs, testOutputs);

                logger.logStats(epoch, meanError, meanTestError, trainAcc, testAcc);
            }

            if (maxEpoch < 100 || epoch % (maxEpoch / 10) == 0 || epoch == maxEpoch - 1) {
                System.out.printf("Epoch %d - Loss: %.6f\n", epoch, meanError);
            }

            if (meanError < 0.001) break;
        }

        if (logger != null) logger.close();
        System.out.println("Entraînement terminé.");
    }
}