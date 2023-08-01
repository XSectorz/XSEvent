package net.xsapi.panat.xsevent.events.model.mobhunting;

import net.xsapi.panat.xsevent.events.handler.XSEventHandler;
import net.xsapi.panat.xsevent.events.model.utils.XSEventRequire;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;
import net.xsapi.panat.xsevent.events.model.utils.XSEventType;
import net.xsapi.panat.xsevent.events.model.utils.XSScore;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Map;

public class XSEvent implements Listener {

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        for(Map.Entry<String,XSEventTemplate> eventList : XSEventHandler.getListEvent().entrySet()) {
            XSEventTemplate xsEventTemplate = eventList.getValue();
            if (xsEventTemplate.getEventType().equals(XSEventType.MOB_HUNTING)) {
                if(xsEventTemplate.isStart()) {

                    Entity entity = e.getEntity();
                    if(entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                        XSMobHunting xsMobHunting = (XSMobHunting) xsEventTemplate;
                        if(((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager() instanceof Player) {
                            if(xsMobHunting.getEventRequired().containsKey(entity.getType().toString())
                                    || xsMobHunting.getEventRequired().containsKey("ALL")) {

                                Player p = (Player) ((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager();

                                if(!xsEventTemplate.getScoreList().containsKey(p.getUniqueId().toString())) {
                                    XSScore xsScore = new XSScore(p.getName().toString());
                                    xsEventTemplate.getScoreList().put(p.getUniqueId().toString(),xsScore);
                                }

                                XSScore score = xsEventTemplate.getScoreList().get(p.getUniqueId().toString());

                                XSEventRequire xsEventRequire;

                                if(!xsMobHunting.getEventRequired().containsKey("ALL")) {
                                    xsEventRequire = xsMobHunting.getEventRequired().get(entity.getType().toString());
                                } else {
                                    xsEventRequire = xsMobHunting.getEventRequired().get("ALL");
                                }

                                if(!xsMobHunting.getListExcept().isEmpty()) {
                                    if(xsMobHunting.getListExcept().contains(entity.getType().toString())) {
                                        return;
                                    }
                                }

                                score.setScore(score.getScore()+xsEventRequire.getAdditionScore()
                                );

                                xsEventTemplate.getScoreList().replace(p.getUniqueId().toString(),score);

                            }
                        }
                    }

                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        for(Map.Entry<String,XSEventTemplate> eventList : XSEventHandler.getListEvent().entrySet()) {
            XSEventTemplate xsEventTemplate = eventList.getValue();
            if (xsEventTemplate.getEventType().equals(XSEventType.MOB_HUNTING)) {
                if (xsEventTemplate.isStart()) {

                    Player p = e.getEntity();
                    XSMobHunting xsMobHunting = (XSMobHunting) xsEventTemplate;

                    String key = "";
                    if (e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {

                        EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();

                        key = nEvent.getDamager().getType().toString();

                        if(!xsMobHunting.getEventRequired().containsKey("ALL")
                        && !xsMobHunting.getEventRequired().containsKey(nEvent.getDamager().getType().toString())) {

                            if(nEvent.getDamager() instanceof Arrow) {
                                Arrow arrow = (Arrow) nEvent.getDamager();

                                if(arrow.getShooter() instanceof Skeleton) {
                                    if(!xsMobHunting.getEventRequired().containsKey("SKELETON")) {
                                        return;
                                    }
                                    key = "SKELETON";
                                }
                            } else {
                                return;
                            }
                        }
                    }

                    XSEventRequire xsEventRequire;

                    if(!xsMobHunting.getEventRequired().containsKey("ALL")) {
                        xsEventRequire = xsMobHunting.getEventRequired().get(key);
                    } else {
                        key = "ALL";
                        xsEventRequire = xsMobHunting.getEventRequired().get("ALL");
                    }

                    if(!xsMobHunting.getListExcept().isEmpty()) {
                        if(xsMobHunting.getListExcept().contains(key)) {
                            return;
                        }
                    }

                    if(xsEventTemplate.getScoreList().containsKey(p.getUniqueId().toString())) {
                        XSScore score = xsEventTemplate.getScoreList().get(p.getUniqueId().toString());

                        score.setScore(score.getScore()-xsEventRequire.getRemoveScore());
                        xsEventTemplate.getScoreList().replace(p.getUniqueId().toString(),score);
                    }

                }
            }
        }
    }

}
