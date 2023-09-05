package com.tigercard.farecalc.processor;

import com.tigercard.farecalc.model.DayEntry;
import com.tigercard.farecalc.model.LineItemEntry;
import com.tigercard.farecalc.model.WeekEntry;
import com.tigercard.farecalc.utils.FareUtils;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.AbstractMap;

/**
 *  {@link DailyProcessor} process the daily fare entries.
 *  Up-on reaching the day boundary, the accrued daily fare entries {@link DayEntry} are rolled-up.
 *  and a {@link WeekEntry} is created and appended to the weekly entries in {@link SharedState}.  
 */
public class DailyProcessor implements FareProcessor {

	private SharedState sharedState;
	private FareProcessor childProcessor;
	
	static Logger log = Logger.getLogger(DailyProcessor.class.getName());
	
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
		
		processDayBoundary(lineItemEntry);
		
		computeFarthestTripInADay(lineItemEntry);
		
		int calcFare = FareCapLogic.calculateFare(sharedState, lineItemEntry);
		
		DayEntry dayEntry = createDayEntry(lineItemEntry, calcFare);
		
		sharedState.dayEntries.add(dayEntry);
		sharedState.lastProcessedEntry = lineItemEntry;
	}

	public void rollUp() {
		if(sharedState.dayEntries.isEmpty()) {
			return;
		}

		int accrualDaily = sharedState.dayEntries.stream()
				.map(DayEntry::getFare)
				.reduce(0, Integer::sum);
		
		rollDayEntriesToWeekEntry(accrualDaily);
	}

	private void computeFarthestTripInADay(LineItemEntry lineItemEntry) {
		if(sharedState.farthestTripInADay != null) {
			if(FareUtils.zoneDiff(lineItemEntry.getFromZone(), lineItemEntry.getToZone()) >
				FareUtils.zoneDiff(sharedState.farthestTripInADay.getKey(), sharedState.farthestTripInADay.getValue())
			) {
				sharedState.farthestTripInADay =
						new AbstractMap.SimpleEntry<>(lineItemEntry.getFromZone(), lineItemEntry.getToZone());
			}
		} else {
			sharedState.farthestTripInADay =
					new AbstractMap.SimpleEntry<>(lineItemEntry.getFromZone(), lineItemEntry.getToZone());
		}
	}

	private void resetFarthestTripInADay() {
		sharedState.farthestTripInADay = null;
	}
	
	private void rollDayEntriesToWeekEntry(int accrualDaily) {
		DayEntry dayFareEntry = sharedState.dayEntries.get(0);
		WeekEntry weekFareEntry = new WeekEntry();
		weekFareEntry.setLocalDateTime(
				LocalDateTime.of(dayFareEntry.getDateTime().getYear(), 
						dayFareEntry.getDateTime().getMonth(), 
						dayFareEntry.getDateTime().getDayOfMonth(),
						0,
						0));
		
		String zones = sharedState.farthestTripInADay.getKey().getZoneId() +
		" - " +
				sharedState.farthestTripInADay.getValue().getZoneId();
		
		weekFareEntry.setZones(zones);
		weekFareEntry.setFare(accrualDaily);
		sharedState.weekEntries.add(weekFareEntry);
		sharedState.dayEntries.clear();
	}
	
	private void processDayBoundary(LineItemEntry lineItemEntry) {
		if(sharedState.lastProcessedEntry != null) {
			if(FareUtils.isNewWeek(lineItemEntry, sharedState.lastProcessedEntry) || 
					FareUtils.isNewDay(lineItemEntry, sharedState.lastProcessedEntry)) {
				log.info("Day boundary reached. Current date-time: " +
						lineItemEntry.getDateTime() + ", " +"Previous date-time: " +
						sharedState.lastProcessedEntry.getDateTime());
				rollUp();
				resetFarthestTripInADay();
			}
		}
	}
	
	private DayEntry createDayEntry(LineItemEntry lineItemEntry, int calcFare) {
		DayEntry dayEntry = new DayEntry(lineItemEntry.getDateTime(), 
				lineItemEntry.getFromZone(), 
				lineItemEntry.getToZone());
		dayEntry.setFare(calcFare);
		return dayEntry;
	}	
}
