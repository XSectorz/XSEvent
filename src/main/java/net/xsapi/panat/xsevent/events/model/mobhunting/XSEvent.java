package net.xsapi.panat.xsevent.events.model.mobhunting;

import net.xsapi.panat.xsevent.events.handler.XSEventHandler;
import net.xsapi.panat.xsevent.events.model.utils.XSEventRequire;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;
import net.xsapi.panat.xsevent.events.model.utils.XSEventType;
import net.xsapi.panat.xsevent.events.model.utils.XSScore;
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
                            if(xsMobHunting.getEventRequired().containsKey(entity.getType().toString())
                                    || xsMobHunting.getEventRequired().containsKey("ALL")) {

                                Player p = (Player) ((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager();

                                if(!xsEventTemplate.getScoreList().containsKey(p.getUniqueId())) {
                                    XSScore xsScore = new XSScore(p);
                                    xsEventTemplate.getScoreList().put(p.getUniqueId(),xsScore);
                                }

                                XSScore score = xsEventTemplate.getScoreList().get(p.getUniqueId());

                                XSEventRequire xsEventRequire;

                                if(!xsMobHunting.getEventRequired().containsKey("ALL")) {
                                    xsEventRequire = xsMobHunting.getEventRequired().get(entity.getType().toString());
                                } else {
                                    xsEventRequire = xsMobHunting.getEventRequired().get("ALL");
                                }

                                score.setScore(score.getScore()+xsEventRequire.getAdditionScore()
                                );

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

                        if(!xsMobHunting.getEventRequired().containsKey("ALL")
                        && !xsMobHunting.getEventRequired().containsKey(nEvent.getDamager().getType().toString())) {
                            return;
                        }

                        XSEventRequire xsEventRequire;

                        if(!xsMobHunting.getEventRequired().containsKey("ALL")) {
                            xsEventRequire = xsMobHunting.getEventRequired().get(nEvent.getDamager().getType().toString());
                        } else {
                            xsEventRequire = xsMobHunting.getEventRequired().get("ALL");
                        }


                        if(xsEventTemplate.getScoreList().containsKey(p.getUniqueId())) {
                            XSScore score = xsEventTemplate.getScoreList().get(p.getUniqueId());

                            score.setScore(score.getScore()-xsEventRequire.getRemoveScore());
                            xsEventTemplate.getScoreList().replace(p.getUniqueId(),score);
                        }
                    }

                }
            }
        }
    }

}
