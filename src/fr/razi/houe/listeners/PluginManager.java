package fr.razi.houe.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.Sound;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.material.NetherWarts;

import fr.razi.houe.Main;

public class PluginManager implements Listener {

	private Main main;
	
	public PluginManager(Main main) {
		this.main = main;
	}
	
    @SuppressWarnings("deprecation")
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		
	    Player player = event.getPlayer();	    
	    if (player == null) return;	    
	    
	    Block brokeblock = event.getBlock();	    
	    Location blockLoc = brokeblock.getLocation();	    
	    ArrayList<Block> blocksDiametre = getBlockAutour(blockLoc);	   
	    
	    ItemStack it = player.getItemInHand();
	    if(it == null || !isRecolteuse(it)) return;
	    
	    boolean useDura = false;


	    for(Block block : blocksDiametre) {
	    	
	    	BlockState state = block.getState();
		    MaterialData data = state.getData();
		    Material type = block.getType();
		    
		    if (type == Material.NETHER_WARTS && data instanceof NetherWarts) {
		    	event.setCancelled(true);
		        NetherWarts nw = (NetherWarts) data;
		        if (nw.getState() == NetherWartsState.RIPE) {
		            block.setType(Material.NETHER_WARTS);
		            player.getInventory().addItem(new ItemStack(Material.NETHER_STALK, 1));
		            useDura = true;
		        }
		    }

		    if (data instanceof Crops) {
		        Crops crop = (Crops) data;
		        event.setCancelled(true);
		        if (crop.getState() == CropState.RIPE) {
		        	block.setType(Material.CROPS);
		            player.getInventory().addItem(new ItemStack(Material.WHEAT));  
		            useDura = true;
		        }

		    } 
		    
		    if (type == Material.CARROT || type == Material.POTATO) {
		    	event.setCancelled(true);
				byte age = block.getData();
		        if(age == 7) {
		        	if (type == Material.CARROT) {
		        		block.setType(Material.CARROT);
		                player.getInventory().addItem(new ItemStack(Material.CARROT_ITEM, 1));
		                useDura = true;
		            } else {
		            	block.setType(Material.POTATO);
		                player.getInventory().addItem(new ItemStack(Material.POTATO_ITEM, 1));
		                useDura = true;
		            }
		        }
		    }
				    
		    
		    if(useDura) {	
		    	decDura(it, player);
		    }
	    }  
	}
    
    
    @EventHandler
    public void PlayerPlowEvent(PlayerInteractEvent event) {
    	
    	Action action = event.getAction();
    	if(action != Action.RIGHT_CLICK_BLOCK) return;

    	ItemStack it = event.getPlayer().getItemInHand();
    	if(it == null) return;
    	if(!isRecolteuse(it)) return;

    	Block clickedBlock = event.getClickedBlock();
    	World world = clickedBlock.getWorld();
    	
    	ArrayList<Block> blocksArround = getBlockAutour(clickedBlock.getLocation());
	
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
 
    
    public void decDura(ItemStack it, Player player) {
    	
    	ItemMeta itM = it.getItemMeta();
    	List<String> lore = itM.getLore();
    	String duraLine = lore.get(1);
    	String dura = "";

    	for(int i = 15; i < duraLine.length(); i++) {
    		dura = dura + duraLine.charAt(i);
    	}
    	
    	int durability = Integer.parseInt(dura);
    	
    	if(durability > 0) {
    		lore.set(1, "§8Durabilité : " + (durability-1));
    		itM.setLore(lore);
    		it.setItemMeta(itM);
    	} 
    	
    	else {
    		player.setItemInHand(new ItemStack(Material.AIR));
	    	player.playSound(player.getLocation(), Sound.ITEM_BREAK, 5, 5);
    	}
    }
    
    
    public ArrayList<Block> getBlockAutour(Location blockCible){
    	
    	ArrayList<Block> blocksDiametre = new ArrayList<>();
    	
    	for(int i = -1; i<=1;i++) {
	    	for(int j = -1; j<=1;j++) {
	    		
		    	int bx = blockCible.getBlockX();
		    	int by = blockCible.getBlockY();
		    	int bz = blockCible.getBlockZ();
		    	
		    	Block block = new Location(blockCible.getWorld(),bx + i, by, bz + j).getBlock();
		    	
		    	if(block.getType() == Material.AIR || block != null) {
		    		blocksDiametre.add(block);
		    	}
		    }
	    }
    	
    	return blocksDiametre;
    }
    
    public boolean isRecolteuse(ItemStack it) {
    	if(it == null || !it.hasItemMeta()) return false;
    	if(!it.getItemMeta().hasDisplayName()) return false;
    	
    	String nomRecolteuse = main.getConfig().getString("NomDuraHoue");
    	
    	return it.getItemMeta().getDisplayName().equalsIgnoreCase(nomRecolteuse);
    }
    
}
