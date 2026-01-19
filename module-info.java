module mlp.project {
    requires javafx.controls;
    requires javafx.fxml;

    opens app to javafx.graphics, javafx.fxml;
    exports app;
}