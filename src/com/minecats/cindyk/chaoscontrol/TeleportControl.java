/**
 * TeleportControl.java - Final executor for actions to take on the player once they accept the agreement
 * 
 * Created by cindy on 4/16/14.
 * Updated by JaysonBond (ongoing)
 */

package com.minecats.cindyk.chaoscontrol;

import org.bukkit.GameMode;
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
        	// If I could figure out how to test for worlds w/ all the different plugins, I'd put validation here...
            plugin.getServer().getLogger().info("[ChaosControl] Sending " + pp.getDisplayName() + " to world [" + plugin.getConfig().getString("transportPlayerToWorld") + "]");
    		pp.teleport(plugin.getServer().getWorld(plugin.getConfig().getString("transportPlayerToWorld")).getSpawnLocation());
        }
    	
    	// Check to see, and execute on, if we want to change the player's game mode
        if (plugin.getConfig().getBoolean("changeGameMode") == true)
        {
    		switch(plugin.getConfig().getString("changeGameModeTo"))
    		{
    		case "CREATIVE":
        		pp.setGameMode(GameMode.CREATIVE);
        		plugin.getServer().getLogger().info("[ChaosControl] Setting gamemode of " + pp.getCustomName() + " to CREATIVE");
        		break;
    		case "SURVIVAL":
        		pp.setGameMode(GameMode.SURVIVAL);
        		plugin.getServer().getLogger().info("[ChaosControl] Setting gamemode of " + pp.getCustomName() + " to SURVIVAL");
        		break;
    		case "ADVENTURE":
        		pp.setGameMode(GameMode.ADVENTURE);
        		plugin.getServer().getLogger().info("[ChaosControl] Setting gamemode of " + pp.getCustomName() + " to ADVENTURE");
        		break;
    		case "SPECTATOR":
        		pp.setGameMode(GameMode.SPECTATOR);
        		plugin.getServer().getLogger().info("[ChaosControl] Setting gamemode of " + pp.getCustomName() + " to SPECTATOR");
        		break;
        	default:
                plugin.getServer().getLogger().info("[ChaosControl] Tried to set gamemode for " + pp.getDisplayName() + " but config has invalid gamemode!");
        		plugin.getServer().getLogger().info("[ChaosControl] Expecting SURVIVAL, CREATIVE , ADVENTURE or SPECTATOR but received " + plugin.getConfig().getString("changeGameModeTo"));            		
    		}
        }
    }
}
