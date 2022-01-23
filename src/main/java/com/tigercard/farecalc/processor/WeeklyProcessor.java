package com.tigercard.farecalc.processor;

import java.util.AbstractMap;

import org.apache.log4j.Logger;

import com.tigercard.farecalc.model.LineItemEntry;
import com.tigercard.farecalc.model.WeekEntry;
import com.tigercard.farecalc.model.Zone;
import com.tigercard.farecalc.utils.FareUtils;

/**
 *  {@link WeeklyProcessor} process the weekly fare entries.
 *  Up-on reaching the week boundary, the accrued weekly fare entries {@link WeekEntry} (considering the weekly cap) is rolled-up
 *  and added to the total amount in {@link SharedState}.  
 */
public class WeeklyProcessor implements FareProcessor {

	private SharedState sharedState;
	private FareProcessor childProcessor;
	
	static Logger log = Logger.getLogger(WeeklyProcessor.class.getName());
	
	public void initContext(SharedState sharedState) {
		this.sharedState = sharedState;
		if(childProcessor != null) {
			childProcessor.initContext(this.sharedState);
		}
	}

	public void setChildProcessor(FareProcessor fareCapProcessor) {
		this.childProcessor = fareCapProcessor;
	}

	public void processLineItemEntry(LineItemEntry lineItemEntry) {
		processWeekBoundary(lineItemEntry);
		
		computeFarthestTripInAWeek(lineItemEntry);
		
		if(childProcessor != null) {
			childProcessor.processLineItemEntry(lineItemEntry);
		}
	}

	public void rollUp() {
		
		if(childProcessor != null) {
			childProcessor.rollUp();
		}
		
		int accrualWeekly = sharedState.weekEntries.stream()
				.map(e-> e.getFare())
				.reduce(0, Integer::sum);
		
		rollWeekEntriesToTotalAmount(accrualWeekly);
	}

	private void computeFarthestTripInAWeek(LineItemEntry lineItemEntry) {
		if(sharedState.farthestTripInAWeek != null) {
			if(FareUtils.zoneDiff(lineItemEntry.getFromZone(), lineItemEntry.getToZone()) >
				FareUtils.zoneDiff(sharedState.farthestTripInAWeek.getKey(), sharedState.farthestTripInAWeek.getValue())
			) {
				sharedState.farthestTripInAWeek = 
						new AbstractMap.SimpleEntry<Zone, Zone>(lineItemEntry.getFromZone(), lineItemEntry.getToZone());
			}
		} else {
			sharedState.farthestTripInAWeek = 
					new AbstractMap.SimpleEntry<Zone, Zone>(lineItemEntry.getFromZone(), lineItemEntry.getToZone());
		}
	}

	private void resetFarthestTripInAWeek() {
		sharedState.farthestTripInAWeek = null;
	}
	
	private void rollWeekEntriesToTotalAmount(int accrualWeekly) {
		sharedState.totalAmount += accrualWeekly;
		sharedState.weekEntries.clear();
	}
	
	private void processWeekBoundary(LineItemEntry lineItemEntry) {
		if(sharedState.lastProcessedEntry != null) {
			if(FareUtils.isNewWeek(lineItemEntry, sharedState.lastProcessedEntry)) {
				log.info("Week boundary reached. Current date-time: " +
						lineItemEntry.getDateTime());
				rollUp();
				resetFarthestTripInAWeek();
			}
		}
	}
}
