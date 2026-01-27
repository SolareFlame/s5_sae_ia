package data.loaders;

import data.RangeConversion;
import data.Dataset;

public class LogicGateDataset implements Dataset {

    private final double[][] inputs;
    private final double[][] outputs;
    private final String name;

    public LogicGateDataset(String name, double[][] rawInputs, double[] rawTruth, RangeConversion range, int outputSize) {
        this.name = name;

        // les 0 deviennent -1 si on utilise tanh
        this.inputs = new double[rawInputs.length][rawInputs[0].length];
        for (int i = 0; i < rawInputs.length; i++) {
            for (int j = 0; j < rawInputs[i].length; j++) {
                this.inputs[i][j] = range.convert(rawInputs[i][j]);
            }
        }

        this.outputs = new double[rawTruth.length][outputSize];
        for (int i = 0; i < rawTruth.length; i++) {
            double val = rawTruth[i]; // 0 ou 1
            boolean isTrue = (val > 0.5);

            if (outputSize == 1) {
                this.outputs[i][0] = range.convert(val);
            } else {

                if (isTrue) {
                    this.outputs[i][0] = range.min;
                    this.outputs[i][1] = range.max;
                } else {
                    this.outputs[i][0] = range.max;
                    this.outputs[i][1] = range.min;
                }
            }
        }
    }

    @Override
    public double[][] getTrainInputs() {
        return inputs;
    }

    @Override
    public double[][] getTrainOutputs() {
        return outputs;
    }

    @Override
    public double[][] getTestInputs() {
        return inputs;
    }

    @Override
    public double[][] getTestOutputs() {
        return outputs;
    }

    @Override
    public String getName() {
        return name;
    }
}