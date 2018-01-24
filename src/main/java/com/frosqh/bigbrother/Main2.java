package com.frosqh.bigbrother;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;


public class Main2 extends Application {
    final static Logger log = LogManager.getLogger(Main2.class);

    @Override
    public void start(Stage primaryStage) throws Exception{

        /*String appDataLoc = System.getenv("APPDATA");
        String fileLoc = appDataLoc + "\\AMD\\BigBrother\\config.cfg";
        File installDir = new File(fileLoc);

        if (installDir.exists()){
            log.info("File already exists !");
            return;
        }*/
        //On effectue l'install !
        Parent root = FXMLLoader.load(getClass().getResource("view/configPane.fxml"));
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
