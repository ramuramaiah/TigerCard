package com.tigercard.farecalc.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tigercard.farecalc.model.FareCap;
import com.tigercard.farecalc.model.FareChart;
import com.tigercard.farecalc.model.LineItemEntry;
import com.tigercard.farecalc.model.Zone;

/**
 * Use the chain of responsibility principle design pattern.</br>
 * {@link DailyProcessor} is a child of {@link WeeklyProcessor}.</br>
 * 
 * In other-words, the control flows from {@link WeeklyProcessor} to 
 * {@link DailyProcessor}.</br>
 * 
 * This makes the design extensible to add more processors in the upper layer, for e.g.MonthlyProcessor.</br>
 */
public class ChainProcessor {

	private FareProcessor rootProcessor;
	private SharedState userContext;
	
	public void init() throws URISyntaxException, FileNotFoundException, IOException {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    	URL resource = classloader.getResource("fareChart.txt");
    	File fareChart = new File(resource.toURI());
		FareChart.initFareChart(fareChart);
		
		resource = classloader.getResource("fareCap.txt");
    	File fareCap = new File(resource.toURI());
		FareCap.initFareCap(fareCap);
		
		FareProcessor dailyProcessor = new DailyProcessor();
		dailyProcessor.setChildProcessor(null);
		
		FareProcessor weeklyProcessor = new WeeklyProcessor();
		weeklyProcessor.setChildProcessor(dailyProcessor);
		
		rootProcessor = weeklyProcessor;
		
		userContext = new SharedState();
		rootProcessor.initContext(userContext);
	}
	
	public void processFares(File file) throws FileNotFoundException, IOException {
		List<LineItemEntry> entries = new ArrayList<LineItemEntry>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String[] parts = line.split(",");
		    	if(parts.length >= 3) {
		    		LineItemEntry entry = 
			    			new LineItemEntry(LocalDateTime.parse(parts[0]), 
			    					new Zone(Integer.valueOf(parts[1])), 
			    					new Zone(Integer.valueOf(parts[2])));
		    		entries.add(entry);
		    	}
		    }
		}
		
		Iterator<LineItemEntry> iterator = entries.iterator();
		while(iterator.hasNext()) {
			LineItemEntry entry = iterator.next();
			rootProcessor.processLineItemEntry(entry);
		}
		
		rootProcessor.rollUp();
		
		System.out.println("The total fare after processing the entries in file: " + file.getName());
		System.out.println(userContext.totalAmount);
	}
}
