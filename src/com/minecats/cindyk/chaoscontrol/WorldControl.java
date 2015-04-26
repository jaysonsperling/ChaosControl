/**
 * WorldControl.java - Responsible for killing entities when there's too many of the pesky things around
 * 
 * Created by cindy on 4/16/14.
 * Updated by JaysonBond (ongoing)
 *
 */

package com.minecats.cindyk.chaoscontrol;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import com.minecats.cindyk.chaoscontrol.ChaosControl;

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

                if(CurrentWorldCount > plugin.getConfig().getInt("intButcherMobCount"))
                {
                	if (plugin.getConfig().getBoolean("enableButchering", true) == true) {
	                    plugin.getServer().broadcastMessage(ChatColor.RED+"[ChaosControl] Warning! There are too many mobs on " + world.getName() + " (" + CurrentWorldCount + ")! Butchering is about to commence!");
	
	                    int totalRemoved = 0;
	                    for(LivingEntity e: world.getLivingEntities())
	                    {
	                        if(e.getType()!= EntityType.PLAYER)
	                        {
	                            e.remove();
	                            totalRemoved++;
	                        }
	
	                    }
	                    plugin.getLogger().info("[ChaosControl] Removed " + totalRemoved + "  from world: " + world.getName());
	                    plugin.getServer().broadcastMessage("[ChaosControl] Removed " + totalRemoved + " mobs from world: " + world.getName());
                	} else {
                		plugin.getLogger().info(ChatColor.RED+"[ChaosControl] Warning! There are too many mobs on "+ world.getName() + " (" + CurrentWorldCount + ")! Butchering is disabled!");
                	}
                }

                if(CurrentWorldCount%100 == 0 && CurrentWorldCount > 0)
                    plugin.getLogger().info("[ChaosControl] Current mob population is: " + CurrentWorldCount + " on world " + world.getName());


            }

    }


}
