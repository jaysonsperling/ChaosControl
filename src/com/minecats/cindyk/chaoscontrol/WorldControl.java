package com.minecats.cindyk.chaoscontrol;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.util.Vector;

/**
 * Created by cindy on 4/16/14.
 */
public class WorldControl implements Listener {

    ChaosControl plugin;
    int countEntities;

    WorldControl(ChaosControl plugin)
    {
        this.plugin = plugin;
        countEntities = 0;
    }

    //Next UP ANIMAL CONTROL!!!
    //when a creature spawns...
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntitySpawn(CreatureSpawnEvent event)
    {

            for (World world : plugin.getServer().getWorlds()) {

                int CurrentWorldCount = world.getLivingEntities().size();

                if(CurrentWorldCount > 400)
                {
                    plugin.getServer().broadcastMessage(ChatColor.RED+"[ChaosControl] Warning! Warning! There are too many Living Entities on "+world.getName()+"! Butchering is about to commence");

                    int totalRemoved = 0;
                    for(LivingEntity e: world.getLivingEntities())
                    {
                        if(e.getType()!= EntityType.PLAYER)
                        {
                            e.remove();
                            totalRemoved++;
                        }

                    }
                    plugin.getLogger().info("Removed " + totalRemoved + "  from world: " + world.getName());
                    plugin.getServer().broadcastMessage("[ChaosControl] Removed " + totalRemoved + " Living Entities from world: " + world.getName());
                }

                if(CurrentWorldCount%100 == 0 &&  CurrentWorldCount > 0)
                    plugin.getLogger().info("[ChaosControl] Current Living Entity Population is: " +CurrentWorldCount+ " on world "+ world.getName() );


            }

    }


}
