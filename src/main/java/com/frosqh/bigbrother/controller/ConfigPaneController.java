package com.frosqh.bigbrother.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ConfigPaneController implements Initializable{
    @FXML
    GridPane grid;

    private ArrayList<String> selected;
    private HashMap<String,String> configMap;
    private final String[] HOURS = new String[]{"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};

    /*
        Kékonfait quand on clique sur une case de notre GridPane ? ^^
     */
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
    }

    private int forbidFunction(String forbid, int i){
        if (forbid.equals("-1"))
            return i;
        else {
            String[] hours= forbid.split(";");
            for (String HOUR : HOURS){
                boolean isRED = vecContains(hours,HOUR);
                Node pane = getNodeFromGridPane(grid,Integer.parseInt(HOUR)+1,i);
                if (isRED){
                    pane.setStyle("-fx-background-color: RED;");
                } else {
                    pane.setStyle("-fx-background-color: WHITE;");
                }
            }
        }
        return -1;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selected = new ArrayList<>();
        String appDataLoc = System.getenv("APPDATA");
        String fileLoc = appDataLoc + "\\AMD\\BigBrother\\config.cfg";
        configMap = new HashMap();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLoc));
            String line;
            while ((line = bufferedReader.readLine())!=null){ //Définition de notre HashMap de config !
                if (!line.startsWith("//") && line.length()>0){
                    String[] args = line.split(" ");
                    configMap.put(args[0],args[1]);
                }
            }

            { //Forbid block
                String mondayForbid = configMap.get("forbid_monday");
                String tuesdayForbid = configMap.get("forbid_tuesday");
                String wednesdayForbid = configMap.get("forbid_wednesday");
                String thursdayForbid = configMap.get("forbid_thursday");
                String fridayForbid = configMap.get("forbid_friday");
                String saturdayForbid = configMap.get("forbid_saturday");
                String sundayForbid = configMap.get("forbid_sunday");
                String defaultForbid = configMap.get("default_forbid");

                ArrayList<Integer> toDefault = new ArrayList<Integer>();

                toDefault.add(forbidFunction(mondayForbid,1));
                toDefault.add(forbidFunction(tuesdayForbid,2));
                toDefault.add(forbidFunction(wednesdayForbid,3));
                toDefault.add(forbidFunction(thursdayForbid,4));
                toDefault.add(forbidFunction(fridayForbid,5));
                toDefault.add(forbidFunction(saturdayForbid,6));
                toDefault.add(forbidFunction(sundayForbid,7));

                for(Integer i : toDefault){
                    if (i!=-1)
                        forbidFunction(defaultForbid,i);
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String key : configMap.keySet()){
            System.out.println(key + " : "+configMap.get(key));
        }
    }

    private boolean vecContains(Object[] arr1, Object obj){
        for (Object o : arr1){
            if (o.equals(obj)){
                return true;
            }
        }
        return false;
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                selected.add((row-1)+"x"+(col-1));
                return node;
            }
        }
        return null;
    }

    public void save(ActionEvent actionEvent) throws IOException {
        String appDataLoc = System.getenv("APPDATA");
        String fileLoc = appDataLoc + "\\AMD\\BigBrother\\config.cfg";
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileLoc));
        for (String key : configMap.keySet()){
            bufferedWriter.write(key+" "+configMap.get(key)+"\n");
        }
        bufferedWriter.close();

    }

    public void cancel(ActionEvent actionEvent) {
        initialize(null,null);
    }

    public void setDefault(ActionEvent actionEvent) throws IOException {
        String appDataLoc = System.getenv("APPDATA");
        String fileLoc = appDataLoc + "\\AMD\\BigBrother\\config.cfg";
        BufferedWriter buffWriter = new BufferedWriter(new FileWriter(fileLoc));
        buffWriter.write(installConfirmController.initConfig);
        buffWriter.close();
        initialize(null,null);
    }

    public void ret(ActionEvent actionEvent) {

    }
}
