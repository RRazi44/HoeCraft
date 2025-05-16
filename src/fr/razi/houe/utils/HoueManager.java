package fr.razi.houe.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HoueManager {
	
	public static boolean isRecolteuse(ItemStack it, String recolteuseEXPName) {
    	if(it == null || !it.hasItemMeta()) return false;
    	if(!it.getItemMeta().hasDisplayName()) return false;
    	return it.getItemMeta().getDisplayName().equalsIgnoreCase(recolteuseEXPName);
    }

	public static ItemStack getHoue(Material mat, String nomItem, ArrayList<String> lore) {

		ItemStack houe = new ItemStack(mat);
		ItemMeta houeM = houe.getItemMeta();
		
		houeM.setDisplayName(nomItem);
		houeM.setLore(lore);
		houe.setItemMeta(houeM);
		
		return houe;
	}
	
	public static void incLoreCompteur(ItemStack it, Player player) {
    	
        if (it == null || !it.hasItemMeta()) return;
        
        ItemMeta itM = it.getItemMeta();
        if (!itM.hasLore()) return;

        List<String> lore = itM.getLore();
        if (lore.size() < 2) return;

        String duraLine = lore.get(1);
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(duraLine);

        if (!matcher.find()) return;

        int durability = Integer.parseInt(matcher.group());
        
        if (durability > 0) {
        	int newDura = durability;
        	newDura -= 1;
        	lore.set(1, "§8Durabilité : " + newDura);
        	
            itM.setLore(lore);
            it.setItemMeta(itM);
        } 
        
        else {
            player.setItemInHand(new ItemStack(Material.AIR));
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1f, 1f);
        }
        
    }

}
