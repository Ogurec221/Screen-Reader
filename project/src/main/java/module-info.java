module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires jnativehook;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires java.desktop;

    opens com.example.project to javafx.fxml;
    exports com.example.project;
}