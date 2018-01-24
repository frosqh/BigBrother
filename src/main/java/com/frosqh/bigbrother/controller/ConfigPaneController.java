package com.frosqh.bigbrother.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.controlsfx.control.spreadsheet.Grid;

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
            System.out.print(s+";");
        }
        System.out.println();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selected = new ArrayList<>();
    }
}
