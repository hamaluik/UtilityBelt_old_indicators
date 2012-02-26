package com.mcnsa.utilitybelt.commands;

import java.util.ArrayList;

import net.minecraft.server.Packet3Chat;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mcnsa.utilitybelt.UtilityBelt;
import com.mcnsa.utilitybelt.util.ColourHandler;
import com.mcnsa.utilitybelt.util.Command;
import com.mcnsa.utilitybelt.util.CommandInfo;

@CommandInfo(alias = "@$&#_utility_accept", visible = false)
public class CommandAccept implements Command {
	private static UtilityBelt plugin = null;
	public CommandAccept(UtilityBelt instance) {
		plugin = instance;
	}

	public Boolean handle(Player player, String sArgs) {
		// get the player's name
		String inviterTarget = sArgs;
		
		// make sure they're online
		if(plugin.getServer().getPlayerExact(inviterTarget) == null) {
			ColourHandler.sendMessage(player, "&cError: that player isn't online anymore!");
			return true;
		}
		
		// and a message back to the person
		ColourHandler.sendMessage(player, "&aAcception sent!");
		ColourHandler.sendMessage(plugin.getServer().getPlayerExact(inviterTarget), "&a" + player.getName() + " accepted your invitation!");
		
		// and remove them from the invite list
		plugin.teamTracker.invitationDealtWith(player.getName());
		
		// see if the inviter was on a team yet
		String team = plugin.teamTracker.playersTeam(inviterTarget);
		if(team.equals("")) {
			// the inviter wasn't on a team yet!
			// create a new team!
			team = inviterTarget;
			plugin.teamTracker.joinTeam(team, inviterTarget);
		}
		// now get this player to join the team
		plugin.teamTracker.joinTeam(team, player.getName());
		
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
		
		// and we handled it!
		return true;
	}
}
