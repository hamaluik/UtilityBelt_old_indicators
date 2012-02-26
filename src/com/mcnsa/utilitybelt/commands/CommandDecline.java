package com.mcnsa.utilitybelt.commands;

import org.bukkit.entity.Player;

import com.mcnsa.utilitybelt.UtilityBelt;
import com.mcnsa.utilitybelt.util.ColourHandler;
import com.mcnsa.utilitybelt.util.Command;
import com.mcnsa.utilitybelt.util.CommandInfo;

@CommandInfo(alias = "@$&#_utility_decline", visible = false)
public class CommandDecline implements Command {
	private static UtilityBelt plugin = null;
	public CommandDecline(UtilityBelt instance) {
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
		ColourHandler.sendMessage(player, "&aDeclination sent!");
		ColourHandler.sendMessage(plugin.getServer().getPlayerExact(inviterTarget), "&c" + player.getName() + " declined your invitation!");
		
		// and remove them from the invite list
		plugin.teamTracker.invitationDealtWith(player.getName());
		
		// and we handled it!
		return true;
	}
}