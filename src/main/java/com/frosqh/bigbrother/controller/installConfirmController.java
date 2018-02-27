package com.frosqh.bigbrother.controller;

import com.frosqh.bigbrother.Main;
import com.frosqh.bigbrother.Session;
import com.frosqh.bigbrother.thread.Timer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class installConfirmController {
    final static Logger log = LogManager.getLogger(Main.class);

    public final static String initConfig = "//Default parameters\n" +
            "default_max_per_day 2\n" +
            "default_max_session 1\n" +
            "default_forbid 00;01;02;03;04;05;06;07;08;22;23;12;13;14\n" +
            "\n" +
            "//On startup\n" +
            "start_shown 1\n" +
            "\n" +
            "//Session per day\n" +
            "max_monday -1\n" +
            "max_session_monday -1\n" +
            "max_tuesday -1\n" +
            "max_session_tuesday -1\n" +
            "max_wednesday -1\n" +
            "max_session_wednesday -1\n" +
            "max_thursday -1\n" +
            "max_session_thursday -1\n" +
            "max_friday -1\n" +
            "max_session_friday -1\n" +
            "max_saturday -1\n" +
            "max_session_saturday -1\n" +
            "max_sunday -1\n" +
            "max_session_sunday -1\n" +
            "\n" +
            "//Forbidden hours\n" +
            "forbid_monday -1\n" +
            "forbid_tuesday -1\n" +
            "forbid_wednesday -1\n" +
            "forbid_thursday -1\n" +
            "forbid_friday -1\n" +
            "forbid_saturday -1\n" +
            "forbid_sunday -1";

    @FXML
    TextField code;

    public void submit(ActionEvent actionEvent) throws IOException {
        if (code.getText().equals(Session.get("code"))){
            String appDataLoc = System.getenv("APPDATA");
            String fileLoc = appDataLoc + "\\AMD\\BigBrother\\config.cfg";
            String dirAccount = appDataLoc + "\\AMD\\BigBrother\\data\\0x52d8\\";
            String fileAccount = appDataLoc + "\\AMD\\BigBrother\\data\\0x52d8\\back.pwd";

            File config = new File(fileLoc);
            File dirPassword = new File(dirAccount);
            File password = new File(fileAccount);

            config.createNewFile();
            dirPassword.mkdirs();
            password.createNewFile();

            BufferedWriter buffWriter = new BufferedWriter(new FileWriter(fileAccount));
            buffWriter.write((String) Session.get("mail"));
            buffWriter.write("\n");
            String pass = (String) Session.get("password");
            for (Byte b : pass.getBytes()){
                buffWriter.write(b+3);
            }
            buffWriter.close();

            buffWriter = new BufferedWriter(new FileWriter(fileLoc));
            buffWriter.write(initConfig);
            buffWriter.close();

            Parent root = FXMLLoader.load(Main.class.getResource("view/ConfigPane.fxml"));
            Stage primaryStage;
            primaryStage = (Stage) Session.get("stage");
            primaryStage.setScene(new Scene(root, 1280, 720));
            primaryStage.show();

            Thread t = new Thread(new Timer());
            t.start();

        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur à l'identification !");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("image/icon.png")));
            alert.setHeaderText(null);
            alert.setResizable(false);
            alert.setContentText("Code erroné !");
            alert.show();
        }
    }
}
