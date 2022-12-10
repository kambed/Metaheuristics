module pl.meta.task4.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.datatransfer;
    requires java.desktop;
    requires jfreechart;
    requires javaluator;

    opens pl.meta.task4.frontend to javafx.fxml;
    exports pl.meta.task4.frontend;
}