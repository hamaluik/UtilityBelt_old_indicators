package com.mcnsa.utilitybelt.util;

import org.bukkit.entity.Player;

public interface Command {
	public Boolean handle(Player player, String sArgs);
}
