package fr.razi.houe;

import org.bukkit.plugin.java.JavaPlugin;

import fr.razi.houe.commands.commandGiveHoe;
import fr.razi.houe.listeners.PluginManager;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		
		getServer().getPluginManager().registerEvents(new PluginManager(this), this);
		getCommand("givehoue").setExecutor(new commandGiveHoe(this));
		
		if(getConfig().getString("NomDuraHoue") == null) {
			
			getConfig().set("NomDuraHoue", "§b§lRécolteuse de plantation");
			getConfig().set("NomExpHoue", "§b§lMawaks Récolteuse");
			getConfig().set("durability", 2500);
			getConfig().set("ExpRequisPourLvl2", 200000);
			getConfig().set("ExpRequisPourLvl3", 5000000);
			getConfig().set("ExpRequisPourLvl4", 20000000);
			getConfig().set("ExpRequisPourLvl5", 150000000);
			saveConfig();
			
		}
	}
	
	@Override
	public void onDisable() {
	
	}
}
