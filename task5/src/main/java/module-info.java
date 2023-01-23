module pl.meta.task5.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.datatransfer;
    requires java.desktop;
    requires jfreechart;

    opens pl.meta.task5.frontend to javafx.fxml;
    exports pl.meta.task5.frontend;
}