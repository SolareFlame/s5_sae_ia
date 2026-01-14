public class Sigmoide implements TransferFunction {
    @Override
    public double evaluate(double value) {
        return 1.0 / (1.0 + Math.exp(-value));
    }

    @Override
    public double evaluateDer(double value) {
        return value * (1.0 - value);
    }
}
