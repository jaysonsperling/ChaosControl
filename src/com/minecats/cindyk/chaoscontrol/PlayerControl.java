package com.minecats.cindyk.chaoscontrol;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by cindy on 4/16/14.
 */
public class PlayerControl implements Listener {

    ChaosControl plugin;
    int taskId;

    PlayerControl(ChaosControl plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    void OnJoin(PlayerJoinEvent event)
    {
        final Player p = event.getPlayer();

        if(p.isOp())
        {
            p.setMetadata("ChaosControl.confirmed",new FixedMetadataValue(plugin,true));
            p.sendMessage(plugin.getConfig().getString("stringAllowPlayerWithOp"));
            return;
        }

        if(p.hasPermission("ChaosControl.bypass"))
        {
        	p.setMetadata("ChaosControl.confirmed", new FixedMetadataValue(plugin, true));
        	p.sendMessage(plugin.getConfig().getString("stringAllowPlayerBypass"));
        	return;
        }
        
        if(p.hasPermission("ChaosControl.confirmed"))
        {
            p.setMetadata("ChaosControl.confirmed",new FixedMetadataValue(plugin,true));
            p.sendMessage(plugin.getConfig().getString("stringAllowPlayerConfirmed"));
            return;
        }

        if(p.hasMetadata("ChaosControl.confirmed"))
        {
            p.removeMetadata("ChaosControl.confirmed",plugin);
        }
        p.removeMetadata("ChaosControl.count",plugin);


        sendWarning(p);
        plugin.getServer().getLogger().info("[ChaosControl] Initial Warning sent to " + p.getDisplayName());

        taskId= plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                if(!p.hasMetadata("ChaosControl.confirmed"))
                {
                    if(p.hasMetadata("ChaosControl.count"))
                    {
                        int count = Integer.parseInt( plugin.getMetadataString(p,"ChaosControl.count",plugin));

                        if(count < plugin.getConfig().getInt("numberOfWarnings"))
                        {
                            plugin.getLogger().info("[ChaosControl] Additional warning Sent to " + p.getDisplayName() + " || Count: "+count);
                            sendWarning(p);
                            count++;
                            p.setMetadata("ChaosControl.count",new FixedMetadataValue(plugin,""+count));
                        }
                        else
                        {
                        	if (plugin.getConfig().getBoolean("enableKick")==true) {
                        		p.kickPlayer(plugin.getConfig().getString("stringKickPlayerForNotConfirming"));
                        	} else {
                        		p.setHealth(0);
                        		p.sendMessage(plugin.getConfig().getString("stringPleaseAcceptRules"));
                        	}
                        }
                    }
                    else
                    {
                        p.setMetadata("ChaosControl.count", new FixedMetadataValue(plugin, "1"));
                    }
                }
                else
                {
                    plugin.getServer().getScheduler().cancelTask(taskId);
                }
            }
        }, 0L, 1000L);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

        Player p = event.getPlayer();
        if(!p.hasMetadata("ChaosControl.confirmed"))
        {
            if(event.getMessage().compareTo(plugin.getConfig().getString("stringTextToAccept"))==0)
            {
                p.setMetadata("ChaosControl.confirmed",new FixedMetadataValue(plugin,true));
                p.teleport(plugin.getServer().getWorld("world").getSpawnLocation());
                plugin.getServer().getLogger().info("[ChaosControl] " +  p.getDisplayName() + " has accepted the rules!");
                return;
            }
            else
            {
                p.sendMessage(plugin.getConfig().getString("stringNoChatOrCommandsUntilConfirm"));
                sendWarning(p);
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    void onMove(final PlayerMoveEvent event)
    {
    	final Player p = event.getPlayer();
    	if (!p.hasMetadata("ChaosControl.confirmed"))
    	{
    		// p.sendMessage("You cannot move until you confirm your age");
    		final Location tpLoc = new Location(event.getFrom().getWorld(), (double)event.getFrom().getBlockX(), (double)event.getFrom().getBlockY(), (double)event.getFrom().getBlockZ());
            tpLoc.setPitch(event.getPlayer().getLocation().getPitch());
            tpLoc.setYaw(event.getPlayer().getLocation().getYaw());
            event.getPlayer().teleport(tpLoc);
    	}
    }

    @EventHandler(priority = EventPriority.LOW)
    void onChat(AsyncPlayerChatEvent event)
    {
        final Player p = event.getPlayer();
        if(!p.hasMetadata("ChaosControl.confirmed"))
        {
            plugin.getLogger().info("RECEIVED MESSAGE: " + event.getMessage());
            if(event.getMessage().compareTo(plugin.getConfig().getString("stringTextToAccept"))==0)
            {
                p.setMetadata("ChaosControl.confirmed",new FixedMetadataValue(plugin,true));

                BukkitTask task = new TeleportControl(this.plugin,p).runTaskLater(this.plugin, 20);
            }
            else
            {
                p.sendMessage(plugin.getConfig().getString("stringNoChatOrCommandsUntilConfirm"));
                event.setCancelled(true);
                return;
            }
        }

        //Do not send messages to players who haven't accepted initial warning.
        for(Player rp: event.getRecipients())
        {
           if(!rp.hasMetadata("ChaosControl.confirmed"))
           {
               event.getRecipients().remove(rp);
           }
        }
    }

    void sendWarning(Player p)
    {
        p.sendMessage(ChatColor.YELLOW + plugin.getConfig().getString("stringSendWarningHeader"));
        p.sendMessage(ChatColor.DARK_PURPLE+"--------------------------");
        for(String line : plugin.getConfig().getStringList("arrayMessageLines"))
        {
        	p.sendMessage(ChatColor.GOLD + line);
        }
        p.sendMessage(ChatColor.DARK_PURPLE+"--------------------------");
        p.sendMessage(ChatColor.YELLOW + plugin.getConfig().getString("stringSendWarningFooter"));
        p.sendMessage(ChatColor.GOLD + plugin.getConfig().getString("stringTextToAccept"));
    }
}
