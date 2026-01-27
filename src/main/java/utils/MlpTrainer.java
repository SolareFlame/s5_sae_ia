package utils;

import data.Dataset;
import mlp.MLP;

import java.io.IOException;

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

    public void train() {
        double[][] inputs = dataset.getTrainInputs();
        double[][] outputs = dataset.getTrainOutputs();
        double[][] testInputs = dataset.getTestInputs();
        double[][] testOutputs = dataset.getTestOutputs();

        System.out.println("Début de l'entraînement...");

        for (int epoch = 0; epoch < maxEpoch; epoch++) {
            double totalError = 0;

            for (int i = 0; i < inputs.length; i++) {
                totalError += mlp.backPropagate(inputs[i], outputs[i]);
            }
            double meanError = totalError / inputs.length;

            // si le logger est activé
            if (logger != null && evaluator != null) {
                double totalTestError = 0;
                for (int i = 0; i < testInputs.length; i++) {
                    double[] pred = mlp.execute(testInputs[i]);
                    for (int j = 0; j < pred.length; j++) {
                        totalTestError += Math.pow(pred[j] - testOutputs[i][j], 2);
                    }
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