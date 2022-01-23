package com.tigercard.farecalc.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link AbstractMap.SimpleEntry} is the java equivalent of Tuple.
 */
public class FareCap {
	private static Map<AbstractMap.SimpleEntry<Zone, Zone>, Integer> dailyCap = new HashMap<AbstractMap.SimpleEntry<Zone, Zone>, Integer>();
	private static Map<AbstractMap.SimpleEntry<Zone, Zone>, Integer> weeklyCap = new HashMap<AbstractMap.SimpleEntry<Zone, Zone>, Integer>();
	
	public static void initFareCap(File file) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String[] parts = line.split(",");
		    	if(parts.length >= 3) {
		    		String[] zones = parts[0].split("-");
		    		if(zones.length >= 2) {
		    			AbstractMap.SimpleEntry<Zone, Zone> zone = 
		    					new AbstractMap.SimpleEntry<Zone, Zone>(new Zone(Integer.valueOf(zones[0])), new Zone(Integer.valueOf(zones[1])));
		    			dailyCap.put(zone, Integer.valueOf(parts[1]));
		    			weeklyCap.put(zone, Integer.valueOf(parts[2]));
		    		}
		    	}
		    }
		}
	}
	
	public static int getDailyCap(Zone fromZone, Zone toZone) {
		AbstractMap.SimpleEntry<Zone, Zone> from_to = new AbstractMap.SimpleEntry<Zone, Zone>(fromZone, toZone);
		
		if(dailyCap.get(from_to) == null) {
			throw new IllegalArgumentException("The model does not have a daily cap for: " + fromZone.getZoneId() + " to " + toZone.getZoneId());
		}
		
		return dailyCap.get(from_to);
	}
	
	public static int getWeeklyCap(Zone fromZone, Zone toZone) {
		AbstractMap.SimpleEntry<Zone, Zone> from_to = new AbstractMap.SimpleEntry<Zone, Zone>(fromZone, toZone);
		
		if(weeklyCap.get(from_to) == null) {
			throw new IllegalArgumentException("The model does not have a weekly cap for: " + fromZone.getZoneId() + " to " + toZone.getZoneId());
		}
		
		return weeklyCap.get(from_to);
	}
}
