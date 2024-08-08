module com.example.apgraphic {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.google.gson;

    exports view;
    opens view to javafx.fxml;
    opens model to com.google.gson;
}