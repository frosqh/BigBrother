package com.frosqh.bigbrother;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main extends Application {
    final static Logger log = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception{

        String os = System.getProperty("os.name");
        String appDataLoc;
        if (os.contains("nix") || os.contains("nux") || os.contains("aix")){
            appDataLoc = System.getProperty("user.home");
        } else {

            appDataLoc = System.getenv("APPDATA");
        }

        String fileLoc = appDataLoc + File.separator+"AMD"+File.separator+"bigbrother"+File.separator+"config.cfg";

        File installDir = new File(fileLoc);

        if (installDir.exists()){
            log.info("File already exists !");
            return;
        }
        //On effectue l'install !
        Parent root = FXMLLoader.load(getClass().getResource("view/homePageInstall.fxml"));
        primaryStage.setTitle("BigBrother");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("image/icon.png")));
        Session.set("stage",primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {
        Session.init();
        launch(args);
    }
}
