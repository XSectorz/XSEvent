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

public class XSEvent implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {

        if(e.isCancelled()) {
            return;
        }

        for(XSEventTemplate xsEventTemplate : XSEventHandler.getListEvent()) {
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
                        if(!xsEventTemplate.getScoreList().containsKey(p.getUniqueId())) {
                            XSScore xsScore = new XSScore(p);
                            xsEventTemplate.getScoreList().put(p.getUniqueId(),xsScore);
                        }

                        XSEventRequire xsEventRequire;

                        if(!xsBlokBreak.getEventRequired().containsKey("ALL")) {
                            xsEventRequire = xsBlokBreak.getEventRequired().get(b.getType().toString());
                        } else {
                            xsEventRequire = xsBlokBreak.getEventRequired().get("ALL");
                        }


                        XSScore score = xsEventTemplate.getScoreList().get(p.getUniqueId());

                        score.setScore(score.getScore()+xsEventRequire.getAdditionScore());

                        xsEventTemplate.getScoreList().replace(p.getUniqueId(),score);
                    }
                }
            }
        }
    }
}
