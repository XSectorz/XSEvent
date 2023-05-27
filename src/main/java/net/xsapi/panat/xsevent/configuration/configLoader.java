package net.xsapi.panat.xsevent.configuration;

public class configLoader {

    public configLoader() {
        new config().loadConfigu();
        new xsevent().loadConfigu();
        new messages().loadConfigu();
    }

}