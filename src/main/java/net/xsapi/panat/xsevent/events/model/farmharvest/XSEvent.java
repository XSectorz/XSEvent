package net.xsapi.panat.xsevent.events.model.farmharvest;

import net.xsapi.panat.xsevent.events.handler.XSEventHandler;
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
        for(XSEventTemplate xsEventTemplate : XSEventHandler.getListEvent()) {
            if (xsEventTemplate.getEventType().equals(XSEventType.FARM_HARVEST)) {
                if (xsEventTemplate.isStart()) {
                    XSFarmHarvest xsFarmHarvest = (XSFarmHarvest) xsEventTemplate;

                    Block b = e.getBlock();

                    if(xsFarmHarvest.getEventRequired().contains(b.getType().toString())) {
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

                        XSScore score = xsEventTemplate.getScoreList().get(p.getUniqueId());

                        score.setScore(score.getScore()+0.5);

                        xsEventTemplate.getScoreList().replace(p.getUniqueId(),score);
                    }
                }
            }
        }
    }
}
