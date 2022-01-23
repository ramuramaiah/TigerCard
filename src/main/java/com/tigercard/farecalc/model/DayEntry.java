package com.tigercard.farecalc.model;

import java.time.LocalDateTime;

public class DayEntry {
	private LocalDateTime dateTime;
	private Zone fromZone;
	private Zone toZone;
	private int fare;
	private String description;
	
	public DayEntry(LocalDateTime dateTime, Zone fromZone, Zone toZone) {
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

	public int getFare() {
		return fare;
	}

	public void setFare(int fare) {
		this.fare = fare;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "DayEntry [dateTime=" + dateTime + ", fromZone=" + fromZone + ", toZone=" + toZone + ", fare=" + fare
				+ "]";
	}
}
