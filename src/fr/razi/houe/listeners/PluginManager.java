package fr.razi.houe.listeners;

import java.util.ArrayList;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.material.NetherWarts;

import fr.razi.houe.Main;
import fr.razi.houe.utils.HoueManager;

public class PluginManager implements Listener {

	private Main main;
	
	public PluginManager(Main main) {
		this.main = main;
	}

    @SuppressWarnings("deprecation")
	@EventHandler
	public void onRecolteuseDura(BlockBreakEvent event) {
    	
	    Player player = event.getPlayer();
	    Block brokeblock = event.getBlock();    
	    Location blockLoc = brokeblock.getLocation();
	    ArrayList<Block> blocksDiametre = getBlockAutour(blockLoc);
	    ItemStack it = player.getItemInHand();
	    
	    boolean useDura = false;
	    boolean eventCancelled = false;
	    
	    if(it == null || !HoueManager.isRecolteuse(it, main.getConfig().getString("NomDuraHoue")) ) return;
	    
	    for(Block block : blocksDiametre) {
	    	
	    	BlockState state = block.getState();
		    MaterialData data = state.getData();
		    Material type = block.getType();
		    
		    if (type == Material.NETHER_WARTS && data instanceof NetherWarts) {
		    	eventCancelled = true;
		        NetherWarts nw = (NetherWarts) data;
		        
		        if (nw.getState() == NetherWartsState.RIPE) {
		            block.setType(Material.NETHER_WARTS);
		            player.getInventory().addItem(new ItemStack(Material.NETHER_STALK, 1));
		            useDura = true;
		        }
		        
		    }
		    
		    if (data instanceof Crops) {
		    	
		        Crops crop = (Crops) data;
		        eventCancelled = true;
		        
		        if (crop.getState() == CropState.RIPE) {
		        	block.setType(Material.CROPS);
		            player.getInventory().addItem(new ItemStack(Material.WHEAT));  
		            useDura = true;
		        }
		        
		    } 	
		    
		    if (type == Material.CARROT || type == Material.POTATO) {
		    	
		    	eventCancelled = true;
				byte age = block.getData();
				
		        if(age == 7) {
		        	
		        	if (type == Material.CARROT) {
		        		block.setType(Material.CARROT);
		                player.getInventory().addItem(new ItemStack(Material.CARROT_ITEM, 1));
		                useDura = true;
		            }
		        	
		        	else {
		            	block.setType(Material.POTATO);
		                player.getInventory().addItem(new ItemStack(Material.POTATO_ITEM, 1));
		                useDura = true;
		            }
		        	
		        }
		    }
		    
	    }
	    
	    event.setCancelled(eventCancelled);
	    
	    if(useDura) {	
	    	HoueManager.incLoreCompteur(it, player);
	    }	    
	}
    
    
    @EventHandler
    public void PlayerPlowEvent(PlayerInteractEvent event) {
    	Action action = event.getAction();
    	ItemStack it = event.getPlayer().getItemInHand();
    	Block clickedBlock = event.getClickedBlock();
    	World world = clickedBlock.getWorld();
    	ArrayList<Block> blocksArround = getBlockAutour(clickedBlock.getLocation());
    	
    	if(it == null) return;
    	if(!HoueManager.isRecolteuse( it, main.getConfig().getString("NomDuraHoue"))) return;
    	if(action != Action.RIGHT_CLICK_BLOCK) return;
    	
		for(Block block : blocksArround) {
			
			Block topBlock = new Location(world, block.getX(), block.getY() + 1,block.getZ()).getBlock();
			
			if(topBlock.getType() == Material.LONG_GRASS) {
				topBlock.setType(Material.AIR);
			}
			
			if(block.getType() == Material.DIRT || block.getType() == Material.GRASS) {
				block.setType(Material.SOIL);
			}
		
		}
		
    }
    
    
    public ArrayList<Block> getBlockAutour(Location blockCible){
    	
    	ArrayList<Block> blocksDiametre = new ArrayList<>();
   
    	for(int i = -1; i<=1;i++) {
	    	for(int j = -1; j<=1;j++) {  
		    	Block block = new Location(blockCible.getWorld(),blockCible.getBlockX() + i, blockCible.getBlockY(), blockCible.getBlockZ() + j).getBlock();
		    	blocksDiametre.add(block);
		    }
	    }
    	
    	return blocksDiametre;
    }
}
