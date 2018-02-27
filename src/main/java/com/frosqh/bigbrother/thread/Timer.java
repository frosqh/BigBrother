package com.frosqh.bigbrother.thread;

import com.frosqh.bigbrother.Main;
import com.frosqh.bigbrother.Session;
import jdk.internal.cmm.SystemResourcePressureImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

public class Timer implements Runnable{
    Date startDate;
    private static boolean started = false;
    private static Duration dursession = null;
    final static Logger log = LogManager.getLogger(Main.class);
    private static Duration durday = null;

    public Timer(){
        startDate = new Date();
        log.info("StartTimer");
    }

    public static boolean isStarted() {
        return started;
    }

    @Override
    public void run() {
        if (started){
            return;
        }
        started = true;
        TrayIcon trayIcon = null;
        SystemTray tray = null;
        if (SystemTray.isSupported()) {
            tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage(Main.class.getResource("image/icon.png"));
            trayIcon = new TrayIcon(image, "Tray Demo");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("BigBrother");
        }
        boolean warn30 = false;
        boolean warn10 = false;
        boolean warn5 = false;
        boolean warn30S = false;
        boolean warn10S = false;
        boolean warn5S = false;
        HashMap<String,String> configMap;
        LocalDateTime localDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DayOfWeek day = localDate.getDayOfWeek();
        String max = "max_";
        String max_session = "max_session_";
        String forbid = "forbid_";
        switch(day){
            case MONDAY:
                max += "monday";
                max_session += "monday";
                forbid += "monday";
                break;
            case THURSDAY:
                max += "thursday";
                max_session += "thursday";
                forbid += "thursday";
                break;
            case WEDNESDAY:
                max += "wednesday";
                max_session += "wednesday";
                forbid += "wednesday";
                break;
            case TUESDAY:
                max += "tuesday";
                max_session += "tuesday";
                forbid += "tuesday";
                break;
            case FRIDAY:
                max += "friday";
                max_session += "friday";
                forbid += "friday";
                break;
            case SATURDAY:
                max += "saturday";
                max_session += "saturday";
                forbid += "saturday";
                break;
            case SUNDAY:
                max += "sunday";
                max_session += "sunday";
                forbid += "sunday";
                break;
        }
        try {
            while(true ){
                synchronized (this) {
                    wait(1000);
                }

                /*
                    Récupération des Settings
                 */

                configMap = (HashMap<String, String>) Session.get("configMap");
                System.out.println(configMap);
                System.out.println(max);
                int maxSetting = Integer.parseInt(configMap.get(max));
                if (maxSetting == -1){
                    maxSetting = Integer.parseInt(configMap.get("default_max_per_day"));
                }
                int maxSessionSetting = Integer.parseInt(configMap.get(max_session));
                if (maxSessionSetting == -1){
                    maxSessionSetting = Integer.parseInt(configMap.get("default_max_session"));
                }
                String forbidSetting = configMap.get(forbid);
                if (Integer.parseInt(forbidSetting) == -1){
                    forbidSetting = configMap.get("default_forbid");
                }
                Date currentDate = new Date();
                LocalDateTime local = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                /*
                    Traitement des settings de session
                 */

                Duration duration = Duration.between(localDate, local);
                dursession = duration;
                if (duration.getSeconds() > maxSessionSetting*3600-30*60 && !warn30S){
                    if (SystemTray.isSupported()){
                        tray.add(trayIcon);
                        trayIcon.displayMessage("BigBrother", "/!\\ Fin de la session dans 30 minutes !", TrayIcon.MessageType.WARNING);
                        warn30S = true;
                    }
                }
                if (duration.getSeconds() > maxSessionSetting*3600-10*60 && !warn10S){
                    if (SystemTray.isSupported()){
                        tray.add(trayIcon);
                        trayIcon.displayMessage("BigBrother", "/!\\ Fin de la session dans 10 minutes !", TrayIcon.MessageType.WARNING);
                        warn10S = true;
                    }
                }
                if (duration.getSeconds() > maxSessionSetting*3600-5*60 && !warn5S){
                    if (SystemTray.isSupported()){
                        tray.add(trayIcon);
                        trayIcon.displayMessage("BigBrother", "/!\\ Fin de la session dans 5 minutes !", TrayIcon.MessageType.WARNING);
                        warn5S = true;
                    }
                }
                if (duration.getSeconds() > maxSessionSetting*3600){
                    Runtime runtime = Runtime.getRuntime();
                    log.info("EndTimer");
                    //Process proc = runtime.exec("shutdown -s -t 0 -f -c \"Temps de session dépassé ! :/\"");
                    System.exit(0);
                }

                /*
                    Traitement des settings par jour
                 */

                int alreadyDone = (int) Session.get("alreadyDone");
                duration = duration.plusSeconds(alreadyDone);
                durday = duration;
                if (duration.getSeconds() > maxSetting*3600-30*60 && !warn30){
                    if (SystemTray.isSupported()){
                        tray.add(trayIcon);
                        trayIcon.displayMessage("BigBrother", "/!\\ Fin pour la journée dans 30 minutes !", TrayIcon.MessageType.WARNING);
                        warn30 = true;
                    }
                }
                if (duration.getSeconds() > maxSessionSetting*3600-10*60 && !warn10){
                    if (SystemTray.isSupported()){
                        tray.add(trayIcon);
                        trayIcon.displayMessage("BigBrother", "/!\\ Fin pour la journée dans 10 minutes !", TrayIcon.MessageType.WARNING);
                        warn10 = true;
                    }
                }
                if (duration.getSeconds() > maxSessionSetting*3600-5*60 && !warn5){
                    if (SystemTray.isSupported()){
                        tray.add(trayIcon);
                        trayIcon.displayMessage("BigBrother", "/!\\ Fin pour la journée dans 5 minutes !", TrayIcon.MessageType.WARNING);
                        warn5 = true;
                    }
                }
                if (duration.getSeconds() > maxSessionSetting*3600){
                    Runtime runtime = Runtime.getRuntime();
                    log.info("EndTimer");
                    //Process proc = runtime.exec("shutdown -s -t 0 -f -c \"Temps de journée dépassé ! :/\"");
                    System.exit(0);
                }

                /*
                    Traitement des horaires interdits !
                 */

                String[] forbids = forbidSetting.split(";");
                for (String s : forbids){
                    if (Integer.parseInt(s) == local.getHour()){
                        Runtime runtime = Runtime.getRuntime();
                        log.info("EndTimer");
                        //Process proc = runtime.exec("shutdown -s -t 0 -f -c \"Horaire interdite ! :/\"");
                        System.exit(0);
                    }
                }



            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
