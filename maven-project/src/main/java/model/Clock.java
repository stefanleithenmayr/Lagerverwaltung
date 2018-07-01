package model;

import java.util.Calendar;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Clock extends Observable {

    private static Clock INSTANCE;

    private Clock() {
    }

    public void triggerObserver() {
        Runnable timeTask = () -> {
            Calendar time = Calendar.getInstance();
            int hour = time.get(Calendar.HOUR_OF_DAY);
            int min = time.get(Calendar.MINUTE);
            int sec = time.get(Calendar.SECOND);
            this.notifyObservers(String.format("%02d:%02d:%02d\n", hour, min, sec));
            this.setChanged();
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(timeTask, 0, 1, TimeUnit.SECONDS);
    }

    public static Clock getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Clock();
        }
        return INSTANCE;
    }
}