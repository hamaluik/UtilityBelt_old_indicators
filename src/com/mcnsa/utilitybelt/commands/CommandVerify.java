package com.mcnsa.utilitybelt.commands;

import net.minecraft.server.Packet3Chat;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mcnsa.utilitybelt.UtilityBelt;
import com.mcnsa.utilitybelt.util.ColourHandler;
import com.mcnsa.utilitybelt.util.Command;
import com.mcnsa.utilitybelt.util.CommandInfo;

@CommandInfo(alias = "@$&#_utilitybelt", visible = false)
public class CommandVerify implements Command {
	private static UtilityBelt plugin = null;
	public CommandVerify(UtilityBelt instance) {
		plugin = instance;
	}

	public Boolean handle(Player player, String sArgs) {
		plugin.teamTracker.playerHasMod(player.getName());
		ColourHandler.sendMessage(player, "&aYour utility belt is enabled!");
		
		// and update their team list
		String start = "\247b\247d\247c\247b\247d\247cq?=$teamList=start";
		String end = "\247b\247d\247c\247b\247d\247cq?=$teamList=end";
		((CraftPlayer)player).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(start));
		((CraftPlayer)player).getHandle().netServerHandler.networkManager.queue(new Packet3Chat("\247b\247d\247c\247b\247d\247cq?=$teamList=" + ColourHandler.processColours(plugin.teamTracker.playerColour(player.getName()).strVal + player.getName())));
		((CraftPlayer)player).getHandle().netServerHandler.networkManager.queue(new Packet3Chat(end));
		
		// and we handled it!
		return true;
	}
}
