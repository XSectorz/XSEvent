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

public class XSEvent implements Listener {
    @EventHandler
    public void onFish(FishResultEvent e) {

        for(XSEventTemplate xsEventTemplate : XSEventHandler.getListEvent()) {
            if(xsEventTemplate.getEventType().equals(XSEventType.CUSTOM_FISHING)) {
                if(xsEventTemplate.isStart()) {
                    FishResult result = e.getResult();

                    if(result.equals(FishResult.FAILURE) || result.equals(FishResult.CATCH_SPECIAL_ITEM)) {
                        Player p = e.getPlayer();
                        if(!xsEventTemplate.getScoreList().containsKey(p.getUniqueId())) {
                            XSScore xsScore = new XSScore(p);
                            xsEventTemplate.getScoreList().put(p.getUniqueId(),xsScore);
                        }

                        XSScore score = xsEventTemplate.getScoreList().get(p.getUniqueId());

                        if(result.equals(FishResult.FAILURE)) {
                            score.setScore(score.getScore()-3);
                        } else if(result.equals(FishResult.CATCH_SPECIAL_ITEM)) {
                            score.setScore(score.getScore()+5);
                        }
                        xsEventTemplate.getScoreList().replace(p.getUniqueId(),score);
                    }
                }
            }
        }
    }
}
