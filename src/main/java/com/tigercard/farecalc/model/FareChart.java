package com.tigercard.farecalc.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * {@link AbstractMap.SimpleEntry} is the java equivalent of Tuple. 
 */
public class FareChart {
	private static Map<AbstractMap.SimpleEntry<Zone, Zone>, List<Fare>> fareEntries = new HashMap<AbstractMap.SimpleEntry<Zone, Zone>, List<Fare>>();
	
	public static void initFareChart(File file) throws FileNotFoundException, IOException {
		List<Fare> fareList;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String[] parts = line.split(",");
		    	if(parts.length >= 3) {
		    		String[] zones = parts[0].split("-");
		    		if(zones.length >= 2) {
		    			AbstractMap.SimpleEntry<Zone, Zone> zone = 
		    					new AbstractMap.SimpleEntry<Zone, Zone>(new Zone(Integer.valueOf(zones[0])), new Zone(Integer.valueOf(zones[1])));
		    			fareList = new ArrayList<Fare>();
			    		fareList.add(new Fare(FareType.Peak, Integer.valueOf(parts[1])));
			    		fareList.add(new Fare(FareType.OffPeak, Integer.valueOf(parts[2])));
			    		fareEntries.put(zone, Collections.unmodifiableList(fareList));
		    		}
		    	}
		    }
		}
	}
	
	public static int getFareAmount(Zone fromZone, Zone toZone, FareType fareType) {
		AbstractMap.SimpleEntry<Zone, Zone> from_to = new AbstractMap.SimpleEntry<Zone, Zone>(fromZone, toZone);
		
		if(fareEntries.get(from_to) == null) {
			throw new IllegalArgumentException("The fare chart does not have a fare entry for: " + fromZone.getZoneId() + " to " + toZone.getZoneId());
		}
		
		List<Fare> list = fareEntries.get(from_to);
		Iterator<Fare> iterator = list.iterator();
		while(iterator.hasNext()) {
			Fare fare = iterator.next();
			if(fare.getFareType().equals(fareType)) {
				return fare.getFareAmount();
			}
		}
		
		throw new IllegalArgumentException("There's no fare amount for: " + 
				fromZone.getZoneId() + " to " + toZone.getZoneId() +
				"fare type: " + fareType.toString());
	}
}
