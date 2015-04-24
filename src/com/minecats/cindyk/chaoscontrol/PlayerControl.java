package com.minecats.cindyk.chaoscontrol;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import sun.net.www.content.text.plain;

/**
 * Created by cindy on 4/16/14.
 */
public class PlayerControl implements Listener {

    ChaosControl plugin;
    int taskId;

    String query = "I confirm that I am old enough to see adult material";

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
            p.sendMessage("Op player entry... Not going to pester you for silly warning.");
            return;
        }

        if(p.hasPermission("ChaosControl.confirmed"))
        {
            p.setMetadata("ChaosControl.confirmed",new FixedMetadataValue(plugin,true));
            p.sendMessage("Permitted player entry... Not going to pester you for silly warning.");
            return;
        }

        if(p.hasMetadata("ChaosControl.confirmed"))
        {
            p.removeMetadata("ChaosControl.confirmed",plugin);
        }
        p.removeMetadata("ChaosControl.count",plugin);


        sendWarning(p);

        taskId= plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {

                if(!p.hasMetadata("ChaosControl.confirmed"))
                {
                    if(p.hasMetadata("ChaosControl.count"))
                    {
                        int count = Integer.parseInt( plugin.getMetadataString(p,"ChaosControl.count",plugin));

                        if(count < 6)
                        {
                            plugin.getLogger().info("Warning Sent! Count: "+count);
                            sendWarning(p);
                            count++;
                            p.setMetadata("ChaosControl.count",new FixedMetadataValue(plugin,""+count));

                        }
                        else
                        {

                            p.kickPlayer("You do not belong here. ");
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
            if(event.getMessage().compareTo(query)==0)
            {
                p.setMetadata("ChaosControl.confirmed",new FixedMetadataValue(plugin,true));
                p.teleport(plugin.getServer().getWorld("world").getSpawnLocation());
                return;
            }
            else
            {

                p.sendMessage("You cannot talk or run commands until you confirm your age");
                sendWarning(p);
                event.setCancelled(true);
            }
        }

    }

    @EventHandler(priority = EventPriority.LOW)
    void onChat(AsyncPlayerChatEvent event)
    {
        final Player p = event.getPlayer();
        if(!p.hasMetadata("ChaosControl.confirmed"))
        {
            plugin.getLogger().info("RECEIVED MESSAGE: " + event.getMessage());
            if(event.getMessage().compareTo(query)==0)
            {
                p.setMetadata("ChaosControl.confirmed",new FixedMetadataValue(plugin,true));


                BukkitTask task = new TeleportControl(this.plugin,p).runTaskLater(this.plugin, 20);
            }
            else
            {
                p.sendMessage("You cannot talk to anyone until you confirm your age");
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

        p.sendMessage(ChatColor.DARK_PURPLE+"--------------------------");
        p.sendMessage(ChatColor.GOLD+"This Server is NOT Moderated");
        p.sendMessage(ChatColor.GOLD + "And is intended for Mature audiences");
        p.sendMessage(ChatColor.GOLD+ "There is no chat moderation or building moderation.");
        p.sendMessage(ChatColor.GOLD+ "To play you must state that you are old enough to play in ");
        p.sendMessage(ChatColor.GOLD+ "this environment.");
        p.sendMessage(ChatColor.DARK_PURPLE+"--------------------------");
        p.sendMessage(ChatColor.GOLD + "Please type: ");
        p.sendMessage(ChatColor.GOLD + query);


    }

}
