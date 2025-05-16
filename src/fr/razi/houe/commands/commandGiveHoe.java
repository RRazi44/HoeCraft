package fr.razi.houe.commands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.razi.houe.Main;
import fr.razi.houe.utils.HoueManager;

public class commandGiveHoe implements CommandExecutor {

	private Main main;
	
	public commandGiveHoe(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {	
			
			Player player = (Player) sender;				
			ItemStack houe = null;
			
			if(args[0].equalsIgnoreCase("dura")) {
				
				String nomItem = main.getConfig().getString("NomDuraHoue");;
				int maxDurability = main.getConfig().getInt("durability");
				ArrayList<String> lore = new ArrayList<String>();

				lore.add("§8--------------------");
				lore.add("§8Durabilité : " + maxDurability);
				lore.add("§8--------------------");
				
				houe = HoueManager.getHoue(Material.GOLD_HOE, nomItem, lore);

			}
				
			player.getInventory().addItem(houe);
			
		}	
			
		return false;
		
	}	
}
