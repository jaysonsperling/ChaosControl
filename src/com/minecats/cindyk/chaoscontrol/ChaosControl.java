/**
 * ChaosControl.java - Main class for enable/disable/load config/reload
 * 
 * Created by cindy on 4/16/14.
 * Updated by JaysonBond (ongoing)
 */

package com.minecats.cindyk.chaoscontrol;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ChaosControl extends JavaPlugin {
	
    PlayerControl playercontrol;
    WorldControl worldcontrol;
    TeleportControl teleportcontrol;
    FileConfiguration config = getConfig();
    
    @Override
    public void onDisable() {
        super.onDisable();

        this.getServer().getLogger().info("Disabled");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        
        loadConfig();
        
        PlayerControl playercontrol = new PlayerControl(this);
        WorldControl worldcontrol = new WorldControl(this);

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(playercontrol, this);
        pluginManager.registerEvents(worldcontrol, this);

        this.getCommand("ccreload").setExecutor(new CommandControl(this));
        this.getCommand("ccinfo").setExecutor(new CommandControl(this));

        this.getServer().getLogger().info("[ChaosControl] Enabled");
    }
    
	// Utility metadata functions...
    public String getMetadataString(Player player, String key, ChaosControl plugin) {
        List<MetadataValue> values = player.getMetadata(key);
        for (MetadataValue value : values) {
            if (value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName())) {
                return value.asString(); //value();
            }
        }
        return "";
    }

    public boolean getMetadata(Player player, String key, ChaosControl plugin) {
        List<MetadataValue> values = player.getMetadata(key);
        for (MetadataValue value : values) {
            if (value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName())) {
                return value.asBoolean(); //value();
            }
        }
        return false;
    }

    // Configuration functions...
    public void loadConfig() {
        this.getServer().getLogger().info("[ChaosControl] Loading config...");
        this.saveDefaultConfig();
        if (getConfig().getBoolean("verbose")==true) {
	        this.getServer().getLogger().info("[ChaosControl] *** Extra verbose? " + getConfig().getBoolean("verbose"));
	        this.getServer().getLogger().info("[ChaosControl] *** Number of warnings: " + getConfig().getInt("numberOfWarnings"));
	        this.getServer().getLogger().info("[ChaosControl] *** Kick enabled? " + getConfig().getBoolean("enableKick"));
	        this.getServer().getLogger().info("[ChaosControl] *** Rules List:");
	        for(String line : this.getConfig().getStringList("arrayMessageLines"))
	        {
	        	this.getServer().getLogger().info(" " + line);
	        }
	        this.getServer().getLogger().info("[ChaosControl] *** Required Input: " + getConfig().getString("stringTextToAccept"));
	        this.getServer().getLogger().info("[ChaosControl] *** Move player to different world? " + getConfig().getBoolean("transportPlayer"));
	        this.getServer().getLogger().info("[ChaosControl] *** World: " + getConfig().getString("transportPlayerToWorld"));
	        this.getServer().getLogger().info("[ChaosControl] *** Change player's gamemode? " + getConfig().getBoolean("changeGameMode"));
	        this.getServer().getLogger().info("[ChaosControl] *** Gamemode: " + getConfig().getString("changeGameModeTo"));
        }
        this.getServer().getLogger().info("[ChaosControl] Configuration loaded successfully!");
    }
}
