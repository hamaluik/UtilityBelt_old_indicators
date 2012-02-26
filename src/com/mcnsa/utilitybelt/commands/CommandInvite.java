package com.mcnsa.utilitybelt.commands;

import net.minecraft.server.Packet3Chat;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mcnsa.utilitybelt.UtilityBelt;
import com.mcnsa.utilitybelt.util.ColourHandler;
import com.mcnsa.utilitybelt.util.Command;
import com.mcnsa.utilitybelt.util.CommandInfo;

@CommandInfo(alias = "@$&#_utility_invite", visible = false)
public class CommandInvite implements Command {
	private static UtilityBelt plugin = null;
	public CommandInvite(UtilityBelt instance) {
		plugin = instance;
	}

	public Boolean handle(Player player, String sArgs) {
		// get the player's name
		String inviteTarget = sArgs;
		
		// make sure our team isn't full
		if(plugin.teamTracker.playersOnPlayersTeam(player.getName()).size() >= 8) {
			ColourHandler.sendMessage(player, "&cError: your team is full!");
			return true;
		}
		
		// make sure they're online
		if(plugin.getServer().getPlayerExact(inviteTarget) == null) {
			ColourHandler.sendMessage(player, "&cError: that player isn't online anymore!");
			return true;
		}
		
		// make sure they're still available for invites
		if(!plugin.teamTracker.playerAvailableForInvite(inviteTarget)) {
			ColourHandler.sendMessage(player, "&cError: that player isn't accepting invites right now!");
			return true;
		}
		
		// track the invite
		plugin.teamTracker.invitationSent(inviteTarget);
		
		// ok, now send the invite
		Player targetPlayer = plugin.getServer().getPlayerExact(inviteTarget);
		((CraftPlayer)targetPlayer).getHandle().netServerHandler.networkManager.queue(new Packet3Chat("\247b\247d\247c\247b\247d\247cq?=$markerInvitation=" + player.getName()));
		
		// and a message back to the person
		ColourHandler.sendMessage(player, "&aInvite sent!");
		ColourHandler.sendMessage(targetPlayer, "&aYou've been invited to join " + player.getName() + "'s team!");
		
		// and we handled it!
		return true;
	}
}
