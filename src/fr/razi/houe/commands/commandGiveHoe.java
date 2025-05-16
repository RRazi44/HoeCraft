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
				
				if(args.length == 1) {
					
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
					
					else if(args[0].equalsIgnoreCase("experience")) {
						
						String nomItem = main.getConfig().getString("NomExpHoue");
						ArrayList<String> lore = new ArrayList<String>();
						int expToLvlOne = main.getConfig().getInt("ExpRequisPourLvl2");
						
						lore.add("§8--------------------");
						lore.add("§8Experience: [0/" + expToLvlOne + "]");
						lore.add("§8--------------------");
						
						houe = HoueManager.getHoue(Material.WOOD_HOE, nomItem, lore);
						
					}
					
					if(houe != null) {
						player.getInventory().addItem(houe);
					}
					
				}
				
				else {
					player.sendMessage("§cUsage: /givehoue <dura/experience>");
				}

			}
		
		return false;
	}
	
}
