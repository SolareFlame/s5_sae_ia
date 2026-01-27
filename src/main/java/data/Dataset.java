package data;

public interface Dataset {

    double[][] getTrainInputs();

    double[][] getTrainOutputs();

    double[][] getTestInputs();

    double[][] getTestOutputs();

    String getName();
}