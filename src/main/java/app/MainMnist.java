package app;

import data.RangeConversion;
import data.loaders.MnistDataset;
import mlp.TangenteHyperbolique;
import utils.MlpTrainer;
import mlp.MLP;
import mlp.Sigmoide;
import mlp.TransferFunction;
import utils.Evaluator;
import utils.evaluation.ClassificationStrategy;

import java.io.IOException;

public class MainMnist {

    public static final String trainImg = "src/main/resources/Ensemble des fichiers MNIST-20251007/train-images-idx3-ubyte/train-images.idx3-ubyte";
    public static final String trainLbl = "src/main/resources/Ensemble des fichiers MNIST-20251007/train-labels-idx1-ubyte/train-labels.idx1-ubyte";
    public static final String testImg = "src/main/resources/Ensemble des fichiers MNIST-20251007/t10k-images-idx3-ubyte/t10k-images.idx3-ubyte";
    public static final String testLbl = "src/main/resources/Ensemble des fichiers MNIST-20251007/t10k-labels-idx1-ubyte/t10k-labels.idx1-ubyte";

    public static void main(String[] args) throws IOException {
        boolean useTanh = true; // pour facilement passer de tanh a singoide pour les tests on va netoyer ça plus tard
        TransferFunction myFunc = useTanh ? new TangenteHyperbolique() : new Sigmoide();
        RangeConversion myRange = useTanh ? RangeConversion.MINUS_ONE_ONE : RangeConversion.ZERO_ONE;

        double learningRate = 0.01;

        System.out.println("Mode: " + (useTanh ? "Tanh (-1,1)" : "Sigmoide (0,1)"));
        System.out.println("Learning Rate: " + learningRate);


        MnistDataset data = new MnistDataset(trainImg, trainLbl, testImg, testLbl, 2000, 1000, myRange);

        int[] layers = {784, 128, 10};
        MLP mlp = new MLP(layers, learningRate, myFunc);

        MlpTrainer trainer = new MlpTrainer(mlp, data, 20);
        Evaluator evaluator = new Evaluator(new ClassificationStrategy());

        trainer.setLogger("mnist_results.csv", evaluator);
        trainer.train();


        evaluator.evaluate(mlp, data);
    }
}