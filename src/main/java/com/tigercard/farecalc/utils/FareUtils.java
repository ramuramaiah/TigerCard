package com.tigercard.farecalc.utils;

import java.time.DayOfWeek;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;

import com.tigercard.farecalc.model.LineItemEntry;
import com.tigercard.farecalc.model.Zone;

public class FareUtils {

	public static int zoneDiff(Zone fromZone, Zone toZone) {
		if(fromZone == null || toZone == null) {
			throw new IllegalArgumentException("Input Zones cannot be null");
		}
		
		int diff = toZone.getZoneId() - fromZone.getZoneId();
		if(diff <0 ) {
			diff = diff*-1;
		}
		return diff;
	}
	
	public static boolean isNewWeek(LineItemEntry lineItemEntry, LineItemEntry lastProcessedEntry) {
		// create WeekFields
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
  
        // apply weekOfMonth()
        TemporalField weekOfMonth = weekFields.weekOfMonth();
        
		if (lineItemEntry.getDateTime().get(weekOfMonth) > lastProcessedEntry.getDateTime().get(weekOfMonth)) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isNewDay(LineItemEntry lineItemEntry, LineItemEntry lastProcessedEntry) {
		if(lineItemEntry.getDateTime().getDayOfWeek().getValue() > 
			lastProcessedEntry.getDateTime().getDayOfWeek().getValue()) {
			return true;
		}
		
		return false;
	}
}
