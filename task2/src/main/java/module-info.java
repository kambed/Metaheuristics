module pl.meta.task2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens pl.meta.task2 to javafx.fxml;
    exports pl.meta.task2;
}