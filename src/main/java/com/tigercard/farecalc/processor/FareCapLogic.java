package com.tigercard.farecalc.processor;

import org.apache.log4j.Logger;

import com.tigercard.farecalc.model.FareCap;
import com.tigercard.farecalc.model.FareChart;
import com.tigercard.farecalc.model.FareType;
import com.tigercard.farecalc.model.LineItemEntry;
import com.tigercard.farecalc.rules.FareTimeRules;

/**
 * The business logic of fare computation including capping. <b><i>All in one place.</i></b>
 */
public class FareCapLogic {
	static Logger log = Logger.getLogger(FareCapLogic.class.getName());
	
	public static int calculateFare(SharedState sharedState, LineItemEntry lineItemEntry) {
			int accrualDaily = sharedState.dayEntries.stream()
					.map(e-> e.getFare())
					.reduce(0, Integer::sum);
			
			int accrualWeekly = sharedState.weekEntries.stream()
					.map(e-> e.getFare())
					.reduce(0, Integer::sum);
			
			FareType fareType = FareTimeRules.getFareType(lineItemEntry.getDateTime());
			int fareAmount = FareChart.getFareAmount(lineItemEntry.getFromZone(), lineItemEntry.getToZone(), fareType);
			int dailyCap = FareCap.getDailyCap(sharedState.farthestTripInADay.getKey(), sharedState.farthestTripInADay.getValue());
			int weeklyCap = FareCap.getWeeklyCap(sharedState.farthestTripInAWeek.getKey(), sharedState.farthestTripInAWeek.getValue());
			int calcFare = fareAmount;
			
			StringBuilder logMsg = new StringBuilder();
			
			if(accrualWeekly + fareAmount >= weeklyCap) {
				calcFare = weeklyCap - accrualWeekly;
				logMsg.append("Weekly cap reached. ");
			} else if(accrualDaily + fareAmount >= dailyCap) {
				calcFare = dailyCap - accrualDaily;
				logMsg.append("Daily cap reached. ");
			}
			
			logMsg.append("The calculated fare for the line item entry: " + 
						lineItemEntry.toString() + " is: " + calcFare);
			log.info(logMsg);
			
			return calcFare;
	}
}
