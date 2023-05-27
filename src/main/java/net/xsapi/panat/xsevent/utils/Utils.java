package net.xsapi.panat.xsevent.utils;

import net.xsapi.panat.xsevent.configuration.config;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static String replaceColor(String str) {
        return str.replace("&", "§");
    }

    public static ItemStack createItem(Material mat, int amount, int customModel, String name, ArrayList<String> lore, boolean isGlow,
                                       Map<Enchantment,Integer> enchants) {
        ItemStack itm;

        itm = new ItemStack(mat,amount);

        ItemMeta itmmeta = itm.getItemMeta();
        itmmeta.setCustomModelData(customModel);

        if (!name.isEmpty())
            itmmeta.setDisplayName(name.replace('&', '\u00A7'));
        if(lore != null) {
            if (!lore.isEmpty()) {
                ArrayList<String> lore_temp = new ArrayList<String>();

                for (String lores : lore) {
                    lores = lores.replace('&', '\u00A7');
                    lore_temp.add(lores);
                }

                itmmeta.setLore(lore_temp);
            }
        }
        if (isGlow) {
            itmmeta.addEnchant(org.bukkit.enchantments.Enchantment.DURABILITY, 1, true);
            itmmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if(!enchants.isEmpty()) {
            for(Map.Entry<Enchantment,Integer> enchant : enchants.entrySet()) {
                itmmeta.addEnchant(enchant.getKey(),enchant.getValue(),true);
            }
        }

        itmmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itm.setItemMeta(itmmeta);
        return itm;
    }

    public static ItemStack createDecoration(String type) {
        return createItem(
                Material.valueOf(config.customConfig.getString("gui.contents." + type + ".material")),
                1,
                config.customConfig.getInt("gui.contents."+ type +".modelData"),
                config.customConfig.getString("gui.contents."+ type +".name"),
                new ArrayList<>(config.customConfig.getStringList("gui.contents." + type + ".lore")),
                false,
                new HashMap<>());
    }

}