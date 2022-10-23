module pl.meta.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires jfreechart;
    requires java.datatransfer;
    requires java.desktop;

    opens pl.meta.frontend to javafx.fxml;
    exports pl.meta.frontend;
}