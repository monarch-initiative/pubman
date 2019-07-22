module pubman {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.web;
    requires org.apache.logging.log4j;
    requires guava;
    requires slf4j.api;
    requires guice;
    exports org.monarchinitiative.pubman;
    exports org.monarchinitiative.pubman.gui;
    opens org.monarchinitiative.pubman.gui to javafx.fxml;
}