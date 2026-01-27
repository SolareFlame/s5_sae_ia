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

    public static void main(String[] args) throws IOException {
        boolean useFashion = true;
        String trainImg = useFashion ? "src/main/ressources/EnsembleFashionMnist/train-images-idx3-ubyte" : "src/main/ressources/Ensemble des fichiers MNIST-20251007/train-images-idx3-ubyte/train-images.idx3-ubyte";
        String trainLbl = useFashion ? "src/main/ressources/EnsembleFashionMnist/train-labels-idx1-ubyte" : "src/main/ressources/Ensemble des fichiers MNIST-20251007/train-labels-idx1-ubyte/train-labels.idx1-ubyte";
        String testImg = useFashion ? "src/main/ressources/EnsembleFashionMnist/t10k-images-idx3-ubyte" : "src/main/ressources/Ensemble des fichiers MNIST-20251007/t10k-images-idx3-ubyte/t10k-images.idx3-ubyte";
        String testLbl = useFashion ? "src/main/ressources/EnsembleFashionMnist/t10k-labels-idx1-ubyte" : "src/main/ressources/Ensemble des fichiers MNIST-20251007/t10k-labels-idx1-ubyte/t10k-labels.idx1-ubyte";

        boolean useTanh = false; // pour facilement passer de tanh a sigmoide pour les tests on va nettoyer ça plus tard
        TransferFunction myFunc = useTanh ? new TangenteHyperbolique() : new Sigmoide();
        RangeConversion myRange = useTanh ? RangeConversion.MINUS_ONE_ONE : RangeConversion.ZERO_ONE;

        double learningRate = useTanh ? 0.01 : 0.1; //mettre 0.1 sur mnist sigmoide et 0.01 sur tanh

        System.out.println("Mode: " + (useTanh ? "Tanh (-1,1)" : "Sigmoide (0,1)"));
        System.out.println("Learning Rate: " + learningRate);


        MnistDataset data = new MnistDataset(trainImg, trainLbl, testImg, testLbl, 20000, 1000, myRange);

        int[] layers = {784, 512, 256, 10};
        MLP mlp = new MLP(layers, learningRate, myFunc);

        MlpTrainer trainer = new MlpTrainer(mlp, data, 20);
        Evaluator evaluator = new Evaluator(new ClassificationStrategy());

        trainer.setLogger("mnist_results.csv", evaluator);
        trainer.train();


        evaluator.evaluate(mlp, data);
    }
}