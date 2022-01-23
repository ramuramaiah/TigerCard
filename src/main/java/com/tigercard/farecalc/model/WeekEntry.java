package com.tigercard.farecalc.model;

import java.time.LocalDateTime;

public class WeekEntry {
	private LocalDateTime localDateTime;
	private String zones;
	private int fare;
	private String description;
	
	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}
	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}
	public String getZones() {
		return zones;
	}
	public void setZones(String zones) {
		this.zones = zones;
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
		return "WeekEntry [localDateTime=" + localDateTime + ", zones=" + zones + ", fare=" + fare + "]";
	}
}
