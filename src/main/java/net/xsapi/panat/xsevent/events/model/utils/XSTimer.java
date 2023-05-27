package net.xsapi.panat.xsevent.events.model.utils;

public class XSTimer {

    public String startTimer; //Format = "xx:xx:xx"
    public int timeToAlive; //In Secs

    public XSTimer(String startTimer,int timeToAlive) {
        this.startTimer = startTimer;
        this.timeToAlive = timeToAlive;
    }

    public String getStartTimer() {
        return this.startTimer;
    }

    public int getTimeToAlive() {
        return this.timeToAlive;
    }

}