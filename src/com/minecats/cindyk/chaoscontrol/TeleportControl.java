/**
 * TeleportControl.java - Final executor for actions to take on the player once they accept the agreement
 * 
 * Created by cindy on 4/16/14.
 * Updated by JaysonBond (ongoing)
 * 
 */

package com.minecats.cindyk.chaoscontrol;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportControl extends BukkitRunnable {

    private final ChaosControl plugin;
    private final Player pp;

    public TeleportControl(ChaosControl plugin, Player pp) {
        this.plugin = plugin;
        this.pp = pp;
    }

    @Override
    public void run() {
    	if (pp.isOnline() && pp.isValid()) {
    		pp.sendMessage(plugin.getConfig().getString("stringAcceptedRules"));
    	}

    	// Check to see, and execute on, if we want to send the player to another world
        if (plugin.getConfig().getBoolean("transportPlayer") == true)
        {
        	if (isWorldInWorldList(plugin.getConfig().getString("transportPlayerToWorld")) && pp.isOnline() && pp.isValid() & !pp.isDead()) {
	            plugin.getServer().getLogger().info("[ChaosControl] Sending " + pp.getDisplayName() + " to world [" + plugin.getConfig().getString("transportPlayerToWorld") + "]");
	    		pp.teleport(plugin.getServer().getWorld(plugin.getConfig().getString("transportPlayerToWorld")).getSpawnLocation());
        	} else {
        		plugin.getServer().getLogger().info("[ChaosControl] Tried sending player to world [" + plugin.getConfig().getString("transportPlayerToWorld") + "] but it failed!");
        		plugin.getServer().getLogger().info("[ChaosControl] -- Player: " + pp.getDisplayName());
        		plugin.getServer().getLogger().info("[ChaosControl] -- isOnline? " + pp.isOnline());
        		plugin.getServer().getLogger().info("[ChaosControl] -- isValid? " + pp.isValid()); 
            	plugin.getServer().getLogger().info("[ChaosControl] -- isDead? " + pp.isDead());
        		plugin.getServer().getLogger().info("[ChaosControl] -- World: " + plugin.getConfig().getString("transportPlayerToWorld"));
        		plugin.getServer().getLogger().info("[ChaosControl] -- Valid World? " + isWorldInWorldList(plugin.getConfig().getString("transportPlayerToWorld")));
        	}
        }
    	
    	// Check to see, and execute on, if we want to change the player's game mode
        if (plugin.getConfig().getBoolean("changeGameMode") == true && pp.isOnline() && pp.isValid() && !pp.isDead())
        {
    		switch(plugin.getConfig().getString("changeGameModeTo"))
    		{
    		case "CREATIVE":
        		pp.setGameMode(GameMode.CREATIVE);
        		plugin.getServer().getLogger().info("[ChaosControl] Setting gamemode of " + pp.getDisplayName() + " to CREATIVE");
        		break;
    		case "SURVIVAL":
        		pp.setGameMode(GameMode.SURVIVAL);
        		plugin.getServer().getLogger().info("[ChaosControl] Setting gamemode of " + pp.getDisplayName() + " to SURVIVAL");
        		break;
    		case "ADVENTURE":
        		pp.setGameMode(GameMode.ADVENTURE);
        		plugin.getServer().getLogger().info("[ChaosControl] Setting gamemode of " + pp.getDisplayName() + " to ADVENTURE");
        		break;
    		case "SPECTATOR":
        		pp.setGameMode(GameMode.SPECTATOR);
        		plugin.getServer().getLogger().info("[ChaosControl] Setting gamemode of " + pp.getDisplayName() + " to SPECTATOR");
        		break;
        	default:
                plugin.getServer().getLogger().info("[ChaosControl] Tried to set gamemode for " + pp.getDisplayName() + " but config has invalid gamemode!");
        		plugin.getServer().getLogger().info("[ChaosControl] Expecting SURVIVAL, CREATIVE , ADVENTURE or SPECTATOR but received " + plugin.getConfig().getString("changeGameModeTo"));            		
    		}
        } else {
        	plugin.getServer().getLogger().info("[ChaosControl] Tried to set gamemore for " + pp.getDisplayName() + " but something is wrong...");
        	plugin.getServer().getLogger().info("[ChaosControl] -- Player: " + pp.getDisplayName());
        	plugin.getServer().getLogger().info("[ChaosControl] -- isOnline? " + pp.isOnline());
        	plugin.getServer().getLogger().info("[ChaosControl] -- isValid? " + pp.isValid());
        	plugin.getServer().getLogger().info("[ChaosControl] -- isDead? " + pp.isDead());
        }
    }
    private boolean isWorldInWorldList(String world)
    {
    	for(World w:plugin.getServer().getWorlds())
    	{
    		if(w.getName().equalsIgnoreCase(world))
    		{
    	         return true;
    	    }
    	}
    	return false;
    }
}
