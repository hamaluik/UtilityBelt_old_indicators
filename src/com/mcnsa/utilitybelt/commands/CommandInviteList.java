package com.mcnsa.utilitybelt.commands;

import java.util.ArrayList;

import net.minecraft.server.Packet3Chat;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mcnsa.utilitybelt.UtilityBelt;
import com.mcnsa.utilitybelt.util.Command;
import com.mcnsa.utilitybelt.util.CommandInfo;

@CommandInfo(alias = "@$&#_utility_invite_list", visible = false)
public class CommandInviteList implements Command {
	private static UtilityBelt plugin = null;
	public CommandInviteList(UtilityBelt instance) {
		plugin = instance;
	}

	public Boolean handle(Player player, String sArgs) {
		String start = "\247b\247d\247c\247b\247d\247cq?=$playerList=start";
		String end = "\247b\247d\247c\247b\247d\247cq?=$playerList=end";
		

		((CraftPlayer)player).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(start));
		// loop through all players who have the mod
		ArrayList<String> players = plugin.teamTracker.playersAvailableForInvite();
		for(int i = 0; i < players.size(); i++) {
			if(!players.get(i).equals(player.getName())) {
				((CraftPlayer)player).getHandle().netServerHandler.networkManager.queue(new Packet3Chat("\247b\247d\247c\247b\247d\247cq?=$playerList=" + players.get(i)));
			}
		}
		((CraftPlayer)player).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(end));
		
		// and we handled it!
		return true;
	}
}

