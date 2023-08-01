package net.xsapi.panat.xsevent.events.model.blockbreak;

import net.xsapi.panat.xsevent.events.handler.XSEventHandler;
import net.xsapi.panat.xsevent.events.model.utils.XSEventRequire;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;
import net.xsapi.panat.xsevent.events.model.utils.XSEventType;
import net.xsapi.panat.xsevent.events.model.utils.XSScore;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;

public class XSEvent implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        if(e.isCancelled()) {
            return;
        }

        for(Map.Entry<String,XSEventTemplate> eventList : XSEventHandler.getListEvent().entrySet()) {
            XSEventTemplate xsEventTemplate = eventList.getValue();
            if (xsEventTemplate.getEventType().equals(XSEventType.BLOCK_BREAK)) {
                if (xsEventTemplate.isStart()) {
                    XSBlockbreak xsBlokBreak = (XSBlockbreak) xsEventTemplate;

                    Block b = e.getBlock();

                    if(xsBlokBreak.getEventRequired().containsKey(b.getType().toString())) {
                        Player p = e.getPlayer();
                        if(b.getBlockData() instanceof Ageable) {
                            Ageable ageable = (Ageable) b.getBlockData();

                            if(ageable.getAge() != ageable.getMaximumAge()) {
                                return;
                            }
                        }
                        if(!xsEventTemplate.getScoreList().containsKey(p.getUniqueId().toString())) {
                            XSScore xsScore = new XSScore(p.getName().toString());
                            xsEventTemplate.getScoreList().put(p.getUniqueId().toString(),xsScore);
                        }

                        XSEventRequire xsEventRequire;

                        if(!xsBlokBreak.getEventRequired().containsKey("ALL")) {
                            xsEventRequire = xsBlokBreak.getEventRequired().get(b.getType().toString());
                        } else {
                            xsEventRequire = xsBlokBreak.getEventRequired().get("ALL");
                        }

                        if(!xsBlokBreak.getListExcept().isEmpty()) {
                            if(xsBlokBreak.getListExcept().contains(b.getType().toString())) {
                                return;
                            }
                        }


                        XSScore score = xsEventTemplate.getScoreList().get(p.getUniqueId().toString());

                        score.setScore(score.getScore()+xsEventRequire.getAdditionScore());

                        xsEventTemplate.getScoreList().replace(p.getUniqueId().toString(),score);
                    }
                }
            }
        }
    }
}
