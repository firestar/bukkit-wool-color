package com.firestar.WoolGiver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

/**
* @author Firestar
*/

public class WoolGiver extends JavaPlugin{
	protected static final Logger log = Logger.getLogger("Minecraft");
	public HashMap<String,Integer> colors = new HashMap<String,Integer>();
	public static PermissionHandler PERMISSIONS = null;
	private PluginDescriptionFile pdfFile = null;
	
	public void obtainPermissions() {
		Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
		if (test != null) {
			PERMISSIONS = ((Permissions)test).getHandler();
		} else {
			this.logMessage("Permissions plugin not enabled. Disabling plugin.");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}
	
	public boolean isAllowed(Player player){
		if(PERMISSIONS != null) {
			return PERMISSIONS.has(player, "woolgiver.spawn");
		} else return false;
	}
	
	public void logMessage(String msg){
		log.info(String.format("[%s] %s", pdfFile.getName(), msg));
	}
	
	public void giveWool(Player player, String color, Integer amount){
		try{
			Byte data;
			if (colors.containsKey(color)) {
				data = Byte.valueOf(String.valueOf(colors.get(color)));
			} else {
				data = 0;
			}
			
			ItemStack item = new ItemStack(Material.WOOL, 64, (short)0, data);
			for (int i = 0; i < amount; i++){
				player.getInventory().addItem(item);
			}
			
		} catch (Exception e) {
			this.logMessage("Exception when trying to give " + player.getName() + " " + (amount * 64) + " wool.");
		}
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player)sender;
		}
		
		if (isAllowed(player)) {
	        String commandName = command.getName().toLowerCase();
	        if (commandName.equalsIgnoreCase("gw")) {
	        	if (args.length == 1) {
	        		if (args[0].equalsIgnoreCase("list")) {
		        		String colorStr = "Wool colors: ";
		        		for(Iterator<String> iter = colors.keySet().iterator(); iter.hasNext();) {
		        			colorStr += iter.next();
		        			if (iter.hasNext()) {
		        				colorStr += ", ";
		        			}
		        		}
		        		player.sendMessage(colorStr);
	        		} else {
	        			giveWool(player, args[0], 1);
	        		}
	        	} else if(args.length == 2) {
	        		try {
	        			giveWool(player, args[0], Integer.valueOf(args[1]));
	        		} catch (NumberFormatException e) {
	        			this.logMessage("Error trying to parse wool quantity!");
	        		}
	        	}
	        	return true;
	        }
		} else {
			player.sendMessage(ChatColor.YELLOW + "Insufficient permissions!");
		}
		
		return true;
    }
	
	public void onEnable() {
		pdfFile = this.getDescription();
		this.logMessage("v" + pdfFile.getVersion() + " is enabled!");
		obtainPermissions();
		
		colors.put("white", 0);
		colors.put("orange", 1);
		colors.put("magenta", 2);
		colors.put("light_blue", 3);
		colors.put("yellow", 4);
		colors.put("lime", 5);
		colors.put("pink", 6);
		colors.put("gray", 7);
		colors.put("light_gray", 8);
		colors.put("cyan", 9);
		colors.put("purple", 10);
		colors.put("blue", 11);
		colors.put("brown", 12);
		colors.put("green", 13);
		colors.put("red", 14);
		colors.put("black", 15);
	}
	
	public void onDisable() {
		this.logMessage("v" + pdfFile.getVersion() + " is disabled!");
	}
}