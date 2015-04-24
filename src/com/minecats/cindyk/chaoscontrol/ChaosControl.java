package com.minecats.cindyk.chaoscontrol;

import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Created by cindy on 4/16/14.
 */
public class ChaosControl extends JavaPlugin {

    PlayerControl playercontrol;
    WorldControl worldcontrol;
    TeleportControl teleportcontrol;

    @Override
    public void onDisable() {
        super.onDisable();

        this.getServer().getLogger().info("Disabled");
    }

    @Override
    public void onEnable() {
        super.onEnable();

        PlayerControl playercontrol = new PlayerControl(this);
        WorldControl worldcontrol = new WorldControl(this);

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(playercontrol, this);
        pluginManager.registerEvents(worldcontrol, this);

        this.getServer().getLogger().info("Enabled");

    }

    //Utiliy metadata functions...
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

}
