module pubman {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.web;
    requires org.apache.logging.log4j;
    requires com.google.common;
    requires com.google.guice;
    requires slf4j.api;
    requires org.apache.commons.io;
    exports org.monarchinitiative.pubman;
    exports org.monarchinitiative.pubman.gui;
    opens org.monarchinitiative.pubman.gui to javafx.fxml;
}