package com.frosqh.bigbrother.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomePageInstallController {
    @FXML
    TextField mail;
    @FXML
    PasswordField password;

    @FXML
    public void submit(){
        if (validate()){

        }
    }

    private boolean validate(){

        String mailString = mail.getText();
        String passwordString = password.getText();

        Pattern patternMail = Pattern.compile(".+@.+\\..+");
        Matcher matcherMail = patternMail.matcher(mailString);

        if (!matcherMail.find()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur à l'identification !");
            alert.setHeaderText(null);
            alert.setResizable(false);
            alert.setContentText("Merci de renseigner un email valide !");
            alert.show();
            return true;
        }

        Pattern patternPassword = Pattern.compile(".+");
        Matcher matcherPassword = patternPassword.matcher(passwordString);

        if (!matcherPassword.find()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur à l'identification !");
            alert.setHeaderText(null);
            alert.setResizable(false);
            alert.setContentText("Merci de renseigner un mot de passe valide !");
            alert.show();
            return false;
        }

        return true;
    }

}
