package com.mcnsa.utilitybelt;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

import com.mcnsa.utilitybelt.listeners.PlayerListener;
import com.mcnsa.utilitybelt.util.CommandManager;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class UtilityBelt extends JavaPlugin {
	// load the minecraft logger
	Logger log = Logger.getLogger("Minecraft");

	// keep track of permissions
	public PermissionManager permissions = null;
	
	// and internal things
	public PlayerListener playerListener = null;
	public CommandManager commandManager = null;

	public void onEnable() {
		// set up permissions
		this.setupPermissions();
		
		// setup the command manager
		commandManager = new CommandManager(this);

		// set up listeners
		playerListener = new PlayerListener(this);
		
		// routines for when the plugin gets enabled
		log("plugin enabled!");
	}

	public void onDisable() {		
		// shut the plugin down
		log("plugin disabled!");
	}

	// for simpler logging
	public void log(String info) {
		log.info("[UtilityBelt] " + info);
	}

	// for error reporting
	public void error(String info) {
		log.info("[UtilityBelt] <ERROR> " + info);
	}

	// for debugging
	// (disable for final release)
	public void debug(String info) {
		log.info("[UtilityBelt] <DEBUG> " + info);
	}

	// load the permissions plugin
	public void setupPermissions() {
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
			this.permissions = PermissionsEx.getPermissionManager();
			log("permissions successfully loaded!");
		}
		else {
			error("PermissionsEx not found!");
		}
	}

	// just an interface function for checking permissions
	// if permissions are down, default to OP status.
	public boolean hasPermission(Player player, String permission) {
		if(permissions != null) {
			return permissions.has(player, "mcnsachat2." + permission);
		}
		else {
			return player.isOp();
		}
	}
}

