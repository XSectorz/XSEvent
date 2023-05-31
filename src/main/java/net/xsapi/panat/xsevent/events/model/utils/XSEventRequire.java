package net.xsapi.panat.xsevent.events.model.utils;

public class XSEventRequire {

    public double additionScore;
    public double removeScore;

    public XSEventRequire(double additionScore,double removeScore) {
        this.additionScore = additionScore;
        this.removeScore = removeScore;
    }

    public void setAdditionScore(double additionScore) {
        this.additionScore = additionScore;
    }

    public void setRemoveScore(double removeScore) {
        this.removeScore = removeScore;
    }

    public double getAdditionScore() {
        return additionScore;
    }

    public double getRemoveScore() {
        return removeScore;
    }
}
