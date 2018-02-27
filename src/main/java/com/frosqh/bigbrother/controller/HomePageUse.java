package com.frosqh.bigbrother.controller;

import com.frosqh.bigbrother.Main;
import com.frosqh.bigbrother.Session;
import com.frosqh.bigbrother.thread.Timer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

public class HomePageUse implements Initializable{
    @FXML
    Label  descLabel;
    @FXML
    Label  dayLabel;
    @FXML
    Label  sessionLabel;
    @FXML
    Label  hrsLabel;
    @FXML
    TextField mail;
    @FXML
    PasswordField password;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Object> selected = new ArrayList<>();
        String appDataLoc = System.getenv("APPDATA");
        String fileCLoc = appDataLoc + "\\AMD\\BigBrother\\config.cfg";
        HashMap<String, String> configMap = new HashMap();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileCLoc));
            String line;
            while ((line = bufferedReader.readLine())!=null){ //Définition de notre HashMap de config !
                if (!line.startsWith("//") && line.length()>0){
                    String[] args = line.split(" ");
                    configMap.put(args[0],args[1]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Session.set("configMap",configMap);

        if (!Timer.isStarted()) {
            Thread t = new Thread(new Timer());
            t.start();
        }




        //Init descLabel
        LocalDateTime localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DayOfWeek day = localDate.getDayOfWeek();
        String dayName = "";
        String max = "max_";
        String max_session = "max_session_";
        String forbid = "forbid_";
        switch(day){
            case MONDAY:
                dayName = "Lundi";
                max += "monday";
                max_session += "monday";
                forbid += "monday";
                break;
            case THURSDAY:
                dayName = "Jeudi";
                max += "thursday";
                max_session += "thursday";
                forbid += "thursday";
                break;
            case TUESDAY:
                dayName = "Mardi";
                max += "tuesday";
                max_session += "tuesday";
                forbid += "tuesday";
                break;
            case WEDNESDAY:
                dayName = "Mercredi";
                max += "wednesday";
                max_session += "wednesday";
                forbid += "wednesday";
                break;
            case FRIDAY:
                dayName = "Vendredi";
                max += "friday";
                max_session += "friday";
                forbid += "friday";
                break;
            case SATURDAY:
                dayName = "Samedi";
                max += "saturday";
                max_session += "saturday";
                forbid += "saturday";
                break;
            case SUNDAY:
                dayName = "Dimanche";
                max += "sunday";
                max_session += "sunday";
                forbid += "sunday";
                break;
        }
        System.out.println(dayName);
        String descText = descLabel.getText();
        descText = descText.replace("%day",dayName).replace("%hr",String.valueOf(localDate.getHour())).replace("%min",String.valueOf(localDate.getMinute()));
        descLabel.setText(descText);

        //Init config thing

        String hrsStr = hrsLabel.getText();
        String forbidSettings = configMap.get(forbid);
        if (Integer.parseInt(forbidSettings) == -1){
            forbidSettings = configMap.get("default_forbid");
        }
        hrsStr = hrsStr.replace("%hrs",forbidSettings);
        hrsLabel.setText(hrsStr);


        
        String maxSettings = configMap.get(max);
        if (Integer.parseInt(maxSettings) == -1){
            maxSettings = configMap.get("default_max_per_day");
        }


    }

    public void submit(){
        String appDataLoc = System.getenv("APPDATA");
        String fileLoc = appDataLoc + "\\AMD\\BigBrother\\data\\0x52d8\\back.pwd";

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileLoc));
            String email = bufferedReader.readLine();
            String pass = "";
            int c ;
            while ((c = bufferedReader.read()) != -1){
                pass += String.valueOf((char) (c-3));
            }

            if (email.equals(mail.getText()) && pass.equals(password.getText())){
                Stage primaryStage = (Stage) Session.get("Stage");
                Parent root = FXMLLoader.load(Main.class.getResource("view/configPane.fxml"));
                primaryStage.setTitle("BigBrother");
                primaryStage.setScene(new Scene(root, 1280, 720));
                primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("image/icon.png")));
                Session.set("stage",primaryStage);
                primaryStage.show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
