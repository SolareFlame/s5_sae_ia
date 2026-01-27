package app;

import data.RangeConversion;
import data.Dataset;
import data.loaders.LogicGateDataset;
import mlp.MLP;
import mlp.Sigmoide;
import mlp.TangenteHyperbolique;
import mlp.TransferFunction;
import utils.*;
import utils.evaluation.ThresholdStrategy;

public class MainLogique {
    public static void main(String[] args) {

        boolean useTanh = true;
        String gatename = "AND";

        int outputSize = 1;

        TransferFunction func = useTanh ? new TangenteHyperbolique() : new Sigmoide();
        RangeConversion range = useTanh ? RangeConversion.MINUS_ONE_ONE : RangeConversion.ZERO_ONE;
        double learningRate = useTanh ? 0.05 : 0.2;


        double[][] inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        double[] tableVerite = switch (gatename) {
            case "AND" -> new double[]{0, 0, 0, 1};
            case "OR" -> new double[]{0, 1, 1, 1};
            case "XOR" -> new double[]{0, 1, 1, 0};
            case "NAND" -> new double[]{1, 1, 1, 0};
            default -> throw new IllegalArgumentException("Porte inconnue");
        };

        Dataset dataset = new LogicGateDataset(gatename, inputs, tableVerite, range, outputSize);

        int[] layers = {2, 4, outputSize};
        MLP mlp = new MLP(layers, learningRate, func);

        System.out.println("Entrainement sur " + gatename + " avec " + func.getClass().getSimpleName());
        System.out.println("Range: " + range + " | Sorties: " + outputSize);

        MlpTrainer trainer = new MlpTrainer(mlp, dataset, 10000);
        trainer.train();

        Evaluator evaluator = new Evaluator(new ThresholdStrategy(0.1));
        evaluator.evaluate(mlp, dataset);
    }
}