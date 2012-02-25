package com.mcnsa.utilitybelt.commands;

import java.util.ArrayList;

import net.minecraft.server.Packet3Chat;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mcnsa.utilitybelt.UtilityBelt;
import com.mcnsa.utilitybelt.util.Command;
import com.mcnsa.utilitybelt.util.CommandInfo;

@CommandInfo(alias = "@$&#_utility_cancel", visible = false)
public class CommandCancel implements Command {
	private static UtilityBelt plugin = null;
	public CommandCancel(UtilityBelt instance) {
		plugin = instance;
	}

	public Boolean handle(Player player, String sArgs) {
		// now close all the markers the player had open
		String targetCancelHere = "\247b\247d\247c\247b\247d\247cq?=$markerCancel="+player.getName()+".here";
		String targetCancelTimer = "\247b\247d\247c\247b\247d\247cq?=$markerCancel="+player.getName()+".timer";
		String targetCancelA = "\247b\247d\247c\247b\247d\247cq?=$markerCancel="+player.getName()+".A";
		String targetCancelB = "\247b\247d\247c\247b\247d\247cq?=$markerCancel="+player.getName()+".B";
		
		// now announce to their team
		ArrayList<String> players = plugin.teamTracker.playersOnPlayersTeam(player.getName());
		for(int i = 0; i < players.size(); i++) {
			Player pl = plugin.getServer().getPlayer(players.get(i));
			((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(targetCancelHere));
			((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(targetCancelTimer));
			((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(targetCancelA));
			((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(targetCancelB));
		}
		
		// and we handled it!
		return true;
	}
}
