package net.xsapi.panat.xsevent.events.model.mobhunting;

import net.xsapi.panat.xsevent.events.handler.XSEventHandler;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;
import net.xsapi.panat.xsevent.events.model.utils.XSEventType;
import net.xsapi.panat.xsevent.events.model.utils.XSScore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class XSEvent implements Listener {

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        for(XSEventTemplate xsEventTemplate : XSEventHandler.getListEvent()) {
            if (xsEventTemplate.getEventType().equals(XSEventType.MOB_HUNTING)) {
                if(xsEventTemplate.isStart()) {

                    Entity entity = e.getEntity();
                    if(entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                        XSMobHunting xsMobHunting = (XSMobHunting) xsEventTemplate;
                        if(((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager() instanceof Player) {
                            if(xsMobHunting.getEventRequired().contains(entity.getType().toString())
                                    || xsMobHunting.getEventRequired().contains("ALL")) {

                                Player p = (Player) ((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager();

                                if(!xsEventTemplate.getScoreList().containsKey(p.getUniqueId())) {
                                    XSScore xsScore = new XSScore(p);
                                    xsEventTemplate.getScoreList().put(p.getUniqueId(),xsScore);
                                }

                                XSScore score = xsEventTemplate.getScoreList().get(p.getUniqueId());

                                score.setScore(score.getScore()+1);

                                xsEventTemplate.getScoreList().replace(p.getUniqueId(),score);

                            }
                        }
                    }

                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        for(XSEventTemplate xsEventTemplate : XSEventHandler.getListEvent()) {
            if (xsEventTemplate.getEventType().equals(XSEventType.MOB_HUNTING)) {
                if (xsEventTemplate.isStart()) {
                    if (e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {

                        EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();

                        Player p = e.getEntity();
                        XSMobHunting xsMobHunting = (XSMobHunting) xsEventTemplate;

                        if(!xsMobHunting.getEventRequired().contains("ALL")
                        && !xsMobHunting.getEventRequired().contains(nEvent.getDamager().getType().toString())) {
                            return;
                        }

                        if(xsEventTemplate.getScoreList().containsKey(p.getUniqueId())) {
                            XSScore score = xsEventTemplate.getScoreList().get(p.getUniqueId());

                            score.setScore(score.getScore()-5);
                            xsEventTemplate.getScoreList().replace(p.getUniqueId(),score);
                        }
                    }

                }
            }
        }
    }

}
