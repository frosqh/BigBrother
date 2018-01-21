package com.frosqh.bigbrother.controller;

import com.frosqh.bigbrother.Main;
import com.frosqh.bigbrother.SendMail;
import com.frosqh.bigbrother.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.core.util.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomePageInstallController {
    @FXML
    TextField mail;
    @FXML
    PasswordField password;

    private int codeLength = 5;

    @FXML
    public void submit() throws IOException {
        if (validate()){
            String code = generateCode();
            Session.set("mail",mail.getText());
            Session.set("code",code);
            String content = generateContent(code);
            SendMail.send((String) Session.get("mail"),content,"Validation de votre compte BigBrother", true);

            Parent root = FXMLLoader.load(Main.class.getResource("view/installConfirm.fxml"));
            Stage primaryStage;
            primaryStage = (Stage) Session.get("stage");
            primaryStage.setScene(new Scene(root, 1280, 720));
            primaryStage.show();

        }
    }

    private String generateContent(String code) {
        String fileURL = String.valueOf(Main.class.getResource("html/mail.html"));
        URL url = Main.class.getResource("html/mail.html");
        System.out.println(String.valueOf(url).substring(6));
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(String.valueOf(url).substring(6)));
            String htmlContent = new String(encoded, Charset.defaultCharset());
            htmlContent = htmlContent.replace("codeToFill",code);
            htmlContent = htmlContent.replace("address", ((String) Session.get("mail")).substring(0,((String) Session.get("mail")).lastIndexOf("@")));
            return htmlContent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }

    private String generateCode() {
        String alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String code = "";
        Random r = new Random();
        for (int i =0; i<codeLength ;i++){
            code += alphabet.charAt(r.nextInt(alphabet.length()));
        }
        return code;
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
