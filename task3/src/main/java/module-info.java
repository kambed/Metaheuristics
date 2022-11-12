module pl.meta.task3.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.datatransfer;
    requires java.desktop;

    opens pl.meta.task3.frontend to javafx.fxml;
    exports pl.meta.task3.frontend;
}