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
			getConfig().set("durability", 2500);
			
			saveConfig();
			
		}
	}
	
	@Override
	public void onDisable() {
	
	}
	
}
