package net.xsapi.panat.xsevent.events.model.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class XSRewards {

    public HashMap<Integer, ArrayList<String >> rewardsList = new HashMap<>();

    public ArrayList<String> participantsRewards = new ArrayList<>();

    public ArrayList<String> getParticipantsRewards() {
        return participantsRewards;
    }

    public void setParticipantsRewards(ArrayList<String> participantsRewards) {
        this.participantsRewards = participantsRewards;
    }

    public HashMap<Integer, ArrayList<String>> getRewardsList() {
        return rewardsList;
    }

}
