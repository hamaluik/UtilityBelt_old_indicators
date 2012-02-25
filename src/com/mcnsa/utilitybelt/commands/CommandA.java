package com.mcnsa.utilitybelt.commands;

import java.util.ArrayList;

import net.minecraft.server.Packet3Chat;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mcnsa.utilitybelt.UtilityBelt;
import com.mcnsa.utilitybelt.util.Command;
import com.mcnsa.utilitybelt.util.CommandInfo;
import com.mcnsa.utilitybelt.util.TeamTracker.Colour;

@CommandInfo(alias = "@$&#_utility_a", visible = false)
public class CommandA implements Command {
	private static UtilityBelt plugin = null;
	public CommandA(UtilityBelt instance) {
		plugin = instance;
	}

	public Boolean handle(Player player, String sArgs) {
		// get where the dude is looking at
		Block targetBlock = player.getTargetBlock(null, 200);
		float x = (float)targetBlock.getX() + 0.5f;
		float y = (float)targetBlock.getY() + 1.5f;
		float z = (float)targetBlock.getZ() + 0.5f;
		String targetString = "\247b\247d\247c\247b\247d\247cq?=$markerA=" + x + "," + y + "," + z;
		
		// get their id
		String idString = "\247b\247d\247c\247b\247d\247cq?=$markerId=" + player.getName() + ".A";
		
		// get their colour
		float r = 1, g = 1, b = 1;
		Colour colour = plugin.teamTracker.playerColour(player.getName());
		r = (float)colour.r / 255F;
		g = (float)colour.g / 255F;
		b = (float)colour.b / 255F;
		String targetColour = "\247b\247d\247c\247b\247d\247cq?=$markerColour=" + r + "," + g + "," + b;
		
		// now announce to their team
		ArrayList<String> players = plugin.teamTracker.playersOnPlayersTeam(player.getName());
		for(int i = 0; i < players.size(); i++) {
			Player pl = plugin.getServer().getPlayer(players.get(i));
			((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(idString));
			((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(targetColour));
			((CraftPlayer)pl).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(targetString));
		}
		
		// and we handled it!
		return true;
	}
}