package com.frosqh.bigbrother.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ConfigPaneController implements Initializable{
    @FXML
    GridPane grid;

    private ArrayList<String> selected;


    public void handleClick(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        Integer columnIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        String id = (rowIndex-1)+"x"+(columnIndex-1);

        if (!selected.contains(id)){
            selected.add(id);
            source.setStyle("-fx-background-color: RED;");
        } else{
            selected.remove(id);
            source.setStyle("-fx-background-color: WHITE;");
        }

        for (String s : selected){
            String[] coo = s.split("x");
            System.out.print(s+";");
        }
        System.out.println();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selected = new ArrayList<>();
        String appDataLoc = System.getenv("APPDATA");
        String fileLoc = appDataLoc + "\\AMD\\BigBrother\\config.cfg";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLoc));
            String line;
            while ((line = bufferedReader.readLine())!=null){
                line.substring(0); //Remplacer ici par le traitement du fichier config !
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
