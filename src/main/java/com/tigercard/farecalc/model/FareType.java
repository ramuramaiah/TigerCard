package com.tigercard.farecalc.model;

public enum FareType {
	
	Peak("Peak"),
	OffPeak("OffPeak");
	
	public final String label;
	
	private FareType(String label) {
		this.label = label;
	}
}
