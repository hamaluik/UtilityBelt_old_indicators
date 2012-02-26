package com.mcnsa.utilitybelt.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.mcnsa.utilitybelt.UtilityBelt;

public class TeamTracker {
	private ArrayList<String> playersWithMod = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> teams = new HashMap<String, ArrayList<String>>();
	private HashMap<String, String> playerTeams = new HashMap<String, String>();
	private ArrayList<String> beingInvited = new ArrayList<String>();
	private MarkerColour markerColours = new MarkerColour();
	private UtilityBelt plugin = null;
	
	public TeamTracker(UtilityBelt instance) {
		plugin = instance;
		
		// make sure to reload all players
		Player[] players = plugin.getServer().getOnlinePlayers();
		for(int i = 0; i < players.length; i++) {
			ColourHandler.sendMessage(players[i], "&creload");
		}
	}
	
	// track people with the mod
	public void playerHasMod(String player) {
		if(!playersWithMod.contains(player)) {
			playersWithMod.add(player);
		}
	}
	
	public boolean isPlayerTracked(String player) {
		return playersWithMod.contains(player);
	}
	
	public ArrayList<String> playersWithMod() {
		return playersWithMod;
	}
	
	public void playerQuit(String player) {
		// remove them from being tracked
		if(playersWithMod.contains(player)) {
			playersWithMod.remove(player);
		}
		// remove them from pending invites
		if(beingInvited.contains(player)) {
			beingInvited.remove(player);
		}
		// note: they will already have been removed from their team at this point
	}
	
	// which players have the mod but are not part of a team
	public ArrayList<String> playersAvailableForInvite() {
		ArrayList<String> ret = new ArrayList<String>();
		
		plugin.debug("Looking for invitable players...");
		for(int i = 0; i < playersWithMod.size(); i++) {
			plugin.debug("\tchecking player " + playersWithMod.get(i));
			if(!playerTeams.containsKey(playersWithMod.get(i)) && !beingInvited.contains(playersWithMod.get(i))) {
				ret.add(playersWithMod.get(i));
				plugin.debug("\t\tyes");
			}
			else {
				plugin.debug("\t\tnope");
			}
		}
		
		return ret;
	}
	
	// see if a specific player can be invited
	public boolean playerAvailableForInvite(String player) {
		// make sure they have the mod
		// make sure they're not already on a team
		// make sure they don't have an invitation pending
		if(playersWithMod.contains(player) && !playerTeams.containsKey(player) && !beingInvited.contains(player)) {
			// yes, they have the mod and can be invited
			return true;
		}
		// nope!
		return false;
	}
	
	public void invitationSent(String player) {
		if(!beingInvited.contains(player)) {
			beingInvited.add(player);
		}
	}
	
	public void invitationDealtWith(String player) {
		if(beingInvited.contains(player)) {
			beingInvited.remove(player);
		}
	}
	
	// get which team a player is on
	public String playersTeam(String player) {
		if(playerTeams.containsKey(player)) {
			return playerTeams.get(player);
		}
		return new String("");
	}
	
	public ArrayList<String> playersOnPlayersTeam(String player) {
		String team = playersTeam(player);
		if(team.equals("")) {
			// they're not on a team!
			// but they're on their own team...
			ArrayList<String> selfTeam = new ArrayList<String>();
			selfTeam.add(player);
			return selfTeam;
		}
		
		// ok, return their team mates!
		return playersOnTeam(team);
	}
	
	// get a list of everyone who's in a given team
	public ArrayList<String> playersOnTeam(String team) {
		if(!teams.containsKey(team)) {
			// the team doesn't exist!
			return new ArrayList<String>();
		}
		
		return teams.get(team);
	}
	
	// add a player to a team or start a team if it doesn't exist
	public void joinTeam(String team, String player) {
		if(!teams.containsKey(team)) {
			// the team didn't exist, make it!
			ArrayList<String> playerList = new ArrayList<String>();
			playerList.add(player);
			teams.put(team, playerList);
			playerTeams.put(player, team);
		}
		else {
			// add the player to the team if they weren't already on it
			if(!teams.get(team).contains(player)) {
				teams.get(team).add(player);
				playerTeams.put(player, team);
			}
		}
	}
	
	public boolean leaveTeam(String player) {
		// make sure it's a valid team
		String team = playersTeam(player);
		if(!teams.containsKey(team)) {
			// the team doesn't exist
			return false;
		}
		
		// make sure they're on the team
		if(!teams.get(team).contains(player)) {
			// the team does exist
			return true;
		}
		
		// ok, remove them
		teams.get(team).remove(player);
		playerTeams.remove(player);
		
		// now if the team is empty, remove it
		if(teams.get(team).size() < 1) {
			teams.remove(team);
			// the team doesn't exist
			return false;
		}
		
		// the team still exists
		return true;
	}
	
	public Colour playerColour(String player) {
		// see if they're on a team
		if(playerTeams.containsKey(player)) {
			// yup, on a team
			String team = playerTeams.get(player);
			// get their number on the team
			return markerColours.getColour(teams.get(team).indexOf(player));
		}
		// just return the default colour
		return markerColours.getColour(0);
	}
	
	public class MarkerColour {
		public Colour[] colours;
		public MarkerColour() {
			colours = new Colour[] {
					new Colour(85, 255, 85, "&a"), // green
					new Colour(85, 85, 255, "&9"), // blue
					new Colour(255, 255, 85, "&e"), // yellow
					new Colour(255, 85, 85, "&c"), // red
					new Colour(85, 255, 255, "&b"), // teal 
					new Colour(255, 170, 0, "&6"), // orange
					new Colour(255, 85, 255, "&d"), // pink
					new Colour(170, 170, 170, "&7") // grey
					};
		}
		
		public Colour getColour(int i) {
			if(i >= 8) {
				return new Colour(255, 255, 255, "&f");
			}
			return colours[i];
		}
	}
	
	public class Colour {
		public int r = 0;
		public int g = 0;
		public int b = 0;
		public String strVal = new String("");
		
		public Colour(int i, int j, int k, String _strVal) {
			r = i;
			g = j;
			b = k;
			strVal = _strVal;
		}
	}
}
