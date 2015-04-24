package com.minecats.cindyk.chaoscontrol;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by cindy on 4/20/14.
 */
public class TeleportControl extends BukkitRunnable {

    private final ChaosControl plugin;
    private final Player pp;

    public TeleportControl(ChaosControl plugin, Player pp) {
        this.plugin = plugin;
        this.pp = pp;

    }

    @Override
    public void run() {
        // What you want to schedule goes here
        //plugin.getServer().broadcastMessage("Welcome to Bukkit! Remember to read the documentation!");

        pp.teleport(plugin.getServer().getWorld("world").getSpawnLocation());
        pp.setGameMode(GameMode.CREATIVE);


    }
}
