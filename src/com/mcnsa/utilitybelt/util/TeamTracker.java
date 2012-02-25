package com.mcnsa.utilitybelt.util;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamTracker {
	private ArrayList<String> playersWithMod = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> teams = new HashMap<String, ArrayList<String>>();
	private HashMap<String, String> playerTeams = new HashMap<String, String>();
	private MarkerColour markerColours = new MarkerColour();
	
	// track people with the mod
	public void playerHasMod(String player) {
		if(!playersWithMod.contains(player)) {
			playersWithMod.add(player);
		}
	}
	
	public ArrayList<String> playersWithMod() {
		return playersWithMod;
	}
	
	// which players have the mod but are not part of a team
	public ArrayList<String> playersAvailableForInvite() {
		ArrayList<String> ret = new ArrayList<String>();
		
		for(int i = 0; i < playersWithMod.size(); i++) {
			if(!playerTeams.containsKey(playersWithMod.get(i))) {
				ret.add(playersWithMod.get(i));
			}
		}
		
		return ret;
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
	
	public void leaveTeam(String player) {
		// make sure it's a valid team
		String team = playersTeam(player);
		if(!teams.containsKey(team)) {
			return;
		}
		
		// make sure they're on the team
		if(!teams.get(team).contains(player)) {
			return;
		}
		
		// ok, remove them
		teams.get(team).remove(player);
		playerTeams.remove(player);
		
		// now if the team is empty, remove it
		if(teams.get(team).size() < 1) {
			teams.remove(team);
		}
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
					new Colour(85, 255, 85), // green
					new Colour(85, 85, 255), // blue
					new Colour(255, 255, 85), // yellow
					new Colour(255, 85, 85), // red
					new Colour(85, 255, 255), // teal 
					new Colour(255, 170, 0), // orange
					new Colour(255, 85, 255), // pink
					new Colour(170, 170, 170) // grey
					};
		}
		
		public Colour getColour(int i) {
			if(i >= 8) {
				return new Colour(255, 255, 255);
			}
			return colours[i];
		}
	}
	
	public class Colour {
		public int r = 0;
		public int g = 0;
		public int b = 0;
		
		public Colour(int i, int j, int k) {
			r = i;
			g = j;
			b = k;
		}
	}
}
