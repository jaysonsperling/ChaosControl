/**
 * CommandControl.java - Command processor class for user/console-entered commands
 * 
 * Created by JaysonBond (ongoing)
 */

package com.minecats.cindyk.chaoscontrol;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

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
	
		if (cmd.getName().equalsIgnoreCase("ccinfo")) 
		{
			sender.sendMessage(ChatColor.YELLOW + "ChaosControl Current Settings:");
			sender.sendMessage(ChatColor.GOLD + "Number of warnings: " + ChatColor.WHITE + plugin.getConfig().getInt("numberOfWarnings"));
			sender.sendMessage(ChatColor.GOLD + "Kick enabled? " + ChatColor.WHITE + plugin.getConfig().getBoolean("enableNotAgreedKick"));
			sender.sendMessage(ChatColor.GOLD + "Kill enabled? " + ChatColor.WHITE + plugin.getConfig().getBoolean("enableNotAgreedKill"));
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
			sender.sendMessage(ChatColor.GOLD + "Enable butchering? " + ChatColor.WHITE + plugin.getConfig().getBoolean("enableButchering"));
		    sender.sendMessage(ChatColor.GOLD + "Mobs to start butchering at: " + ChatColor.WHITE + plugin.getConfig().getInt("intButcherMobCount"));
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("ccbypass"))
		{
			sender.sendMessage("This command is not yet implimented, but you do have access to it.");
			return true;
		}
			
		if (cmd.getName().equalsIgnoreCase("ccpermissions")) {
			PermissionUser user = PermissionsEx.getUser(sender.getName());
			sender.recalculatePermissions();
			Player playerinfo = (Player) sender;
			List<String> permissions = user.getPermissions(playerinfo.getLocation().getWorld().getName().toString());
			sender.sendMessage("These are your effective permissions:");

			for(String permission:permissions) {
				sender.sendMessage(permission);
			}

			return true;
		}
		return false;
	}
}