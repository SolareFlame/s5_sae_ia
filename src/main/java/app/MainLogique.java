package app;

import data.RangeConversion;
import data.Dataset;
import data.loaders.LogicGateDataset;
import mlp.*;
import utils.*;
import utils.evaluation.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MainLogique {

    public static void main(String[] args) throws IOException {
        new File("logs").mkdirs();

        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Mode manuel ");
        System.out.println("2. Faire tout les cas possible");
        System.out.print("Votre choix : ");

        int choix = -1;
        if (scanner.hasNextInt()) {
            choix = scanner.nextInt();
            scanner.nextLine();
        }

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
        System.out.println("Mode Manuel : ");

        System.out.print("Porte (AND, OR, XOR) : ");
        String gate = scanner.next().toUpperCase();

        System.out.print("Fonction (1=Sigmoide, 2=Tanh) : ");
        int funcChoice = scanner.nextInt();
        TransferFunction func = (funcChoice == 2) ? new TangenteHyperbolique() : new Sigmoide();
        RangeConversion range = (funcChoice == 2) ? RangeConversion.MINUS_ONE_ONE : RangeConversion.ZERO_ONE;

        System.out.print("Couches cachées (ex: 3 ou 4,2 ou 0) : ");
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

        System.out.print("Learning Rate (ex: 0,1) : ");
        double lr = scanner.nextDouble();

        System.out.print("Nombre d'époques (ex: 5000) : ");
        int epochs = scanner.nextInt();

        System.out.print("Sauvegarder CSV ? (o/n) : ");
        String saveCsv = scanner.next();
        String logPath = null;

        if (saveCsv.equalsIgnoreCase("o")) {
            System.out.print("Nom du fichier (ex: test.csv) : ");
            String name = scanner.next();
            logPath = "logs/" + name;
            File f = new File(logPath);
            if (f.getParentFile() != null) f.getParentFile().mkdirs();
        }

        lancer(gate, func, range, hiddenLayers, lr, epochs, shuffle, logPath);
    }


    private static void lancerToutLesCas() throws IOException {
        System.out.println("Lancement de tout les cas possible");

        String[] gates = {"XOR", "AND", "OR"};
        boolean[] shuffleOptions = {false, true};
        double[] learningRates = {0.1, 0.5};
        int[][] architectures = { {}, {3}, {8}, {4, 2} };

        ActivationConfig[] functions = {
                new ActivationConfig(new Sigmoide(), RangeConversion.ZERO_ONE),
                new ActivationConfig(new TangenteHyperbolique(), RangeConversion.MINUS_ONE_ONE)
        };

        int total = gates.length * shuffleOptions.length * learningRates.length * architectures.length * functions.length;
        int current = 0;

        for (String gate : gates) {
            for (ActivationConfig conf : functions) {
                for (boolean shuffle : shuffleOptions) {
                    for (int[] hidden : architectures) {
                        for (double lr : learningRates) {
                            current++;

                            String funcName = conf.func.getClass().getSimpleName();
                            String shufStr = shuffle ? "Shuffle" : "NoShuffle";
                            String hidStr = (hidden.length == 0) ? "0" : "Many";
                            if(hidden.length == 1) hidStr = String.valueOf(hidden[0]);

                            String folderState = shuffle ? "Melange" : "NonMelange";
                            String directoryPath = "logs/Portes_Logiques/" + gate + "/" + folderState;

                            File dir = new File(directoryPath);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }

                            String filename = String.format("%s/%s_%s_%s_Hid%s_LR%.1f.csv",
                                    directoryPath, gate, funcName, shufStr, hidStr, lr);

                            System.out.printf("[%d/%d] Génération : %s\n", current, total, filename);

                            lancer(gate, conf.func, conf.range, hidden, lr, 2000, shuffle, filename);
                        }
                    }
                }
            }
        }
        System.out.println("Fini ! Tout est mis dans le dossier /logs/Portes_Logiques/...");
    }

    public static void lancer(String gate, TransferFunction func, RangeConversion range,
                              int[] hiddenLayers, double lr, int epochs,
                              boolean shuffle, String logPath) throws IOException {


        double[][] inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        double[] truth = getTruthTable(gate);
        Dataset dataset = new LogicGateDataset(gate, inputs, truth, range, 1);

        int[] layers = new int[2 + hiddenLayers.length];
        layers[0] = 2;
        for (int i = 0; i < hiddenLayers.length; i++) layers[i + 1] = hiddenLayers[i];
        layers[layers.length - 1] = 1;

        MLP mlp = new MLP(layers, lr, func);

        Evaluator evaluator = new Evaluator(new ThresholdStrategy(0.3));
        MlpTrainer trainer = new MlpTrainer(mlp, dataset, epochs);

        if (logPath != null) {
            trainer.setLogger(logPath, evaluator);
        }

        trainer.train(shuffle);

        if (logPath == null || !logPath.startsWith("logs/")) {
            System.out.println("Fini");
            evaluator.evaluate(mlp, dataset);
        }
    }

    private static double[] getTruthTable(String gate) {
        return switch (gate) {
            case "AND" -> new double[]{0, 0, 0, 1};
            case "OR"  -> new double[]{0, 1, 1, 1};
            case "XOR" -> new double[]{0, 1, 1, 0};
            default -> throw new IllegalArgumentException("Porte inconnue");
        };
    }

    private static class ActivationConfig {
        TransferFunction func;
        RangeConversion range;
        ActivationConfig(TransferFunction f, RangeConversion r) { this.func = f; this.range = r; }
    }
}