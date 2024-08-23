package com.jook.dpsmeter;

import com.jook.dpsmeter.utils.Params;
import com.jook.dpsmeter.utils.Stages;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;


public class MainFx extends Application {

    public static String ver = "2.0";
    private static final Logger logger = LogManager.getLogger(MainFx.class);

    @Override
    public void start(Stage mainStage) throws IOException {
        Stages stages = new Stages();
        stages.checkExistsFiles();
        Params.loadAll();
        stages.initRoot(mainStage);
        stages.initSett();
        stages.initSkills();
        stages.initOverlay();
        stages.initCharts();
        logger.info("initialization completed");
    }

    public static void startFx() {
        launch();
    }
}

