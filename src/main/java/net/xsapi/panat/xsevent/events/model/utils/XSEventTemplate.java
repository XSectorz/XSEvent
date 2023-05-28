package net.xsapi.panat.xsevent.events.model.utils;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;

public class XSEventTemplate {

    public String IDKey;

    public Material iconMaterial;
    public int iconmodelData;
    public String iconName;
    public ArrayList<String> iconLore = new ArrayList<>();
    public boolean iconGlowActivate;
    public HashMap<String,XSTimer> timers = new HashMap<>();

    public XSEventType eventType;
    public String eventDate;
    public ArrayList<String> eventDateData = new ArrayList<>();


    /* String */
    public String dateFormat;
    public HashMap<String,String> timerFormat = new HashMap<>();


    public void setTimerFormat(HashMap<String, String> timerFormat) {
        this.timerFormat = timerFormat;
    }

    public HashMap<String, String> getTimerFormat() {
        return timerFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public ArrayList<String> getEventDateData() {
        return eventDateData;
    }

    public void setEventDateData(ArrayList<String> eventDateData) {
        this.eventDateData = eventDateData;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getIDKey() {
        return IDKey;
    }

    public void setIDKey(String IDKey) {
        this.IDKey = IDKey;
    }

    public void setEventType(XSEventType eventType) {
        this.eventType = eventType;
    }

    public XSEventType getEventType() {
        return eventType;
    }

    public void setIconGlowActivate(boolean iconGlowActivate) {
        this.iconGlowActivate = iconGlowActivate;
    }

    public void setIconLore(ArrayList<String> iconLore) {
        this.iconLore = iconLore;
    }

    public void setIconMaterial(Material iconMaterial) {
        this.iconMaterial = iconMaterial;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public void setIconModelData(int iconModelData) {
        this.iconmodelData = iconModelData;
    }

    public XSEventTrigger evtTrigger;

    public XSEventTrigger getEvtTrigger() {
        return evtTrigger;
    }

    public void setEvtTrigger(XSEventTrigger evtTrigger) {
        this.evtTrigger = evtTrigger;
    }

    public void setTimers(HashMap<String,XSTimer> timers) {
        this.timers = timers;
    }

    public HashMap<String,XSTimer> getTimers() {

        return this.timers;
    }

    public int getIconModelData() {
        return iconmodelData;
    }

    public Material getIconMaterial() {
        return iconMaterial;
    }

    public String getIconName() {
        return iconName;
    }

    public ArrayList<String> getIconLore() {
        return iconLore;
    }

    public boolean getIsIconGlowActivate() {
        return iconGlowActivate;
    }
}