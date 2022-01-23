package com.tigercard.farecalc.rules;

import java.time.LocalDateTime;

import com.tigercard.farecalc.model.FareType;

public class FareTimeRules {
	
	public static FareType getFareType(LocalDateTime localDateTime) {
		
		FareType fareType = FareType.OffPeak;
		
		LocalDateTime tenThirtyThatDay = localDateTime.withHour(10).withMinute(30).withSecond(0).withNano(0);
		LocalDateTime twentyThatDay = localDateTime.withHour(20).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime elevanThatDay = localDateTime.withHour(11).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime twentyTwoThatDay = localDateTime.withHour(22).withMinute(0).withSecond(0).withNano(0);
		
		//Mon - Fri
		//Peak hours 07:00-10:30, 17:00-20:00
		//Sat-Sun
		//Peak hours 09:00-11:00, 18:00-22:00
		if(localDateTime.getDayOfWeek().getValue() >= 1 &&
				localDateTime.getDayOfWeek().getValue() <= 5) {
			if(localDateTime.getHour() >= 7 &&
					(localDateTime.isBefore(tenThirtyThatDay) || localDateTime.isEqual(tenThirtyThatDay)) ) {
				fareType = FareType.Peak;
			}
			
			if(localDateTime.getHour() >= 17 &&
					(localDateTime.isBefore(twentyThatDay) || localDateTime.isEqual(twentyThatDay)) ) {
				fareType = FareType.Peak;
			}
		}
		
		if(localDateTime.getDayOfWeek().getValue() >= 6 &&
				localDateTime.getDayOfWeek().getValue() <= 7) {
			if(localDateTime.getHour() >= 9 &&
					(localDateTime.isBefore(elevanThatDay) || localDateTime.isEqual(elevanThatDay)) ) {
				fareType = FareType.Peak;
			}
			
			if(localDateTime.getHour() >= 18 &&
					(localDateTime.isBefore(twentyTwoThatDay) || localDateTime.isEqual(twentyTwoThatDay)) ) {
				fareType = FareType.Peak;
			}
		}
		
		return fareType;
	}

}
