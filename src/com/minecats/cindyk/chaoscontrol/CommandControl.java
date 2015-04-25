package com.minecats.cindyk.chaoscontrol;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandControl implements CommandExecutor {
    ChaosControl plugin;

    CommandControl(ChaosControl plugin)
    {
        this.plugin = plugin;
    }
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ccreload")) {
	        plugin.getServer().getLogger().info("!!! Reloading the config file!");
	        sender.sendMessage("Reloading config file!");
			plugin.reloadConfig();
			plugin.saveDefaultConfig();
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("ccinfo")) {
			sender.sendMessage(ChatColor.YELLOW + "ChaosControl Current Settings:");
			sender.sendMessage(ChatColor.GOLD + "Number of warnings: " + ChatColor.WHITE + plugin.getConfig().getInt("numberOfWarnings"));
			sender.sendMessage(ChatColor.GOLD + "Kick enabled? " + ChatColor.WHITE + plugin.getConfig().getBoolean("enableKick"));
			sender.sendMessage(ChatColor.GOLD + "Required response:");
			sender.sendMessage(ChatColor.WHITE + plugin.getConfig().getString("stringTextToAccept"));
			sender.sendMessage(ChatColor.GOLD + "Rules:");
			for(String line : plugin.getConfig().getStringList("arrayMessageLines"))
			{
				sender.sendMessage(ChatColor.WHITE + line);
			}
			sender.sendMessage(ChatColor.GOLD + "Move player to different world? " + ChatColor.WHITE + plugin.getConfig().getBoolean("transportPlayer"));
			sender.sendMessage(ChatColor.GOLD + "World: " + ChatColor.WHITE + plugin.getConfig().getString("transportPlayerToWorld"));
			sender.sendMessage(ChatColor.GOLD + "Change player's gamemode? " + ChatColor.WHITE + plugin.getConfig().getBoolean("changeGameMode"));
			sender.sendMessage(ChatColor.GOLD + "Gamemode: " + ChatColor.WHITE + plugin.getConfig().getString("changeGameModeTo"));

			return true;
		}
		return false;
	}
}