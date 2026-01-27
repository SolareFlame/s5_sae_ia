package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvLogger {
    private final BufferedWriter writer;

    public CsvLogger(String filePath, String headerLine) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(headerLine);
        writer.newLine();
        writer.flush();
    }

    public void logStats(int epoch, double trainLoss, double testLoss, double trainAcc, double testAcc) {
        try {
            String line = epoch + "," + trainLoss + "," + testLoss + "," + trainAcc + "," + testAcc;
            writer.write(line);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (writer != null) writer.close();
        } catch (IOException e) {
        }
    }
}