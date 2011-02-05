package com.firestar.wool_color;

import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import java.awt.Color;
import java.io.*;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import com.nijikokun.bukkit.Permissions.Permissions;


/**
* @author Firestar
*/

public class wool_color extends JavaPlugin{
	public static Permissions Permissions = null;
	protected static final Logger log = Logger.getLogger("Minecraft");
	public Hashtable<String,Integer> colors= new Hashtable<String,Integer>();
	public wool_color(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File  folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
        log(desc.getName() + " version " + desc.getVersion() + " initialized");
    }
	public int color_to_num(String s){
		if(colors.containsKey(s)){
			return colors.get(s);
		}
		try {
			if (s.matches("(?si)([0-9]+)")) {
				return Integer.valueOf(s);
			} else {
			} 
		} catch (PatternSyntaxException ex) {
		}
		return 0;
	}
	public void setupPermissions() {
		Plugin test = getServer().getPluginManager().getPlugin("Permissions");
		if(Permissions == null) {
		    if(test != null) {
		    	Permissions = (Permissions)test;
		    	log("Permissions plugin found, switching to its permissions");
		    } else {
		    	log("Permissions plugin not found, switching to Ops only mode.");
		    }
		}
	}
	public boolean is_allowed(Player player){
		if(Permissions != null) {
			if(Permissions.Security.permission(player, "woolcolor.spawn")){
				return true;
			}else{
				return false;
			}
		}else{
			if(player.isOp()){
				return true;
			}else{
				return false;
			}
		}
	}
	public void log(String msg){
		log.info("Wool_Color_Spawner: " + msg);
	}
	public void give_wool(Player player, String color, Integer amount){
		try{
			Byte data = (byte) Byte.valueOf(String.valueOf(color_to_num(color)));
			short f = 0;
			ItemStack item=new ItemStack(Material.WOOL,64, f, data);
			for (int i = 0; i < amount; i++){
				player.getInventory().addItem(item);
			}
			player.sendMessage("giving yourself "+(amount*64)+" "+color+" wool");
			log(player.getName()+" gave "+(amount*64)+" "+color+" wool");
		} catch (Exception e) {
		}
	}
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		Player player = null;
		if(sender instanceof Player){
			player = (Player) sender;
		}
		if(is_allowed(player)){
	        String commandName = command.getName().toLowerCase();
	        if(commandName.equalsIgnoreCase("gw")){
	        	if(args.length==1){
	        		if(args[0].equalsIgnoreCase("list")){
		        		String mi="";
		        		for ( Map.Entry<String, Integer> entry : colors.entrySet() ){
							String key = entry.getKey();
							mi=mi+key+", ";
						}
		        		player.sendMessage(mi);
	        		}else{
	        			give_wool(player,args[0],1);
	        		}
	        	}else if(args.length==2){
	        		give_wool(player,args[0],Integer.valueOf(args[1]));
	        	}else{
	        		give_wool(player,"white",1);
	        	}
	        	return true;
	        }
		}else{
			player.sendMessage(ChatColor.YELLOW+"Insufficient permissions!");
		}
        return true;
    }
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		log( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
		setupPermissions();
		colors.put("orange",1);
		colors.put("white",0);
		colors.put("magenta",2);
		colors.put("light_blue",3);
		colors.put("yellow",4);
		colors.put("lime",5);
		colors.put("pink",6);
		colors.put("gray",7);
		colors.put("light_gray",8);
		colors.put("cyan",9);
		colors.put("purple",10);
		colors.put("blue",11);
		colors.put("brown",12);
		colors.put("green",13);
		colors.put("red",14);
		colors.put("black",15);
	}
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		log( pdfFile.getName() + " version " + pdfFile.getVersion() + " disabled!" );
	}
}