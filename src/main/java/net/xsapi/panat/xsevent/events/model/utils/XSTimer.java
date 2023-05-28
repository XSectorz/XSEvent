package net.xsapi.panat.xsevent.events.model.utils;

public class XSTimer {

    public String startTimer; //Format = "xx:xx:xx"
    public int timeToAlive; //In Secs

    public String endTimer; //Format = "xx:xx:xx"

    public XSTimer(String startTimer,int timeToAlive,String endTimer) {
        this.startTimer = startTimer;
        this.timeToAlive = timeToAlive;
        this.endTimer = endTimer;
    }

    public String getStartTimer() {
        return this.startTimer;
    }

    public int getTimeToAlive() {
        return this.timeToAlive;
    }

    public String getEndTimer() {
        return endTimer;
    }
}