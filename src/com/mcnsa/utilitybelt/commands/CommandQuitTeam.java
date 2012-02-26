package com.mcnsa.utilitybelt.commands;

import java.util.ArrayList;

import net.minecraft.server.Packet3Chat;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mcnsa.utilitybelt.UtilityBelt;
import com.mcnsa.utilitybelt.util.ColourHandler;
import com.mcnsa.utilitybelt.util.Command;
import com.mcnsa.utilitybelt.util.CommandInfo;

@CommandInfo(alias = "@$&#_utility_quitteam", visible = false)
public class CommandQuitTeam implements Command {
	private static UtilityBelt plugin = null;
	public CommandQuitTeam(UtilityBelt instance) {
		plugin = instance;
	}

	public Boolean handle(Player player, String sArgs) {
		// make sure they're already on a team
		String team = plugin.teamTracker.playersTeam(player.getName());
		if(team.equals("")) {
			ColourHandler.sendMessage(player, "&cYou aren't even on a team!");
			return true;
		}
		
		// remove all their markers
		// now close all the markers the player had open
		String targetCancelHere = "\247b\247d\247c\247b\247d\247cq?=$markerCancel="+player.getName()+".here";
		String targetCancelTimer = "\247b\247d\247c\247b\247d\247cq?=$markerCancel="+player.getName()+".timer";
		String targetCancelA = "\247b\247d\247c\247b\247d\247cq?=$markerCancel="+player.getName()+".A";
		String targetCancelB = "\247b\247d\247c\247b\247d\247cq?=$markerCancel="+player.getName()+".B";
		
		// now announce to their team
		ArrayList<String> preTeamPlayers = plugin.teamTracker.playersOnPlayersTeam(player.getName());
		for(int i = 0; i < preTeamPlayers.size(); i++) {
			Player pl = plugin.getServer().getPlayer(preTeamPlayers.get(i));
			((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(targetCancelHere));
			((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(targetCancelTimer));
			((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(targetCancelA));
			((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(targetCancelB));
		}
		
		// ok, remove them from the team
		boolean teamExists = plugin.teamTracker.leaveTeam(player.getName());
		
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
					((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat("\247b\247d\247c\247b\247d\247cq?=$teamList=" + ColourHandler.processColours(plugin.teamTracker.playerColour(players.get(j)).strVal + players.get(j))));
				}
				((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(end));
			}
		}
		
		// now update the player who quit
		String start = "\247b\247d\247c\247b\247d\247cq?=$teamList=start";
		String end = "\247b\247d\247c\247b\247d\247cq?=$teamList=end";
		((CraftPlayer)player).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(start));
		((CraftPlayer)player).getHandle().netServerHandler.networkManager.queue(new Packet3Chat("\247b\247d\247c\247b\247d\247cq?=$teamList=" + ColourHandler.processColours(plugin.teamTracker.playerColour(player.getName()).strVal + player.getName())));
		((CraftPlayer)player).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(end));
		
		// and we handled it!
		return true;
	}
}
