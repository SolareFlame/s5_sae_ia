package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MLPWindow extends Application {

    private TextField txtLayers = new TextField("2,4,1");
    private TextField txtLR = new TextField("0.1");
    private ComboBox<String> comboActivation = new ComboBox<>();
    private TextArea console = new TextArea();

    // Ton instance de app.MLP (à relier)
    // private app.MLP mlp;

    @Override
    public void start(Stage stage) {
        stage.setTitle("app.MLP Neural Network Designer");

        // --- PANNEAU DE CONFIGURATION (GAUCHE) ---
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: #2c3e50;");

        Label title = new Label("CONFIGURATION");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("System", FontWeight.BOLD, 14));

        comboActivation.getItems().addAll("Sigmoïde", "Tangente Hyperbolique");
        comboActivation.getSelectionModel().selectFirst();
        comboActivation.setMaxWidth(Double.MAX_VALUE);

        Button btnInit = new Button("Réinitialiser app.MLP");
        btnInit.setMaxWidth(Double.MAX_VALUE);
        btnInit.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white;");

        Button btnTrain = new Button("Lancer Apprentissage");
        btnTrain.setMaxWidth(Double.MAX_VALUE);
        btnTrain.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

        Button btnTest = new Button("Tester Modèle");
        btnTest.setMaxWidth(Double.MAX_VALUE);
        btnTest.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;");

        sidebar.getChildren().addAll(
                title,
                new LabelGui("Architecture (ex: 2,4,1)", txtLayers),
                new LabelGui("Learning Rate", txtLR),
                new LabelGui("Fonction de Transfert", comboActivation),
                new Separator(),
                btnInit, btnTrain, btnTest
        );

        // --- ZONE DE LOGS (DROITE) ---
        VBox mainContent = new VBox(10);
        mainContent.setPadding(new Insets(20));
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        Label logLabel = new Label("Console de sortie / Résultats");
        logLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));

        console.setEditable(false);
        console.setFont(Font.font("Consolas", 12));
        console.setStyle("-fx-control-inner-background: #ecf0f1;");
        VBox.setVgrow(console, Priority.ALWAYS);

        mainContent.getChildren().addAll(logLabel, console);

        // --- LAYOUT GLOBAL ---
        HBox root = new HBox(sidebar, mainContent);
        Scene scene = new Scene(root, 900, 600);

        stage.setScene(scene);
        stage.show();

        // --- ACTIONS ---
        btnInit.setOnAction(e -> log("app.MLP réinitialisé avec " + txtLayers.getText()));
        btnTrain.setOnAction(e -> log("Apprentissage en cours..."));
        btnTest.setOnAction(e -> log("Test sur les données XOR : [0,1] -> Résultat attendu 1"));
    }

    private void log(String msg) {
        console.appendText("[LOG] " + msg + "\n");
    }

    // Petite classe interne pour gérer les labels proprement
    private class LabelGui extends VBox {
        public LabelGui(String labelText, javafx.scene.Node node) {
            super(5);
            Label l = new Label(labelText);
            l.setTextFill(Color.LIGHTGRAY);
            this.getChildren().addAll(l, node);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}