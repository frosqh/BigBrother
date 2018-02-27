package com.frosqh.bigbrother;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

public class Session {
    private static HashMap<String,Object> map;
    private static boolean logExists;
    private static String lastEncounteredEnd = null;
    private static String lastEncounteredStart = null;

    public static void init(){
        map = new HashMap<String,Object>();
        if (logExists){
            String[] lineSplitted = lastEncounteredEnd.split(" ");
            String date = lineSplitted[0];
            String hours = lineSplitted[1];
            String[] dateSplitted = date.split("-");
            String[] hourSplitted = hours.split(":");
            int year = Integer.parseInt(dateSplitted[0]);
            int month = Integer.parseInt(dateSplitted[1]);
            int day = Integer.parseInt(dateSplitted[2]);
            int hour = Integer.parseInt(hourSplitted[0]);
            int minute = Integer.parseInt(hourSplitted[1]);
            LocalDateTime dateTimeEnd = LocalDateTime.of(year,month,day,hour,minute);

            lineSplitted = lastEncounteredStart.split(" ");
            date = lineSplitted[0];
            hours = lineSplitted[1];
            dateSplitted = date.split("-");
            hourSplitted = hours.split(":");
            year = Integer.parseInt(dateSplitted[0]);
            month = Integer.parseInt(dateSplitted[1]);
            day = Integer.parseInt(dateSplitted[2]);
            hour = Integer.parseInt(hourSplitted[0]);
            minute = Integer.parseInt(hourSplitted[1]);
            LocalDateTime dateTimeStart = LocalDateTime.of(year,month,day,hour,minute);

            LocalDateTime now = LocalDateTime.now();

            Duration duration = Duration.between(dateTimeEnd,dateTimeStart);
            if (now.getYear() > dateTimeEnd.getYear() || now.getMonth().getValue() > dateTimeEnd.getMonth().getValue() || now.getDayOfMonth() > dateTimeEnd.getDayOfMonth()){
                set("alreadyDone",0);
            } else {
                set("alreadyDone",duration.getSeconds());
            }

        } else {
            set("alreadyDone",0);
        }
    }

    public static void set(String key, Object value){
        if (map.containsKey(key)){
            map.replace(key,value);
        } else {
            map.put(key, value);
        }
    }

    public static Object get(String key){
        return map.get(key);
    }

    public static boolean checkLogs() {
        String appDataLoc = System.getenv("APPDATA");
        String fileLoc = appDataLoc + "\\AMD\\BigBrother\\logs\\bigBrother.log";
        boolean startTimeEncountered = false;
        try {
            if (!(new File(fileLoc).exists())){
                logExists = false;
                System.err.println("Log file does not exist :(");
                return true;
            }
            logExists = true;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileLoc));
            String line;
            System.out.println("Coucou ! ");
            while ((line = bufferedReader.readLine()) != null){
                System.out.println(line);
                if (line.contains("StartTimer")) {
                    if (!startTimeEncountered) {
                        startTimeEncountered = true;
                        lastEncounteredStart = line;
                    } else {
                        return false;
                    }
                }
                if (line.contains("EndTimer")) {
                    if (startTimeEncountered) {
                        lastEncounteredEnd =  line;
                        startTimeEncountered = false;
                    } else {
                        return false;
                    }
                }
            }

            /*System.out.println(lastEncounteredEnd);
            System.out.println(lastEncounteredStart);
            System.out.println(startTimeEncountered);*/

            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return !startTimeEncountered;
    }

    public static void deleteLogs() {
        String appDataLoc = System.getenv("APPDATA");
        String fileLoc = appDataLoc + "\\AMD\\BigBrother\\logs\\bigBrother.log";
        new File(fileLoc).delete();
        logExists = false;
    }
}
