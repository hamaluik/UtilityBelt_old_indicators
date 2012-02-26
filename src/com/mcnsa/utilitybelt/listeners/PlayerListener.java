package com.mcnsa.utilitybelt.listeners;

import java.util.ArrayList;

import net.minecraft.server.Packet3Chat;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mcnsa.utilitybelt.UtilityBelt;
import com.mcnsa.utilitybelt.util.ColourHandler;

public class PlayerListener implements Listener {
	UtilityBelt plugin = null;
	public PlayerListener(UtilityBelt instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerQuitHandler(PlayerQuitEvent event) {
		// make sure we're tracking them
		if(!plugin.teamTracker.isPlayerTracked(event.getPlayer().getName())) {
			// ok, we're not tracking them
			return;
		}
		
		// see if they're already on a team
		String team = plugin.teamTracker.playersTeam(event.getPlayer().getName());
		if(!team.equals("")) {
			// remove all their markers
			// now close all the markers the player had open
			String targetCancelHere = "\247b\247d\247c\247b\247d\247cq?=$markerCancel="+event.getPlayer().getName()+".here";
			String targetCancelTimer = "\247b\247d\247c\247b\247d\247cq?=$markerCancel="+event.getPlayer().getName()+".timer";
			String targetCancelA = "\247b\247d\247c\247b\247d\247cq?=$markerCancel="+event.getPlayer().getName()+".A";
			String targetCancelB = "\247b\247d\247c\247b\247d\247cq?=$markerCancel="+event.getPlayer().getName()+".B";
			
			// now announce to their team
			ArrayList<String> preTeamPlayers = plugin.teamTracker.playersOnPlayersTeam(event.getPlayer().getName());
			for(int i = 0; i < preTeamPlayers.size(); i++) {
				Player pl = plugin.getServer().getPlayer(preTeamPlayers.get(i));
				((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(targetCancelHere));
				((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(targetCancelTimer));
				((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(targetCancelA));
				((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(targetCancelB));
			}
			
			// ok, remove them from the team
			boolean teamExists = plugin.teamTracker.leaveTeam(event.getPlayer().getName());
			
			if(teamExists) {
				// now update everyone who's on this team about the changes
				ArrayList<String> players = plugin.teamTracker.playersOnTeam(team);
				
				for(int i = 0; i < players.size(); i++) {
					// now tell them who's on their team
					String start = "\247b\247d\247c\247b\247d\247cq?=$teamList=start";
					String end = "\247b\247d\247c\247b\247d\247cq?=$teamList=end";

					Player pl = plugin.getServer().getPlayer(players.get(i));
					((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(start));
					// loop through all players who have the mod
					for(int j = 0; j < players.size(); j++) {
						((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat("\247b\247d\247c\247b\247d\247cq?=$teamList=" + ColourHandler.processColours(plugin.teamTracker.playerColour(players.get(i)).strVal + players.get(j))));
					}
					((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(end));
				}
			}
		}
		
		// now remove them from everything else
		plugin.teamTracker.playerQuit(event.getPlayer().getName());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void preprocessHandler(PlayerCommandPreprocessEvent event) {
		// if the command is cancelled, back out
		if(event.isCancelled()) return;
		
		// intercept the command
		if(plugin.commandManager.handleCommand(event.getPlayer(), event.getMessage())) {
			// we handled it, cancel it
			event.setCancelled(true);
		}
	}
}
