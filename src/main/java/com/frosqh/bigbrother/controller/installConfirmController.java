package com.frosqh.bigbrother.controller;

import com.frosqh.bigbrother.Main;
import com.frosqh.bigbrother.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class installConfirmController {
    final static Logger log = LogManager.getLogger(Main.class);

    @FXML
    TextField code;

    public void submit(ActionEvent actionEvent) {
        if (code.getText().equals(Session.get("code"))){
            System.out.println("YESSSSS ! ");
            String appDataLoc = System.getenv("APPDATA");
            String fileLoc = appDataLoc + "\\AMD\\BigBrother\\";

            File installDir = new File(fileLoc);

            installDir.mkdirs();

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
