package net.xsapi.panat.xsevent.events.model.customfishing;

import net.momirealms.customfishing.api.event.FishResultEvent;
import net.momirealms.customfishing.fishing.FishResult;
import net.xsapi.panat.xsevent.events.handler.XSEventHandler;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;
import net.xsapi.panat.xsevent.events.model.utils.XSEventType;
import net.xsapi.panat.xsevent.events.model.utils.XSScore;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class XSEvent implements Listener {
    @EventHandler
    public void onFish(FishResultEvent e) {

        for(Map.Entry<String,XSEventTemplate> eventList : XSEventHandler.getListEvent().entrySet()) {
            XSEventTemplate xsEventTemplate = eventList.getValue();
            if(xsEventTemplate.getEventType().equals(XSEventType.CUSTOM_FISHING)) {
                if(xsEventTemplate.isStart()) {
                    FishResult result = e.getResult();

                    if(result.equals(FishResult.FAILURE) || result.equals(FishResult.CATCH_SPECIAL_ITEM)) {
                        Player p = e.getPlayer();
                        if(!xsEventTemplate.getScoreList().containsKey(p.getUniqueId().toString())) {
                            XSScore xsScore = new XSScore(p.getName().toString());
                            xsEventTemplate.getScoreList().put(p.getUniqueId().toString(),xsScore);
                        }

                        XSScore score = xsEventTemplate.getScoreList().get(p.getUniqueId().toString());

                        if(result.equals(FishResult.FAILURE)) {
                            score.setScore(score.getScore()-3);
                        } else if(result.equals(FishResult.CATCH_SPECIAL_ITEM)) {
                            score.setScore(score.getScore()+5);
                        }
                        xsEventTemplate.getScoreList().replace(p.getUniqueId().toString(),score);
                    }
                }
            }
        }
    }
}
