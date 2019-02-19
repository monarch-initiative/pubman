package org.monarchinitiative;

import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class PubMan  extends Application {

    private static final String WINDOW_TITLE = "PubMan";

    private Injector injector;

        @Override
        public void start(Stage stage) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/mainview.fxml"));
            stage.setTitle("PubMan");
            stage.setScene(new Scene(root, 800, 700));
            stage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }

    }