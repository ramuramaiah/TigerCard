package com.tigercard.farecalc.model;

import java.time.LocalDateTime;

public class LineItemEntry {
	private LocalDateTime dateTime;
	private Zone fromZone;
	private Zone toZone;
	
	public LineItemEntry(LocalDateTime dateTime, Zone fromZone, Zone toZone) {
		this.dateTime = dateTime;
		this.fromZone = fromZone;
		this.toZone = toZone;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public Zone getFromZone() {
		return fromZone;
	}

	public Zone getToZone() {
		return toZone;
	}
	
	@Override
	public String toString() {
		return "LineItemEntry [dateTime=" + dateTime + ", fromZone=" + fromZone + ", toZone=" + toZone + "]";
	}
}
