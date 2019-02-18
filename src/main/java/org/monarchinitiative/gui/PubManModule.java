package org.monarchinitiative.gui;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import javafx.application.HostServices;
import javafx.stage.Stage;
import org.monarchinitiative.PubMan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PubManModule extends AbstractModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(PubManModule.class);

    private static final String PROPERTIES_FILE_NAME = "pubman.properties";

    private final Stage primaryStage;

    private final HostServices hostServices;


    PubManModule(Stage primaryStage, HostServices hostServices) {
        this.primaryStage = primaryStage;
        this.hostServices = hostServices;
    }

    @Override
    protected void configure() {
        bind(Stage.class)
                .annotatedWith(Names.named("primaryStage"))
                .toInstance(primaryStage);

        bind(HostServices.class)
                .toInstance(hostServices);

        bind(ExecutorService.class)
                .toInstance(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

        bind(ResourceBundle.class)
                .toInstance(ResourceBundle.getBundle(PubMan.class.getName()));

      /*  bind(OptionalResources.class)
                .asEagerSingleton();
        */
    }


    /**
     * Return {@link Properties} with paths to resources. At first, {@link File} <code>propertiesFilePath</code>
     * will be tried. If the file doesn't exist, we will fall back to the <code>hpo-case-annotator.properties</code>
     * that is bundled in the JAR file.
     *
     * @param propertiesFilePath {@link File} pointing to the <code>hpo-case-annotator.properties</code> file
     * @return application {@link Properties}
     * @throws IOException if I/O error occurs
     */
    @Provides
    @Singleton
    public Properties properties(@Named("propertiesFilePath") File propertiesFilePath) throws IOException {
        Properties properties = new Properties();
        if (propertiesFilePath.isFile()) {
            try {
                LOGGER.info("Loading app properties from {}", propertiesFilePath.getAbsolutePath());
                properties.load(new FileReader(propertiesFilePath));
            } catch (IOException e) {
                LOGGER.warn("Cannot load app properties", e);
                throw e;
            }
        } else {
            try {
                URL propertiesUrl = PubMan.class.getResource("/" + PROPERTIES_FILE_NAME);
                LOGGER.info("Loading bundled app properties {}", propertiesUrl.getPath());
                properties.load(PubMan.class.getResourceAsStream("/" + PROPERTIES_FILE_NAME));
            } catch (IOException e) {
                LOGGER.warn("Cannot load app properties", e);
                throw e;
            }
        }
        return properties;
    }


}
