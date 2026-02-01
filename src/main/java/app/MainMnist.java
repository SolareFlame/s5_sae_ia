package app;

import data.RangeConversion;
import data.loaders.MnistDataset;
import mlp.*;
import utils.*;
import utils.evaluation.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MainMnist {

    // fashion
    private static final String F_TRAIN_IMG = "src/main/resources/EnsembleFashionMnist/train-images-idx3-ubyte";
    private static final String F_TRAIN_LBL = "src/main/resources/EnsembleFashionMnist/train-labels-idx1-ubyte";
    private static final String F_TEST_IMG  = "src/main/resources/EnsembleFashionMnist/t10k-images-idx3-ubyte";
    private static final String F_TEST_LBL  = "src/main/resources/EnsembleFashionMnist/t10k-labels-idx1-ubyte";

    // nombres
    private static final String M_TRAIN_IMG = "src/main/resources/Ensemble des fichiers MNIST-20251007/train-images-idx3-ubyte/train-images.idx3-ubyte";
    private static final String M_TRAIN_LBL = "src/main/resources/Ensemble des fichiers MNIST-20251007/train-labels-idx1-ubyte/train-labels.idx1-ubyte";
    private static final String M_TEST_IMG  = "src/main/resources/Ensemble des fichiers MNIST-20251007/t10k-images-idx3-ubyte/t10k-images.idx3-ubyte";
    private static final String M_TEST_LBL  = "src/main/resources/Ensemble des fichiers MNIST-20251007/t10k-labels-idx1-ubyte/t10k-labels.idx1-ubyte";

    public static void main(String[] args) throws IOException {
        new File("logs").mkdirs();

        Scanner scanner = new Scanner(System.in);


        System.out.println("1. Mode MANUEL (Test unique)");
        System.out.println("2. Faire tout les cas possible");
        System.out.print("Votre choix : ");

        int choix = -1;
        if (scanner.hasNextInt()) choix = scanner.nextInt();
        scanner.nextLine();

        if (choix == 1) {
            manualMode(scanner);
        } else if (choix == 2) {
            lancerToutLesCas();
        } else {
            System.out.println("Choix invalide.");
        }
        scanner.close();
    }

    private static void manualMode(Scanner scanner) throws IOException {
        System.out.println("Mode Manuel :");

        System.out.print("Type de données (1=Nombre, 2=Fashion) : ");
        int type = scanner.nextInt();
        boolean useFashion = (type == 2);

        System.out.print("Fonction (1=Sigmoide, 2=Tanh) : ");
        int funcChoice = scanner.nextInt();
        TransferFunction func = (funcChoice == 2) ? new TangenteHyperbolique() : new Sigmoide();
        RangeConversion range = (funcChoice == 2) ? RangeConversion.MINUS_ONE_ONE : RangeConversion.ZERO_ONE;

        System.out.print("Couches cachées (ex: 128,64) : ");
        String layersStr = scanner.next();
        int[] hiddenLayers;
        if (layersStr.equals("0")) {
            hiddenLayers = new int[]{};
        } else {
            String[] parts = layersStr.split(",");
            hiddenLayers = new int[parts.length];
            for (int i = 0; i < parts.length; i++) hiddenLayers[i] = Integer.parseInt(parts[i]);
        }

        System.out.print("Mélanger les données ? (true/false) : ");
        boolean shuffle = scanner.nextBoolean();

        System.out.print("Taille dataset entrainement (ex: 20000) : ");
        int trainSize = scanner.nextInt();

        System.out.print("Learning Rate (ex: 0.1) : ");
        double lr = scanner.nextDouble();

        System.out.print("Nombre d'époques (ex: 20) : ");
        int epochs = scanner.nextInt();

        System.out.print("Sauvegarder CSV ? (o/n) : ");
        String saveCsv = scanner.next();
        String logPath = null;

        if (saveCsv.equalsIgnoreCase("o")) {
            System.out.print("Nom du fichier (ex: mnist_test.csv) : ");
            String name = scanner.next();
            logPath = "logs/" + name;
            File f = new File(logPath);
            if (f.getParentFile() != null) f.getParentFile().mkdirs();
        }

        lancer(useFashion, func, range, hiddenLayers, lr, epochs, trainSize, shuffle, logPath);
    }

    private static void lancerToutLesCas() throws IOException {
        System.out.println("Lancement de tout les cas possible");

        boolean[] fashionOptions = {false, true};

        boolean[] shuffleOptions = {true, false};

        double[] learningRates = {0.01, 0.1, 0.5};

        int[][] architectures = {
                {},
                {64},
                {128},
                {128, 64}
        };

        ActivationConfig[] functions = {
                new ActivationConfig(new Sigmoide(), RangeConversion.ZERO_ONE),
                new ActivationConfig(new TangenteHyperbolique(), RangeConversion.MINUS_ONE_ONE)
        };

        int nbImages = 10000;
        int epochs = 20;

        int total = fashionOptions.length * shuffleOptions.length * learningRates.length * architectures.length * functions.length;
        int current = 0;

        for (boolean useFashion : fashionOptions) {
            String dataName = useFashion ? "Fashion" : "Numbers";

            for (ActivationConfig conf : functions) {
                for (boolean shuffle : shuffleOptions) {
                    for (int[] hidden : architectures) {
                        for (double lr : learningRates) {
                            current++;

                            String funcName = conf.func.getClass().getSimpleName();
                            String shufStr = shuffle ? "Shuffle" : "NoShuffle";

                            StringBuilder archName = new StringBuilder();
                            if (hidden.length == 0) archName.append("0");
                            else {
                                for (int i = 0; i < hidden.length; i++) {
                                    archName.append(hidden[i]).append(i == hidden.length - 1 ? "" : "-");
                                }
                            }

                            String directoryPath = "logs/MNIST/" + dataName + "/" + (shuffle ? "Melange" : "NonMelange");
                            File dir = new File(directoryPath);
                            if (!dir.exists()) dir.mkdirs();

                            String filename = String.format("%s/%s_%s_%s_Arch%s_LR%.2f.csv",
                                    directoryPath, dataName, funcName, shufStr, archName.toString(), lr);

                            System.out.printf("[%d/%d] En cours : %s (LR: %.2f)\n", current, total, filename, lr);

                            lancer(useFashion, conf.func, conf.range, hidden, lr, epochs, nbImages, shuffle, filename);
                        }
                    }
                }
            }
        }
        System.out.println("Fini ! Tout est mis dans le dossier /logs/Portes_Logiques/...");
    }


    public static void lancer(boolean useFashion, TransferFunction func, RangeConversion range,
                              int[] hiddenLayers, double lr, int epochs, int trainSize,
                              boolean shuffle, String logPath) throws IOException {

        String tImg = useFashion ? F_TRAIN_IMG : M_TRAIN_IMG;
        String tLbl = useFashion ? F_TRAIN_LBL : M_TRAIN_LBL;
        String testImg = useFashion ? F_TEST_IMG : M_TEST_IMG;
        String testLbl = useFashion ? F_TEST_LBL : M_TEST_LBL;

        MnistDataset data = new MnistDataset(tImg, tLbl, testImg, testLbl, trainSize, 1000, range);

        int[] layers = new int[2 + hiddenLayers.length];
        layers[0] = 784;
        for (int i = 0; i < hiddenLayers.length; i++) layers[i + 1] = hiddenLayers[i];
        layers[layers.length - 1] = 10;

        MLP mlp = new MLP(layers, lr, func);

        Evaluator evaluator = new Evaluator(new ClassificationStrategy());
        MlpTrainer trainer = new MlpTrainer(mlp, data, epochs);

        if (logPath != null) {
            trainer.setLogger(logPath, evaluator);
        }
        trainer.train(shuffle);

        if (logPath == null || !logPath.startsWith("logs/")) {
            System.out.println("Fini");
            evaluator.evaluate(mlp, data);
        }
    }

    private static class ActivationConfig {
        TransferFunction func;
        RangeConversion range;
        ActivationConfig(TransferFunction f, RangeConversion r) { this.func = f; this.range = r; }
    }
}