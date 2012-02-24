package com.mcnsa.utilitybelt.commands;

import org.bukkit.entity.Player;

import com.mcnsa.utilitybelt.UtilityBelt;
import com.mcnsa.utilitybelt.util.ColourHandler;
import com.mcnsa.utilitybelt.util.Command;
import com.mcnsa.utilitybelt.util.CommandInfo;

@CommandInfo(alias = "@$&#_utilityBelt", visible = false)
public class CommandVerify implements Command {
	@SuppressWarnings("unused")
	private static UtilityBelt plugin = null;
	public CommandVerify(UtilityBelt instance) {
		plugin = instance;
	}

	public Boolean handle(Player player, String sArgs) {		
		// handle the /me
		ColourHandler.sendMessage(player, "&4Your utility belt is enabled!");
		
		// and we handled it!
		return true;
	}
}
