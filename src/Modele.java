public class Modele {
    private MLP mlp;
    private double[][] inputData;
    private double[][] outputData;

    public Modele(int[] layers, double learningRate, TransferFunction fonctionActivation, String dataPath) {
        this.mlp = new MLP(layers, learningRate, fonctionActivation);

        this.loadData(dataPath);
    }

    public void train(int epochs, boolean verbose) {
        for (int i = 0; i < epochs; i++) {
            double totalError = 0.0;
            for (int j = 0; j < inputData.length; j++) {
                double error = mlp.backPropagate(inputData[j], outputData[j]);
                totalError += error;
                if(verbose) {
                    System.out.println("erreur à l'appelle " + i + " et l'input " + j + " : " + error);
                }
            }
            //si apres un tour des données l'erreur est nulle on arrete l'entrainement
            if(totalError == 0.0) {
                if (verbose) {
                    System.out.println("Entraînement arrêté à l'époque " + i + " car l'erreur est nulle.");
                }
                break;
            }
        }
    }

    public double[] execute(double[] input) {
        //on verifie que la taille de l'input correspond au nombre de neurones d'entrée
        if(input.length != this.mlp.getInputLayerSize()) {
            throw new IllegalArgumentException("La taille de l'entrée ne correspond pas au nombre de neurones d'entrée.");
        }
        return mlp.execute(input);
    }

    public void loadData(String dataPath) {
        var loadData = new LoadData();
        var data = loadData.loadData(dataPath);
        this.inputData = data[0];
        this.outputData = data[1];

        //on verifie si le nombre de neurones d'entrée et de sortie correspond bien a la taille des données
        if(this.inputData[0].length != this.mlp.getInputLayerSize()) {
            throw new IllegalArgumentException("Le nombre de neurones d'entrée ne correspond pas à la taille des données d'entrée.");
        }
        if(this.outputData[0].length != this.mlp.getOutputLayerSize()) {
            throw new IllegalArgumentException("Le nombre de neurones de sortie ne correspond pas à la taille des données de sortie.");
        }
    }

    public double[] evaluate(double precision) {
        //on évalue le modèle sur les données chargées
        //avec un certain niveau de précision
        int correct = 0, incorrect = 0, total = 0;
        for (int i = 0; i < inputData.length; i++) {
            total++;
            double[] output = mlp.execute(inputData[i]);

            if(Math.abs(output[0] - outputData[i][0]) <= precision) {
                correct++;
            } else {
                incorrect++;
            }
        }

        double[] results = new double[3];
        results[0] = correct;
        results[1] = incorrect;
        results[2] = total;
        return results;
    }
}
