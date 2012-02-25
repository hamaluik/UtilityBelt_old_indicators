package com.mcnsa.utilitybelt.commands;

import net.minecraft.server.Packet3Chat;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mcnsa.utilitybelt.UtilityBelt;
import com.mcnsa.utilitybelt.util.ColourHandler;
import com.mcnsa.utilitybelt.util.Command;
import com.mcnsa.utilitybelt.util.CommandInfo;

@CommandInfo(alias = "@$&#_utility_here", visible = false)
public class CommandHere implements Command {
	private static UtilityBelt plugin = null;
	public CommandHere(UtilityBelt instance) {
		plugin = instance;
	}

	public Boolean handle(Player player, String sArgs) {
		// get where the dude is looking at
		Block targetBlock = player.getTargetBlock(null, 200);
		float x = (float)targetBlock.getX() + 0.5f;
		float y = (float)targetBlock.getY() + 1.5f;
		float z = (float)targetBlock.getZ() + 0.5f;
		String targetString = "\247b\247d\247c\247b\247d\247cq?=$markerHere=" + x + "," + y + "," + z;
		
		// announce to all connected players
		Player[] players = plugin.getServer().getOnlinePlayers();
		for(int i = 0; i < players.length; i++) {
			((CraftPlayer)players[i]).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(targetString));
		}
		
		// and we handled it!
		return true;
	}
}
